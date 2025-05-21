package com.example.kolos.service;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface GroupsService {
    // Найти группу по её названию
    List<Groups> findGroupsByNameContaining(String groupName);

    // Найти все группы, созданные конкретным пользователем
    List<Groups> findGroupsByCreator(User groupMaster);

    // Найти группу по ID
    Optional<Groups> findById(Long groupId);

    // Получить все группы
    List<Groups> findAll();
}
