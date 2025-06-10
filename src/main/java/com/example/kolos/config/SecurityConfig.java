package com.example.kolos.config;

import com.example.kolos.security.JwtAuthenticationEntryPoint;
import com.example.kolos.security.JwtRequestFilter;
import com.example.kolos.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Для @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // Включает веб-безопасность Spring Security
@EnableMethodSecurity(prePostEnabled = true) // Включает аннотации @PreAuthorize, @PostAuthorize
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtRequestFilter jwtRequestFilter,
                          CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // Определяем AuthenticationManager, который будет использоваться для аутентификации пользователей
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Настраиваем DaoAuthenticationProvider для работы с CustomUserDetailsService и PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // Основная цепочка фильтров безопасности
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF, так как JWT защищает от CSRF по своей природе
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Применяем CORS (если нужен)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Обработка неаутентифицированных запросов
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Сессия не используется, так как у нас JWT
                .authorizeHttpRequests(authorize -> authorize
                        // Разрешаем доступ без аутентификации к эндпоинту аутентификации и регистрации
                        .requestMatchers("/authenticate", "/register", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                );

        // Добавляем наш JWT фильтр перед UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Включаем провайдер аутентификации
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    // CORS Configuration (если ваше фронтенд-приложение находится на другом домене/порту)
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Разрешить любой домен (для разработки). В продакшене укажите конкретные домены.
        configuration.addAllowedMethod("*"); // Разрешить все HTTP методы (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedHeader("*"); // Разрешить все заголовки
        configuration.setAllowCredentials(true); // Разрешить отправку куки и заголовков авторизации
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Применить CORS ко всем путям
        return source;
    }
}