package com.balita.springexamplecrud.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "REFRESH_TOKEN")
public class RefreshToken  extends DateAudit{

    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
    private Long id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String token;

    @Column(name = "REFRESH_COUNT")
    private Long refreshCount;

    @Column(name = "EXPIRY_DT", nullable = false)
    private Instant expiryDate;

    public RefreshToken() {
    }

    public RefreshToken(Long id, String token, Long refreshCount, Instant expiryDate) {
        this.id = id;
        this.token = token;
        this.refreshCount = refreshCount;
        this.expiryDate = expiryDate;
    }

    public void incrementRefreshCount() {
        refreshCount = refreshCount + 1;
    }
}
