package com.snowball.snowball.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "point_logs")
public class PointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PointActionType type;  // e.g., gallery_upload, post, etc.

    private Long targetId;         // spot_gallery_photo id or similar

    private Integer changeAmount;

    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setType(PointActionType type) {
        this.type = type;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public void setChangeAmount(Integer changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}