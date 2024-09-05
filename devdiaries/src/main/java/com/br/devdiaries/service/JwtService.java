package com.br.devdiaries.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    // Retorna a chave de assinatura decodificada do segredo JWT
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrai o nome de usuário (subject) do token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

     // Extrai um determinado claim do token JWT usando uma função fornecida
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrai todos os claims do token JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Gera um token JWT usando o nome de usuário como subject
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Cria um token JWT com claims personalizados, subject, data de emissão e data de expiração
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validade
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida o token JWT verificando o nome de usuário e a expiração
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Verifica se o token JWT está expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrai a data de expiração do token JWT
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // Extrai o token JWT do cabeçalho de autorização da requisição HTTP
    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
         return authorizationHeader.substring(7);
        }
        return null;
}

}
