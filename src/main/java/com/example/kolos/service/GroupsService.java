package com.example.kolos.service;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface GroupsService {

    // --- CRUD Operations ---
    Groups save(Groups group);
    Groups update(Long groupId, Groups updatedGroup);
    void deleteById(Long groupId);


    // --- Search Operations ---
    Optional<Groups> findGroupByExactName(String groupName);
    List<Groups> findGroupsByNameContaining(String groupName);
    List<Groups> findGroupsByMasterId(Long groupMasterId);
    List<Groups> findGroupsByMaster(User groupMaster);
    Optional<Groups> findById(Long groupId);
    List<Groups> findGroupsByMember(User user);
    List<Groups> findGroupsByMemberId(Long userId);
    List<Groups> findAllGroupsOrderedByDate();
    List<Groups> findAll();

    // --- Specific Methods for Managing Members (ДОБАВЛЕНО/ИСПРАВЛЕНО ЗДЕСЬ) ---

    /**
     * Добавляет пользователя в группу.
     * @param groupId ID группы.
     * @param userId ID пользователя для добавления.
     * @return Обновленный объект группы.
     */
    Groups addMemberToGroup(Long groupId, Long userId);

    /**
     * Удаляет пользователя из группы.
     * @param groupId ID группы.
     * @param userId ID пользователя для удаления.
     * @return Обновленный объект группы.
     */
    Groups removeMemberFromGroup(Long groupId, Long userId); // Также добавлен этот метод, чтобы не было ошибок в будущем
}