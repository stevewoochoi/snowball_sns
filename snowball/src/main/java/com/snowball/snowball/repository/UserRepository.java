package com.snowball.snowball.repository;

import com.snowball.snowball.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable; // ★이게 반드시 필요!

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // 추가 커스텀 쿼리는 여기에 작성
    @Query("SELECT u FROM User u WHERE " +
       "LOWER(u.nickname) LIKE LOWER(CONCAT('%', :q, '%')) " +
       "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchUser(@Param("q") String q, Pageable pageable);
}