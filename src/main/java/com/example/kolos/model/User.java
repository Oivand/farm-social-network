package com.example.kolos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // <-- Важный импорт

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails { // <-- ДОБАВЛЕНО: implements UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private Long idUser;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String nickname; // Будет использоваться как username для Spring Security

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    // Это поле будет использоваться Spring Security для определения строковых ролей (например, "ROLE_USER")
    // Имя столбца остается 'role_rights', как вы указали.
    // 'nullable = false' - если вы хотите, чтобы столбец был NOT NULL в БД.
    // 'columnDefinition' обеспечивает значение по умолчанию для новых записей в БД.
    @Column(name = "role_rights", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ROLE_USER'")
    private String role_rights;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_region", nullable = true)
    private Region userRegion;

    // Это поле представляет собой связь с таблицей ролей (RolesUsers),
    // оно отличается от role_rights, которое является строкой для Spring Security GrantedAuthority.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role", nullable = false)
    private RolesUsers role; // Название поля 'role', но столбец 'user_role'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sector", nullable = false)
    private Sector sector;

    @Column(name="bio", nullable = true)
    private String bio;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_since_time")
    private LocalDateTime lastSinceTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "hash_password") // Имя столбца 'hash_password', как вы указали
    private String password; // Будет использоваться как password для Spring Security

    // --- Методы интерфейса UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Используем поле 'role_rights' для Spring Security, т.к. оно строковое и содержит имя роли.
        // Предполагаем, что 'role_rights' уже содержит префикс "ROLE_" (например, "ROLE_USER").
        return Collections.singletonList(new SimpleGrantedAuthority(this.role_rights));
    }

    @Override
    public String getUsername() {
        // Возвращает имя пользователя (логин) для Spring Security.
        return this.nickname;
    }

    @Override
    public String getPassword() {
        // Возвращает пароль для Spring Security.
        // Соответствует столбцу 'hash_password' и полю 'password'.
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}