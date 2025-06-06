package com.example.kolos.service;

import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Найти пользователя по email
    Optional<User> findUserByEmail(String email);

    // Найти пользователя по номеру телефона
    Optional<User> findUserByPhone(String phone);

    // Найти пользователя по имени (с частичным совпадением) и отсортировать по имени
    List<User> findUserByName(String name);

    // Найти пользователя по фамилии (с частичным совпадением) и отсортировать по фамилии
    List<User> findUserBySurname(String surname);

    // Найти пользователя по имени и фамилии
    List<User> findUserByNameAndSurname(String name, String surname);

    // Найти пользователей по региону
    List<User> findUserByRegion(Region userRegion);

    // Найти пользователей по роли
    List<User> findUserByRole(RolesUsers role);

    // Найти пользователей по сектору
    List<User> findUserBySector(Sector sector);

    // Найти пользователя по ID
    Optional<User> findById(Long idUser);

    // Получить всех пользователей
    List<User> findAll();
}
