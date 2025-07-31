package com.snowball.snowball.service;

import com.snowball.snowball.entity.*;
import com.snowball.snowball.repository.SpotGalleryPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotGalleryPhotoService {

    @Autowired
    private SpotGalleryPhotoRepository photoRepository;

    // useYn="Y"만 반환
    public List<SpotGalleryPhoto> getPhotosBySpot(Long spotId) {
        return photoRepository.findBySpotIdAndUseYn(spotId, "Y");
    }

    public SpotGalleryPhoto uploadPhoto(Long spotId, User uploader, String imageUrl, Double lat, Double lng) {
        SpotGalleryPhoto photo = new SpotGalleryPhoto();
        photo.setSpot(new Spot(spotId));
        photo.setUploader(uploader);
        photo.setImageUrl(imageUrl);
        photo.setLat(lat);
        photo.setLng(lng);
        photo.setUseYn("Y"); // 반드시 명시 (기본값이 있어도)
        return photoRepository.save(photo);
    }

    // 논리삭제 메서드도 추가 (필요시)
    public void deletePhoto(Long photoId) {
        SpotGalleryPhoto photo = photoRepository.findById(photoId).orElseThrow();
        photo.setUseYn("N");
        photoRepository.save(photo);
    }
}