package com.dreamers2025.dct.repository.custom;

import com.dreamers2025.dct.domain.user.dto.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QUser user =QUser.user;
    @Override
    public long updateUserGrade(Long id){
        return queryFactory
                .update(user)
                .set(user.usergrade, "premium")
                .where(user.id.eq(id))
                .execute();
    }
}
