package com.br.devdiaries.repository;

import com.br.devdiaries.model.RevokedToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRevokedToken extends JpaRepository<RevokedToken, Integer> {
    Optional<RevokedToken> findByToken(String token);
}