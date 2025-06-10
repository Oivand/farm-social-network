package com.example.kolos.security;

import com.example.kolos.service.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter; // <-- Это важный импорт!

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter { // <-- Класс должен расширять OncePerRequestFilter

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil; // <-- Это ссылка на *другой* класс JwtTokenUtil

    // Внедрение зависимостей через конструктор
    public JwtRequestFilter(CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Проверяем, есть ли заголовок Authorization и начинается ли он с "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Извлекаем сам токен
            try {
                // Используем JwtTokenUtil для извлечения имени пользователя из токена
                username = jwtTokenUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                // Если токен невалиден или срок его действия истек, логируем ошибку.
                // Вместо logger.warn() можно использовать System.err.println() для быстрого вывода
                System.err.println("JWT Token is invalid or expired: " + e.getMessage());
            }
        }

        // Если имя пользователя из токена есть и аутентификация для него еще не установлена в SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Загружаем UserDetails по имени пользователя (никнейму)
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            // Если токен валиден, устанавливаем аутентификацию
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                // Создаем объект аутентификации Spring Security
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Устанавливаем аутентификацию в SecurityContext
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Передаем запрос дальше по цепочке фильтров
        chain.doFilter(request, response);
    }
}