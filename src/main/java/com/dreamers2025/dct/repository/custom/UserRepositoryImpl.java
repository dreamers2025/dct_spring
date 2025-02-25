package com.dreamers2025.dct.repository.custom;

import com.dreamers2025.dct.domain.user.dto.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QUser user =QUser.user;
    @Override
    public long updateUserGrade(Long id){
        return queryFactory
                .update(user)
                .set(user.usergrade, "premium")
                .set(user.gradeExpiry, LocalDateTime.now().plusDays(30))
                .where(user.id.eq(id))
                .execute();
    }

    @Override
    public long expireGrade(Long id){
        return queryFactory
                .update(user)
                .set(user.usergrade,"free")
                .set(user.gradeExpiry, (LocalDateTime) null)
                .where(user.id.eq(id).and(user.gradeExpiry.before(LocalDateTime.now())))
                .execute();
    }

    @Override
    public long updateUsername(Long id,String newName){
        return queryFactory
                .update(user)
                .set(user.username,newName)
                .where(user.id.eq(id))
                .execute();
    }
}
