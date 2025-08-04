// src/main/java/com/snowball/snowball/dto/UserFriendDto.java
package com.snowball.snowball.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserFriendDto {
    private Long id;
    private Long userId;          // 나의 user id
    private Long friendId;        // 친구 user id
    private String nickname;      // 친구 닉네임
    private String email;         // 친구 이메일
    private String status;        // REQUESTED, ACCEPTED, REJECTED 등
    private String direction;     // INCOMING, OUTGOING 등
}