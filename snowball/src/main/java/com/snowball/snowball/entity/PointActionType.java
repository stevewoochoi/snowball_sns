package com.snowball.snowball.entity;

public enum PointActionType {
    post,              // 게시글 작성
    gallery_upload,    // 사진 업로드
    spot_cloned,       // 내 스팟 복제됨 (포인트 +)
    spot_clone_used    // 내가 남 스팟 복제 (포인트 -)
}