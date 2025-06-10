package com.example.kolos.controller;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User; // Import User model
import com.example.kolos.service.GroupsService;
import com.example.kolos.service.UserService; // Import UserService to find current user

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import for @PreAuthorize
import org.springframework.security.core.Authentication; // Import for Authentication object
import org.springframework.security.core.context.SecurityContextHolder; // Import for SecurityContextHolder
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Optional; // Import Optional

@RestController
@RequestMapping("/groups")
public class GroupsController {

    private final GroupsService groupsService;
    private final UserService userService; // Inject UserService

    public GroupsController(GroupsService groupsService, UserService userService) {
        this.groupsService = groupsService;
        this.userService = userService;
    }

    // --- CRUD-операции ---

    /**
     * Создает новую группу.
     * Только аутентифицированные пользователи (любой роли) могут создавать группы.
     * Создатель группы автоматически становится её мастером.
     * POST /groups
     * @param group Объект группы из тела запроса.
     * @return ResponseEntity с созданной группой и статусом 201 (Created), или 400 (Bad Request).
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Только аутентифицированные пользователи могут создавать группы
    public ResponseEntity<Groups> createGroup(@RequestBody Groups group) {
        try {
            // Получаем никнейм текущего аутентифицированного пользователя
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            // Находим объект User для текущего пользователя
            User currentUser = userService.findByNicknameExact(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Аутентифицированный пользователь не найден."));

            // Устанавливаем текущего пользователя как мастера создаваемой группы
            group.setGroupMaster(currentUser); // Предполагается, что у Groups есть setMaster(User user)

            Groups savedGroup = groupsService.save(group);
            return ResponseEntity.created(URI.create("/groups/" + savedGroup.getIdGroup())).body(savedGroup);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Можно добавить сообщение об ошибке: .body(e.getMessage())
        }
    }

    /**
     * Возвращает все группы, отсортированные по дате создания.
     * Доступно всем (включая неаутентифицированных пользователей).
     * GET /groups
     * @return ResponseEntity со списком групп и статусом 200 (OK), или 204 (No Content), если группы не найдены.
     */
    @GetMapping
    @PreAuthorize("permitAll()") // Все пользователи (аутентифицированные и нет) могут просматривать
    public ResponseEntity<List<Groups>> getAllGroups() {
        List<Groups> groups = groupsService.findAllGroupsOrderedByDate();
        return groups.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(groups);
    }

    /**
     * Возвращает группу по её ID.
     * Доступно всем (включая неаутентифицированных пользователей).
     * GET /groups/{id}
     * @param id ID группы из пути запроса.
     * @return ResponseEntity с объектом группы и статусом 200 (OK), или 404 (Not Found), если группа не существует.
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()") // Все пользователи (аутентифицированные и нет) могут просматривать
    public ResponseEntity<Groups> getGroupById(@PathVariable("id") Long id) {
        return groupsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Обновляет существующую группу.
     * Только ADMIN и MODERATOR могут обновлять любую группу.
     * Обычный USER может обновлять только те группы, где он является мастером.
     * PUT /groups/{id}
     * @param id ID группы для обновления из пути запроса.
     * @param groupDetails Объект группы с обновленными данными из тела запроса.
     * @return ResponseEntity с обновленной группой и статусом 200 (OK), 400 (Bad Request) или 404 (Not Found), 403 (Forbidden).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @groupOwnershipService.isMaster(#id, authentication.name))")
    public ResponseEntity<Groups> updateGroup(@PathVariable("id") Long id, @RequestBody Groups groupDetails) {
        try {
            // При обновлении убедитесь, что 'master' не изменяется случайно, если это не разрешено.
            // Логика проверки на владельца уже в @PreAuthorize.
            Groups updatedGroup = groupsService.update(id, groupDetails);
            return ResponseEntity.ok(updatedGroup);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Удаляет группу по её ID.
     * Только ADMIN и MODERATOR могут удалять любую группу.
     * Обычный USER может удалять только те группы, где он является мастером.
     * DELETE /groups/{id}
     * @param id ID группы для удаления из пути запроса.
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении, или 404 (Not Found), 403 (Forbidden).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @groupOwnershipService.isMaster(#id, authentication.name))")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        try {
            groupsService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Операции поиска ---

    /**
     * Находит группу по точному названию.
     * Доступно всем.
     * GET /groups/search/by-exact-name?name=MyGroup
     * @param name Точное название группы.
     * @return ResponseEntity с группой и статусом 200 (OK), или 404 (Not Found).
     */
    @GetMapping("/search/by-exact-name")
    @PreAuthorize("permitAll()")
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
     * Доступно всем.
     * GET /groups/search/by-name-containing?name=partOfName
     * @param name Часть названия группы для поиска.
     * @return ResponseEntity со списком групп, или 204 (No Content), если не найдено.
     */
    @GetMapping("/search/by-name-containing")
    @PreAuthorize("permitAll()")
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
     * Доступно всем.
     * GET /groups/search/by-master-id?masterId=1
     * @param masterId ID пользователя-мастера группы.
     * @return ResponseEntity со списком групп, или 204 (No Content).
     */
    @GetMapping("/search/by-master-id")
    @PreAuthorize("permitAll()")
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
     * Пользователь может запрашивать свои собственные группы по членству.
     * ADMIN/MODERATOR могут запрашивать группы любого пользователя.
     * GET /groups/search/by-member-id?userId=1
     * @param userId ID пользователя, который является участником.
     * @return ResponseEntity со списком групп, или 204 (No Content).
     */
    @GetMapping("/search/by-member-id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and #userId == @userService.findByNicknameExact(authentication.name).map(User::getIdUser).orElse(0L))")
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
     * Только ADMIN, MODERATOR или мастер группы может добавлять участников.
     * POST /groups/{groupId}/members/{userId}
     * @param groupId ID группы.
     * @param userId ID пользователя для добавления.
     * @return ResponseEntity с обновленной группой и статусом 200 (OK), или 400/404/403 в случае ошибки.
     */
    @PostMapping("/{groupId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @groupOwnershipService.isMaster(#groupId, authentication.name))")
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
     * Только ADMIN, MODERATOR или мастер группы может удалять участников.
     * DELETE /groups/{groupId}/members/{userId}
     * @param groupId ID группы.
     * @param userId ID пользователя для удаления.
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении, или 400/404/403 в случае ошибки.
     */
    @DeleteMapping("/{groupId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @groupOwnershipService.isMaster(#groupId, authentication.name))")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {
        try {
            groupsService.removeMemberFromGroup(groupId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}