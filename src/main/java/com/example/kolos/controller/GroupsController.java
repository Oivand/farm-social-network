package com.example.kolos.controller;

import com.example.kolos.model.Groups;
import com.example.kolos.service.GroupsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI; // Для ResponseEntity.created()
import java.util.List;

@RestController
@RequestMapping("/groups") // Базовый путь для всех эндпоинтов, связанных с группами
public class GroupsController {

    private final GroupsService groupsService;

    // Внедряем сервис GroupsService через конструктор
    public GroupsController(GroupsService groupsService) {
        this.groupsService = groupsService;
    }

    // --- CRUD-операции ---

    /**
     * Создает новую группу.
     * POST /groups
     * @param group Объект группы из тела запроса.
     * @return ResponseEntity с созданной группой и статусом 201 (Created), или 400 (Bad Request) в случае ошибки валидации.
     */
    @PostMapping
    public ResponseEntity<Groups> createGroup(@RequestBody Groups group) {
        try {
            Groups savedGroup = groupsService.save(group);
            // Возвращаем 201 Created со ссылкой на новый ресурс
            return ResponseEntity.created(URI.create("/groups/" + savedGroup.getIdGroup())).body(savedGroup);
        } catch (IllegalArgumentException e) {
            // Возвращаем 400 Bad Request для ошибок валидации из сервиса
            return ResponseEntity.badRequest().body(null); // Можно добавить сообщение об ошибке: .body(e.getMessage())
        }
    }

    /**
     * Возвращает все группы, отсортированные по дате создания.
     * GET /groups
     * @return ResponseEntity со списком групп и статусом 200 (OK), или 204 (No Content), если группы не найдены.
     */
    @GetMapping
    public ResponseEntity<List<Groups>> getAllGroups() {
        List<Groups> groups = groupsService.findAllGroupsOrderedByDate();
        return groups.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(groups);
    }

    /**
     * Возвращает группу по её ID.
     * GET /groups/{id}
     * @param id ID группы из пути запроса.
     * @return ResponseEntity с объектом группы и статусом 200 (OK), или 404 (Not Found), если группа не существует.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Groups> getGroupById(@PathVariable("id") Long id) {
        return groupsService.findById(id)
                .map(ResponseEntity::ok) // Если найдена, возвращаем 200 OK с группой
                .orElse(ResponseEntity.notFound().build()); // Если не найдена, возвращаем 404 Not Found
    }

    /**
     * Обновляет существующую группу.
     * PUT /groups/{id}
     * @param id ID группы для обновления из пути запроса.
     * @param groupDetails Объект группы с обновленными данными из тела запроса.
     * @return ResponseEntity с обновленной группой и статусом 200 (OK), 400 (Bad Request) или 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Groups> updateGroup(@PathVariable("id") Long id, @RequestBody Groups groupDetails) {
        try {
            Groups updatedGroup = groupsService.update(id, groupDetails);
            return ResponseEntity.ok(updatedGroup);
        } catch (IllegalArgumentException e) {
            // Простая проверка на сообщение "not found" для возврата 404
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(null); // Ошибки валидации или другие неверные данные
        }
    }

    /**
     * Удаляет группу по её ID.
     * DELETE /groups/{id}
     * @param id ID группы для удаления из пути запроса.
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении, или 404 (Not Found), если группа не существует.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        try {
            groupsService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content для успешного удаления
        } catch (IllegalArgumentException e) {
            // Если группа не найдена
            return ResponseEntity.notFound().build();
        }
    }

    // --- Операции поиска ---

    /**
     * Находит группу по точному названию.
     * GET /groups/search/by-exact-name?name=MyGroup
     * @param name Точное название группы.
     * @return ResponseEntity с группой и статусом 200 (OK), или 404 (Not Found).
     */
    @GetMapping("/search/by-exact-name")
    public ResponseEntity<Groups> getGroupByExactName(@RequestParam("name") String name) {
        try {
            return groupsService.findGroupByExactName(name)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Находит группы, названия которых содержат указанную подстроку.
     * GET /groups/search/by-name-containing?name=partOfName
     * @param name Часть названия группы для поиска.
     * @return ResponseEntity со списком групп, или 204 (No Content), если не найдено.
     */
    @GetMapping("/search/by-name-containing")
    public ResponseEntity<List<Groups>> getGroupsByNameContaining(@RequestParam("name") String name) {
        try {
            List<Groups> groups = groupsService.findGroupsByNameContaining(name);
            return groups.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(groups);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Находит все группы, созданные конкретным мастером, по ID пользователя.
     * GET /groups/search/by-master-id?masterId=1
     * @param masterId ID пользователя-мастера группы.
     * @return ResponseEntity со списком групп, или 204 (No Content).
     */
    @GetMapping("/search/by-master-id")
    public ResponseEntity<List<Groups>> getGroupsByMasterId(@RequestParam("masterId") Long masterId) {
        try {
            List<Groups> groups = groupsService.findGroupsByMasterId(masterId);
            return groups.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(groups);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Находит все группы, в которых состоит пользователь по его ID.
     * GET /groups/search/by-member-id?userId=1
     * @param userId ID пользователя, который является участником.
     * @return ResponseEntity со списком групп, или 204 (No Content).
     */
    @GetMapping("/search/by-member-id")
    public ResponseEntity<List<Groups>> getGroupsByMemberId(@RequestParam("userId") Long userId) {
        try {
            List<Groups> groups = groupsService.findGroupsByMemberId(userId);
            return groups.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(groups);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Операции управления участниками ---

    /**
     * Добавляет пользователя в группу.
     * POST /groups/{groupId}/members/{userId}
     * @param groupId ID группы.
     * @param userId ID пользователя для добавления.
     * @return ResponseEntity с обновленной группой и статусом 200 (OK), или 400/404 в случае ошибки.
     */
    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Groups> addMemberToGroup(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {
        try {
            Groups updatedGroup = groupsService.addMemberToGroup(groupId, userId);
            return ResponseEntity.ok(updatedGroup);
        } catch (IllegalArgumentException e) {
            // Здесь может быть более детальная обработка для 404 vs 400
            // Например: if (e.getMessage().contains("not found")) return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Удаляет пользователя из группы.
     * DELETE /groups/{groupId}/members/{userId}
     * @param groupId ID группы.
     * @param userId ID пользователя для удаления.
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении, или 400/404 в случае ошибки.
     */
    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {
        try {
            groupsService.removeMemberFromGroup(groupId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Здесь также может быть более детальная обработка для 404 vs 400
            return ResponseEntity.badRequest().build();
        }
    }
}