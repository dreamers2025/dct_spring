package com.dreamers2025.dct.domain.dream.entity;

import com.dreamers2025.dct.domain.interpreter.entity.InterpreterType;
import com.dreamers2025.dct.domain.user.dto.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREAMENT 옵션
    // MySql 계열은 GenerationType.IDENTITY 사용, PostgreSQL이나 Oracle에선 GenerationType.SEQUENCE
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 외래 키
    private User user;

    @Column(length = 50)
    private String summary; // 요약

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private InterpreterType interpreter;

    @Column(length = 700, nullable = false)
    private String content; // 해몽 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate date; // 기록일
}
