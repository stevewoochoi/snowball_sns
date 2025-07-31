package com.snowball.snowball.repository;

import com.snowball.snowball.entity.SpotGalleryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotGalleryPhotoRepository extends JpaRepository<SpotGalleryPhoto, Long> {
    // 기존: List<SpotGalleryPhoto> findBySpotId(Long spotId);
    // 변경(추가): useYn이 Y인 사진만 조회
    List<SpotGalleryPhoto> findBySpotIdAndUseYn(Long spotId, String useYn);
}