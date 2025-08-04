package com.snowball.snowball.service;

import com.snowball.snowball.dto.UserFriendDto;
import com.snowball.snowball.entity.User;
import com.snowball.snowball.entity.UserFriend;
import com.snowball.snowball.repository.UserFriendRepository;
import com.snowball.snowball.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.snowball.snowball.dto.UserFriendDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFriendService {
    private final UserFriendRepository userFriendRepository;
    private final UserRepository userRepository;

    public UserFriendService(UserFriendRepository userFriendRepository, UserRepository userRepository) {
        this.userFriendRepository = userFriendRepository;
        this.userRepository = userRepository;
    }

    // 친구 요청 보내기
    public UserFriend sendFriendRequest(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow();
        User friend = userRepository.findById(friendId).orElseThrow();
        // 중복 체크 (이미 요청이 있거나 친구인 경우)
        if (userFriendRepository.existsByUserAndFriend(user, friend)) {
            throw new IllegalArgumentException("이미 친구 요청을 보냈거나 친구입니다.");
        }
        // 나 → 친구 : OUTGOING
        UserFriend outgoing = UserFriend.builder()
                .user(user)
                .friend(friend)
                .createdAt(LocalDateTime.now())
                .status("REQUESTED")
                .direction("OUTGOING")
                .build();
        // 친구 → 나 : INCOMING
        UserFriend incoming = UserFriend.builder()
                .user(friend)
                .friend(user)
                .createdAt(LocalDateTime.now())
                .status("REQUESTED")
                .direction("INCOMING")
                .build();
        userFriendRepository.save(outgoing);
        userFriendRepository.save(incoming);
        return outgoing;
    }

    // 친구 요청 수락 (나 → 친구: status, 친구 → 나: status 둘 다 ACCEPTED)
    public void acceptFriendRequest(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow();
        User friend = userRepository.findById(friendId).orElseThrow();
        UserFriend incoming = userFriendRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청이 없습니다."));
        UserFriend outgoing = userFriendRepository.findByUserAndFriend(friend, user)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청이 없습니다."));
        incoming.setStatus("ACCEPTED");
        outgoing.setStatus("ACCEPTED");
        userFriendRepository.save(incoming);
        userFriendRepository.save(outgoing);
    }

    // 친구 요청 거절/취소 (둘 다 삭제)
    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow();
        User friend = userRepository.findById(friendId).orElseThrow();
        userFriendRepository.deleteByUserAndFriend(user, friend);
        userFriendRepository.deleteByUserAndFriend(friend, user);
    }

    // 나의 친구 목록 (ACCEPTED만)
    public List<UserFriend> getFriends(Long userId) {
        return userFriendRepository.findByUserIdAndStatus(userId, "ACCEPTED");
    }

    // // 나의 친구 요청 목록 (INCOMING, REQUESTED만)
    // public List<UserFriend> getPendingFriendRequests(Long userId) {
    // return userFriendRepository.findByUserIdAndDirectionAndStatus(userId,
    // "INCOMING", "REQUESTED");
    // }

    public List<UserFriendDto> getPendingFriendRequests(Long userId) {
        // "내가 받은" 친구 요청만 반환
        List<UserFriend> requests = userFriendRepository.findIncomingFriendRequests(userId);
        return requests.stream().map(req -> {
            // INCOMING 기준: 요청을 보낸 상대는 req.getFriend()
            User requester = req.getFriend();
            return UserFriendDto.builder()
                    .id(req.getId())
                    .userId(req.getUser().getId())
                    .friendId(req.getFriend().getId())
                    .nickname(requester.getNickname())
                    .email(requester.getEmail())
                    .status(req.getStatus())
                    .direction(req.getDirection())
                    .build();
        }).collect(Collectors.toList());
    }

    public List<UserFriendDto> getFriendsDto(Long userId) {
        // 친구 관계 ACCEPTED 된 목록 (user 또는 friend가 userId)
        List<UserFriend> friends = userFriendRepository.findAcceptedFriends(userId);

        return friends.stream().map(f -> {
            // 상대방 정보 (direction에 따라 다름)
            User other = (f.getUser().getId().equals(userId)) ? f.getFriend() : f.getUser();
            return UserFriendDto.builder()
                    .id(f.getId())
                    .userId(f.getUser().getId())
                    .friendId(f.getFriend().getId())
                    .nickname(other.getNickname())
                    .email(other.getEmail())
                    .status(f.getStatus())
                    .direction(f.getDirection())
                    .build();
        }).collect(Collectors.toList());
    }

    // 내가 보낸 친구 요청 목록 (OUTGOING, REQUESTED만)
    public List<UserFriendDto> getSentFriendRequests(Long userId) {
        // 내가 보낸 요청은 userId가 나고, direction='OUTGOING', status='REQUESTED'
        List<UserFriend> requests = userFriendRepository.findOutgoingFriendRequests(userId);
        return requests.stream().map(req -> {
            User receiver = req.getFriend(); // 내가 친구 신청을 보낸 대상
            return UserFriendDto.builder()
                    .id(req.getId())
                    .userId(req.getUser().getId()) // 나 (요청자)
                    .friendId(req.getFriend().getId()) // 상대방 (받는 사람)
                    .nickname(receiver.getNickname())
                    .email(receiver.getEmail())
                    .status(req.getStatus())
                    .direction(req.getDirection())
                    .build();
        }).collect(Collectors.toList());
    }
}