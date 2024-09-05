package com.br.devdiaries.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

//Entidade da tabela de tokens revogados no banco de dados
@Entity
public class RevokedToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "token cannot be empty")
    @Column(name = "token", length = 500, nullable = false)
    private String token;
    
    @Column(name = "revoked_at", nullable = false)
    private LocalDateTime revokedAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    
}
