package com.snowball.snowball.repository;

import com.snowball.snowball.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 추가 커스텀 쿼리는 여기에 작성
}