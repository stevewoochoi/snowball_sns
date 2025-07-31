package com.snowball.snowball.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "spot_gallery_photos")
public class SpotGalleryPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @ManyToOne
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    @Column(nullable = false)
    private String imageUrl;

    private Double lat;

    private Double lng;

    private LocalDateTime exifTakenAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Y: 사용, N: 논리삭제
    @Column(name = "use_yn", length = 1)
    private String useYn = "Y";
}