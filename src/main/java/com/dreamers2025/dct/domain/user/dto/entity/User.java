package com.dreamers2025.dct.domain.user.dto.entity;

import com.dreamers2025.dct.domain.dream.entity.Dream;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_username", columnList = "username")
        }
)
@Getter
@ToString
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Setter
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    // @Column(nullable = false, length = 20)
    // private String usergrade="free";

    // @Column
    // private LocalDateTime gradeExpiry=null;

    @Column(length = 255)
    private String refreshToken;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dream> dreams = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addDream(Dream dream) {
        dreams.add(dream);
        dream.setUser(this);
    }

    @Builder
    private User(String email,String password,String username){
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
