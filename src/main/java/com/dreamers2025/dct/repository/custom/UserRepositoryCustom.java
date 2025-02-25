package com.dreamers2025.dct.repository.custom;

public interface UserRepositoryCustom {
    long updateUserGrade(Long id);
    long expireGrade(Long id);
    long updateUsername(Long id,String newName);
}
