package com.dreamers2025.dct.domain.dream.entity;

import com.dreamers2025.dct.domain.interpreter.entity.Interpreter;
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
    private Long dreamId;

    private Long userId;

    @Column(length = 40)
    private String summary; // 요약

    @ManyToOne
    @JoinColumn(name = "interpreter_id")
    private Interpreter interpreter;

    private String content; // 해몽 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate date; // 기록일
}
