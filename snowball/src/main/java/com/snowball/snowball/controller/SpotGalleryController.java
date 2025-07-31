package com.snowball.snowball.controller;

import java.util.List;

import com.snowball.snowball.entity.*;
import com.snowball.snowball.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spots/{spotId}/gallery")
public class SpotGalleryController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpotGalleryPhotoRepository photoRepository;

    @Autowired
    private PointLogRepository pointLogRepository;

    // [1] S3 presign 업로드 완료 후 DB등록 (JSON 요청)
    @PostMapping("/presigned")
    public SpotGalleryPhoto uploadByPresignedUrl(
            @PathVariable Long spotId,
            @RequestBody GalleryUploadRequest request,
            @RequestAttribute(value = "user", required = false) User uploader) {

        // [원인 추적용 로그 추가]
        System.out.println("[업로드] @RequestAttribute user: " + (uploader != null ? uploader.getId() : "null"));

        // 기존 로직 유지 (null일 때 1번 게스트로 대체)
        if (uploader == null) {
            uploader = userRepository.findById(1L).orElse(null);
            System.out.println("[업로드] user가 null이라 게스트(1)로 대체: " + (uploader != null ? uploader.getId() : "null"));
        }
        SpotGalleryPhoto photo = new SpotGalleryPhoto();
        photo.setSpot(new Spot(spotId));
        photo.setUploader(uploader);
        photo.setImageUrl(request.getImageUrl());
        photo.setLat(request.getLat());
        photo.setLng(request.getLng());
        SpotGalleryPhoto savedPhoto = photoRepository.save(photo);

        if (uploader != null) {
            uploader.setPoints((uploader.getPoints() == null ? 0 : uploader.getPoints()) + 3);
            userRepository.save(uploader);

            PointLog log = new PointLog();
            log.setUser(uploader);
            log.setType(PointActionType.gallery_upload);
            log.setTargetId(savedPhoto.getId());
            log.setChangeAmount(3);
            log.setDescription("갤러리 사진 업로드로 인한 포인트 적립");
            pointLogRepository.save(log);
        }
        return savedPhoto;
    }

    // [2] 기존 멀티파트 업로드 (form)
    @PostMapping(consumes = { "multipart/form-data" })
    public SpotGalleryPhoto uploadPhoto(
            @PathVariable Long spotId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "lat", required = false) Double lat,
            @RequestParam(value = "lng", required = false) Double lng,
            @RequestAttribute(value = "user", required = false) User uploader) throws Exception {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String savePath = "/tmp/" + fileName;
        file.transferTo(new java.io.File(savePath));
        String imageUrl = "/uploads/" + fileName; // 실서비스 S3 대체

        if (uploader == null) {
            uploader = userRepository.findById(1L).orElse(null); // 1번 게스트
        }

        SpotGalleryPhoto photo = new SpotGalleryPhoto();
        photo.setSpot(new Spot(spotId));
        photo.setUploader(uploader);
        photo.setImageUrl(imageUrl);
        photo.setLat(lat);
        photo.setLng(lng);
        SpotGalleryPhoto savedPhoto = photoRepository.save(photo);

        if (uploader != null) {
            uploader.setPoints((uploader.getPoints() == null ? 0 : uploader.getPoints()) + 3);
            userRepository.save(uploader);

            PointLog log = new PointLog();
            log.setUser(uploader);
            log.setType(PointActionType.gallery_upload);
            log.setTargetId(savedPhoto.getId());
            log.setChangeAmount(3);
            log.setDescription("갤러리 사진 업로드로 인한 포인트 적립");
            pointLogRepository.save(log);
        }

        return savedPhoto;
    }

    @PatchMapping("/{photoId}/delete")
    public SpotGalleryPhoto deletePhoto(
            @PathVariable Long spotId,
            @PathVariable Long photoId) {
        SpotGalleryPhoto photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("사진이 존재하지 않습니다."));
        photo.setUseYn("N");
        return photoRepository.save(photo);
    }

    public static class GalleryUploadRequest {
        private String imageUrl;
        private Double lat;
        private Double lng;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    @GetMapping
    public List<SpotGalleryPhoto> getGallery(@PathVariable Long spotId) {
        return photoRepository.findBySpotIdAndUseYn(spotId, "Y");
    }
}