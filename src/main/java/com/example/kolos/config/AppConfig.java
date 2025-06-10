package com.example.kolos.config; // Убедитесь, что пакет правильный

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Эта аннотация указывает Spring, что это конфигурационный класс
public class AppConfig {

    @Bean // Эта аннотация указывает Spring, что метод создает Bean, который будет управляться Spring IoC контейнером
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}