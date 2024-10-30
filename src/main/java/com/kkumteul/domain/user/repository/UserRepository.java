package com.kkumteul.domain.user.repository;

import com.kkumteul.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByRefreshToken(String refreshToken);
    boolean existsByNickName(String nickName); // 닉네임 중복 여부 확인
    boolean existsByUsername(String username); // 아이디 중복 여부 확인
}