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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // =========================================================================
        // НОВОЕ: Проверка на публичные эндпоинты
        // Здесь мы явно пропускаем запросы, которые не требуют аутентификации.
        // Пути должны совпадать с теми, что указаны в .permitAll() в SecurityConfig.
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/authenticate") ||
                requestURI.equals("/register") ||
                requestURI.startsWith("/regions") || // Используйте startsWith для путей, которые могут иметь подпути (например, /regions/1)
                requestURI.startsWith("/sectors") ||
                requestURI.startsWith("/roles") ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/v3/api-docs/") ||
                requestURI.equals("/kindsComplaint")) {
            chain.doFilter(request, response); // Просто пропускаем запрос дальше
            return; // Завершаем выполнение этого фильтра для публичных путей
        }
        // =========================================================================

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                System.err.println("JWT Token is invalid or expired: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response); // Передаем запрос дальше по цепочке фильтров
    }
}