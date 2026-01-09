package com.pocketbudget.pocketbudget.security;
import java.time.Instant;

import com.pocketbudget.pocketbudget.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="refresh_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RefreshToken {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false, unique=true, length=200)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;




}
