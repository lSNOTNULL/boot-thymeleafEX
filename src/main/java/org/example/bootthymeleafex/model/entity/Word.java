package org.example.bootthymeleafex.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data // Lombok
@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    @Column(nullable = false, length = 3)
    private String text;
    @CreationTimestamp
    // priavte String createdAt; // 일시는 문자열이 가장 안전하다 but 계산이 애매함.
    private LocalDateTime createdAt;


}
