package com.snowball.snowball.controller;

import com.snowball.snowball.entity.UserFriend;
import com.snowball.snowball.service.UserFriendService;
import org.springframework.web.bind.annotation.*;
import com.snowball.snowball.dto.UserFriendDto;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class UserFriendController {
    private final UserFriendService userFriendService;

    public UserFriendController(UserFriendService userFriendService) {
        this.userFriendService = userFriendService;
    }

    // 친구 요청 보내기
    @PostMapping("/request")
    public UserFriend sendFriendRequest(@RequestParam Long userId, @RequestParam Long friendId) {
        return userFriendService.sendFriendRequest(userId, friendId);
    }

    // 친구 요청 수락
    @PostMapping("/accept")
    public void acceptFriendRequest(@RequestParam Long userId, @RequestParam Long friendId) {
        userFriendService.acceptFriendRequest(userId, friendId);
    }

    // 친구 삭제/거절
    @DeleteMapping
    public void removeFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        userFriendService.removeFriend(userId, friendId);
    }

    // // 친구 목록
    // @GetMapping
    // public List<UserFriend> getFriends(@RequestParam Long userId) {
    // return userFriendService.getFriends(userId);
    // }

    // // 내게 온 친구 요청 목록
    // @GetMapping("/requests")
    // public List<UserFriend> getPendingRequests(@RequestParam Long userId) {
    // return userFriendService.getPendingFriendRequests(userId);
    // }

    @GetMapping("/requests")
    public List<UserFriendDto> getPendingRequests(@RequestParam Long userId) {
        return userFriendService.getPendingFriendRequests(userId);
    }

    // 친구 목록
    @GetMapping
    public List<UserFriendDto> getFriends(@RequestParam Long userId) {
        return userFriendService.getFriendsDto(userId); // getFriendsDto도 DTO 반환
    }

    // 내가 보낸 친구 요청 목록 (내가 남에게 보낸 요청만)
    @GetMapping("/requests/sent")
    public List<UserFriendDto> getSentFriendRequests(@RequestParam Long userId) {
        return userFriendService.getSentFriendRequests(userId);
    }
}