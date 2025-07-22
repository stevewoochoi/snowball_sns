package com.snowball.snowball.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sns_type")
    private String snsType;

    @Column(name = "sns_id")
    private String snsId;

    private String username;
    private String nickname;
    private String phone;
    private String email;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    private Integer points = 0;

    // Getter, Setter 등 Lombok 쓰면 @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor
    // Lombok을 안 쓸 거라면 아래처럼 직접 작성

    public User() {}

    // ... getter/setter 생략 ...
    // 필요하면 자동 생성 도구(IDE, Lombok 등) 사용

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSnsType() {
        return snsType;
    }

    public void setSnsType(String snsType) {
        this.snsType = snsType;
    }

    public String getSnsId() {
        return snsId;
    }

    public void setSnsId(String snsId) {
        this.snsId = snsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}