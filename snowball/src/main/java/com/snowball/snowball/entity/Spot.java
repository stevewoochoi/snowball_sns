package com.snowball.snowball.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "spot")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "map_id")
    private Long mapId;

    private String name;
    private String type;

    // 기존: category (문자열) → category_id (마스터와 연관)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Double lat;
    private Double lng;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "origin_spot_id")
    private Long originSpotId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;


    @Column(name = "scope")
    private String scope; // "PRIVATE", "FRIENDS", "PUBLIC"

    public Spot(Long id) {
        this.id = id;
    }

    @Builder.Default
    @Column(name = "use_yn", length = 1)
    private String useYn = "Y";

    @Builder.Default
    @OneToMany(mappedBy = "spot", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SpotBoardPost> posts = new ArrayList<>();
}