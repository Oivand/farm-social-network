package com.example.kolos.service;

import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // Найти пользователя по email
    Optional<User> findUserByEmail(String email);

    // Найти пользователя по номеру телефона
    Optional<User> findUserByPhone(String phone);

    // Найти пользователя по имени (с частичным совпадением) и отсортировать по имени
    List<User> findUserByName(String name); // Method name consistent with impl

    // Найти пользователя по фамилии (с частичным совпадением) и отсортировать по фамилии
    List<User> findUserBySurname(String surname); // Method name consistent with impl

    // Найти пользователя по имени и фамилии
    List<User> findUserByNameAndSurname(String name, String surname);

    // Найти пользователей по региону
    List<User> findUserByRegion(Region userRegion); // Parameter type: Region object

    // Найти пользователей по роли
    List<User> findUserByRole(RolesUsers role); // Parameter type: RolesUsers object

    // Найти пользователей по сектору
    List<User> findUserBySector(Sector sector); // Parameter type: Sector object

    // Найти пользователя по ID
    Optional<User> findById(Long idUser);

    // Получить всех пользователей
    List<User> findAll();

    // --- Added methods for CRUD operations ---
    User save(User user);
    User update(Long idUser, User userDetails); // For updating a user
    void delete(Long idUser); // For deleting a user by ID
    // --- End of added methods ---

    // --- Added methods for new repository functions ---
    List<User> findUserByNickname(String nickname);
    List<User> findUserByDateOfBirth(LocalDate dateOfBirth); // Requires import java.time.LocalDate List<User> findUserByDateOfBirth(LocalDate dateOfBirth);
    // --- End of added methods ---

    Optional<User> findByNicknameExact(String nickname); // НОВОЕ НАЗВАНИЕ!
}