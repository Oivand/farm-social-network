// src/main/java/com/example/kolos/dto/UserRegistrationDto.java
package com.example.kolos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

// (Необязательно: lombok аннотации для сокращения кода)
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;

// @Data // Генерирует геттеры, сеттеры, toString, equals, hashCode
// @NoArgsConstructor // Генерирует конструктор без аргументов
// @AllArgsConstructor // Генерирует конструктор со всеми аргументами
@Data
public class UserRegistrationDto {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format") // Пример regex для телефона
    private String phone;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Size(max = 50, message = "Surname cannot exceed 50 characters")
    private String surname;

    @NotBlank(message = "Nickname cannot be empty")
    @Size(min = 3, max = 30, message = "Nickname must be between 3 and 30 characters")
    private String nickname; // Соответствует полю 'username' в вашей сущности User

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    // Укажите ваши требования к сложности пароля
    private String password;

    // --- Поля для связанных сущностей (Region, Sector, Role) ---
    // Важно: Здесь мы принимаем ТОЛЬКО ID, а не полные объекты!
    @NotNull(message = "Role ID cannot be null")
    private Long idRoleUser;

    @NotNull(message = "Sector ID cannot be null")
    private Long idSector;

    @NotNull(message = "Region ID cannot be null")
    private Long idRegion;

    @NotNull(message = "Enter your date of birth")
    private LocalDate dateOfBirth;

    // --- Конструкторы, Геттеры и Сеттеры ---
    // Если не используете Lombok, вам нужно будет написать их вручную.

    public UserRegistrationDto() {
    }


    // (Опционально) Переопределите toString() для удобства отладки
    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", idRoleUser=" + idRoleUser +
                ", idSector=" + idSector +
                ", idRegion=" + idRegion +
                '}';
    }
}
