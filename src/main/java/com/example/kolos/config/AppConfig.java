package com.example.kolos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
// --- НОВЫЙ ИМПОРТ: ---
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
// --- КОНЕЦ НОВОГО ИМПОРТА ---

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // --- ПЕРЕХОД НА PBKDF2PasswordEncoder с полным конструктором ---
        // Используем рекомендуемые по умолчанию значения:
        // saltLength: 16 (длина соли в байтах)
        // iterations: 185000 (количество итераций)
        // algorithm: SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        //tring pbkdf2Secret = "BWG6jeWxuO3nSlLbmPF5nt4LuUiyEQNdOdU7bBhgrT328NgyZTKa0f1GCW7n5wMVDGgF/hsIcbNi0PV1bCUbg=="; // Ваш секретный ключ

//        return new Pbkdf2PasswordEncoder(
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    }
}