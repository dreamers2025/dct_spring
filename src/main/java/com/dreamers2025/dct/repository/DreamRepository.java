package com.dreamers2025.dct.repository;

import com.dreamers2025.dct.domain.dream.entity.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dreamers2025.dct.domain.user.dto.entity.User;

import java.util.List;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Long> {
    List<Dream> findByUser(User user); // 특정 유저의 꿈 목록 조회
}
