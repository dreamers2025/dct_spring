package com.dreamers2025.dct.repository;

import com.dreamers2025.dct.domain.user.dto.entity.User;
// import com.dreamers2025.dct.repository.custom.UserRepositoryCustom;
import com.dreamers2025.dct.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom
{

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

//    long expireGrade();
}
