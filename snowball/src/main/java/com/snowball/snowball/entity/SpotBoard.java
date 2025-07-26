package com.snowball.snowball.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "spot_boards")
public class SpotBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    private String name; // 게시판 이름(예: "GUEST BOOK", "NOTICE" 등)
    private LocalDateTime createdAt;

    public SpotBoard() {}

    public SpotBoard(Long id, Spot spot, String name, LocalDateTime createdAt) {
        this.id = id;
        this.spot = spot;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Spot getSpot() { return spot; }
    public void setSpot(Spot spot) { this.spot = spot; }
    public void setSpotId(Long spotId) {
        Spot s = new Spot();
        s.setId(spotId);
        this.spot = s;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}