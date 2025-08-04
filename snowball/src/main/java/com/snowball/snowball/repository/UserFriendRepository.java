package com.snowball.snowball.repository;

import com.snowball.snowball.entity.User;
import com.snowball.snowball.entity.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {
    boolean existsByUserAndFriend(User user, User friend);

    Optional<UserFriend> findByUserAndFriend(User user, User friend);

    void deleteByUserAndFriend(User user, User friend);

    List<UserFriend> findByUserIdAndStatus(Long userId, String status);

    List<UserFriend> findByUserIdAndDirectionAndStatus(Long userId, String direction, String status);

    // ✅ 내가 받은(수신) 친구 요청만 (상대가 나한테 보낸 요청)
    @Query("SELECT uf FROM UserFriend uf WHERE uf.user.id = :userId AND uf.direction = 'INCOMING' AND uf.status = 'REQUESTED'")
    List<UserFriend> findIncomingFriendRequests(@Param("userId") Long userId);

    // ✅ 내가 보낸(발신) 친구 요청만 (내가 남에게 보낸 요청)
    @Query("SELECT uf FROM UserFriend uf WHERE uf.user.id = :userId AND uf.direction = 'OUTGOING' AND uf.status = 'REQUESTED'")
    List<UserFriend> findOutgoingFriendRequests(@Param("userId") Long userId);

    @Query("SELECT uf FROM UserFriend uf " +
            "WHERE uf.status = 'ACCEPTED' AND " +
            "((uf.user.id = :userId OR uf.friend.id = :userId) " +
            "AND uf.user.id < uf.friend.id)")
    List<UserFriend> findAcceptedFriends(@Param("userId") Long userId);

}