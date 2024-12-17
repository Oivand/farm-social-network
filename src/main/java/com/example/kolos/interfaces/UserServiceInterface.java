package com.example.kolos.interfaces;

import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    User createUser(User user);                         // Создание нового пользователя

    Optional<User> getUserById(Long id);                // Получение пользователя по ID

    Optional<User> getUserByEmail(String email);        // Получение пользователя по email

    List<User> getAllUsers();                           // Получение списка всех пользователей

    List<User> getUsersByName(String name);             // Получение пользователей по имени

    User updateUser(Long id, User user);                // Обновление информации о пользователе

    void deleteUser(Long id);                           // Удаление пользователя

    boolean existsByEmail(String email);                // Проверка существования пользователя по email

    boolean authenticateUser(String email, String password); // Аутентификация пользователя

    void changeUserRole(Long userId, String newRole);   // Изменение роли пользователя

    void blockUser(Long userId);                        // Блокировка пользователя

    void unblockUser(Long userId);                      // Разблокировка пользователя

    List<User> getUserBySurname(String surname);

    List<User> getUserByName (String name);

    List<User> getByNameAndSurname(String name, String surname);



}
