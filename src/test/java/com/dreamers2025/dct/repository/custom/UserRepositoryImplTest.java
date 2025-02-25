package com.dreamers2025.dct.repository.custom;

import com.dreamers2025.dct.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("")
    void test1() {
        //given

        //when
        userRepository.expireGrade(1L);
        //then
    }
}