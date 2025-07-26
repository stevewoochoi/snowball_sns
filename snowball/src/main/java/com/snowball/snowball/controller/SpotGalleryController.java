package com.snowball.snowball.controller;

import java.util.List;

import com.snowball.snowball.entity.*;
import com.snowball.snowball.entity.User;
import com.snowball.snowball.config.repository.*;
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

    @PostMapping
    public SpotGalleryPhoto uploadPhoto(
            @PathVariable Long spotId,
            @RequestBody GalleryUploadRequest request,
            @RequestAttribute("user") User uploader
    ) {
        SpotGalleryPhoto photo = new SpotGalleryPhoto();
        photo.setSpot(new Spot(spotId));
        photo.setUploader(uploader);
        photo.setImageUrl(request.getImageUrl());
        photo.setLat(request.getLat());
        photo.setLng(request.getLng());
        SpotGalleryPhoto savedPhoto = photoRepository.save(photo);

        uploader.setPoints(uploader.getPoints() + 3);
        userRepository.save(uploader);

        PointLog log = new PointLog();
        log.setUser(uploader);
        log.setType(PointActionType.gallery_upload);
        log.setTargetId(savedPhoto.getId());
        log.setChangeAmount(3);
        log.setDescription("갤러리 사진 업로드로 인한 포인트 적립");
        pointLogRepository.save(log);

        return savedPhoto;
    }

    public static class GalleryUploadRequest {
        private String imageUrl;
        private Double lat;
        private Double lng;

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLng() { return lng; }
        public void setLng(Double lng) { this.lng = lng; }
    }
    @GetMapping
    public List<SpotGalleryPhoto> getGallery(@PathVariable Long spotId) {
        return photoRepository.findBySpotId(spotId);
    }
}