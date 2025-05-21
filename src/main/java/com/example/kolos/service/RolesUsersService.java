package com.example.kolos.service;

import com.example.kolos.model.RolesUsers;

import java.util.List;
import java.util.Optional;

public interface RolesUsersService {

    // Найти роли по имени с частичным совпадением
    List<RolesUsers> searchRolesByName(String userRole);

    // Получить все роли пользователей
    List<RolesUsers> findAll();

    // Найти роль по ID
    Optional<RolesUsers> findById(Long idRoleUser);
}
