package com.snowball.snowball.config.repository;

import com.snowball.snowball.entity.SpotGalleryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotGalleryPhotoRepository extends JpaRepository<SpotGalleryPhoto, Long> {
    List<SpotGalleryPhoto> findBySpotId(Long spotId);
}