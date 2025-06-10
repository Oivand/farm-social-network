package com.example.kolos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}") // Секретный ключ из application.properties
    private String secret;

    @Value("${jwt.expiration}") // Время жизни токена в миллисекундах
    private long expiration;

    // Извлечение имени пользователя из токена
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Извлечение даты истечения токена
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Извлечение конкретной "заявки" (claims) из токена
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Извлечение всех "заявок" из токена
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Проверка, истек ли срок действия токена
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Генерация токена для пользователя
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Опционально: добавьте роли пользователя в claims, если это нужно для фронтенда
        // List<String> roles = userDetails.getAuthorities().stream()
        //                                 .map(GrantedAuthority::getAuthority)
        //                                 .collect(Collectors.toList());
        // claims.put("roles", roles);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Создание JWT токена
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 'sub' (subject) обычно имя пользователя (никнейм)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Дата выдачи
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Дата истечения
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Подписываем токен
                .compact();
    }

    // Валидация токена
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Получение секретного ключа
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}