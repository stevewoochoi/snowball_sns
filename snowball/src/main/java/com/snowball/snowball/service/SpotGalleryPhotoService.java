package com.snowball.snowball.service;

import com.snowball.snowball.entity.*;
import com.snowball.snowball.config.repository.SpotGalleryPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotGalleryPhotoService {

    @Autowired
    private SpotGalleryPhotoRepository photoRepository;

    public List<SpotGalleryPhoto> getPhotosBySpot(Long spotId) {
        return photoRepository.findBySpotId(spotId);
    }

    public SpotGalleryPhoto uploadPhoto(Long spotId, User uploader, String imageUrl, Double lat, Double lng) {
        SpotGalleryPhoto photo = new SpotGalleryPhoto();
        photo.setSpot(new Spot(spotId));
        photo.setUploader(uploader);
        photo.setImageUrl(imageUrl);
        photo.setLat(lat);
        photo.setLng(lng);
        return photoRepository.save(photo);
    }
}