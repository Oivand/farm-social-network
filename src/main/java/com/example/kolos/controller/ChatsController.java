package com.example.kolos.controller;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User; // Import User model
import com.example.kolos.service.ChatsService;
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
@RequestMapping("/chats")
public class ChatsController {

    private final ChatsService chatsService;
    private final UserService userService; // Inject UserService

    public ChatsController(ChatsService chatsService, UserService userService) {
        this.chatsService = chatsService;
        this.userService = userService;
    }

    // --- CRUD-операции ---

    /**
     * Создает новый чат.
     * Только аутентифицированные пользователи (любой роли) могут создавать чаты.
     * Создатель чата автоматически становится его основателем.
     * POST /chats
     * @param chat Объект чата из тела запроса.
     * @return ResponseEntity с созданным чатом и статусом 201 (Created), или 400 (Bad Request).
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Только аутентифицированные пользователи могут создавать чаты
    public ResponseEntity<Chats> createChat(@RequestBody Chats chat) {
        try {
            // Получаем никнейм текущего аутентифицированного пользователя
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            // Находим объект User для текущего пользователя
            User currentUser = userService.findByNicknameExact(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Аутентифицированный пользователь не найден."));

            // Устанавливаем текущего пользователя как основателя создаваемого чата
            chat.setFounderChat(currentUser); // Предполагается, что у Chats есть setFounderChat(User user)

            Chats savedChat = chatsService.save(chat);
            return ResponseEntity.created(URI.create("/chats/" + savedChat.getIdChat())).body(savedChat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Можно добавить сообщение об ошибке: .body(e.getMessage())
        }
    }

    /**
     * Обновляет существующий чат.
     * Только ADMIN и MODERATOR могут обновлять любой чат.
     * Обычный USER может обновлять только те чаты, где он является основателем.
     * PUT /chats/{id}
     * @param id ID чата для обновления из пути запроса.
     * @param chatDetails Объект чата с обновленными данными из тела запроса.
     * @return ResponseEntity с обновленным чатом и статусом 200 (OK), 400 (Bad Request) или 404 (Not Found), 403 (Forbidden).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @chatOwnershipService.isFounder(#id, authentication.name))")
    public ResponseEntity<Chats> updateChat(@PathVariable("id") Long id, @RequestBody Chats chatDetails) {
        try {
            // При обновлении убедитесь, что 'founderChat' не изменяется случайно, если это не разрешено.
            // Логика проверки на владельца уже в @PreAuthorize.
            Chats updatedChat = chatsService.update(id, chatDetails);
            return ResponseEntity.ok(updatedChat);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Удаляет чат по его ID.
     * Только ADMIN и MODERATOR могут удалять любой чат.
     * Обычный USER может удалять только те чаты, где он является основателем.
     * DELETE /chats/{id}
     * @param id ID чата для удаления из пути запроса.
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении, или 404 (Not Found), 403 (Forbidden).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @chatOwnershipService.isFounder(#id, authentication.name))")
    public ResponseEntity<Void> deleteChat(@PathVariable("id") Long id) {
        try {
            chatsService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Возвращает чат по его ID.
     * Доступно всем.
     * GET /chats/{id}
     * @param id ID чата из пути запроса.
     * @return ResponseEntity с объектом чата и статусом 200 (OK), или 404 (Not Found).
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()") // Все пользователи (аутентифицированные и нет) могут просматривать
    public ResponseEntity<Chats> getChatById(@PathVariable("id") Long id) {
        return chatsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Возвращает все чаты, отсортированные по дате создания.
     * Доступно всем.
     * GET /chats
     * @return ResponseEntity со списком чатов и статусом 200 (OK), или 204 (No Content), если чаты не найдены.
     */
    @GetMapping
    @PreAuthorize("permitAll()") // Все пользователи (аутентифицированные и нет) могут просматривать
    public ResponseEntity<List<Chats>> getAllChats() {
        List<Chats> chats = chatsService.findAllChatsOrderedByDate();
        return chats.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(chats);
    }

    // --- Операции поиска ---

    /**
     * Находит чаты, созданные конкретным пользователем, по его ID.
     * Доступно всем.
     * GET /chats/search/by-founder-id?founderId=1
     * @param founderId ID пользователя-основателя.
     * @return ResponseEntity со списком чатов, или 204 (No Content).
     */
    @GetMapping("/search/by-founder-id")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Chats>> getChatsByFounderId(@RequestParam("founderId") Long founderId) {
        try {
            List<Chats> chats = chatsService.findChatsByFounderId(founderId);
            return chats.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(chats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Находит чаты, в которых состоит пользователь по его ID.
     * Пользователь может запрашивать свои собственные чаты по членству.
     * ADMIN/MODERATOR могут запрашивать чаты любого пользователя.
     * GET /chats/search/by-member-id?userId=1
     * @param userId ID пользователя, который является участником.
     * @return ResponseEntity со списком чатов, или 204 (No Content).
     */
    @GetMapping("/search/by-member-id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and #userId == @userService.findByNicknameExact(authentication.name).map(User::getIdUser).orElse(0L))")
    public ResponseEntity<List<Chats>> getChatsByMemberId(@RequestParam("userId") Long userId) {
        try {
            List<Chats> chats = chatsService.findChatsByMemberId(userId);
            return chats.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(chats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Операции управления участниками ---

    /**
     * Добавляет пользователя в чат.
     * Только ADMIN, MODERATOR или основатель чата может добавлять участников.
     * POST /chats/{chatId}/members/{userId}
     * @param chatId ID чата.
     * @param userId ID пользователя для добавления.
     * @return ResponseEntity с обновленным чатом и статусом 200 (OK), или 400/404/403 в случае ошибки.
     */
    @PostMapping("/{chatId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @chatOwnershipService.isFounder(#chatId, authentication.name))")
    public ResponseEntity<Chats> addMemberToChat(@PathVariable("chatId") Long chatId, @PathVariable("userId") Long userId) {
        try {
            Chats updatedChat = chatsService.addUserToChat(chatId, userId);
            return ResponseEntity.ok(updatedChat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Удаляет пользователя из чата.
     * Только ADMIN, MODERATOR или основатель чата может удалять участников.
     * DELETE /chats/{chatId}/members/{userId}
     * @param chatId ID чата.
     * @param userId ID пользователя для удаления.
     * @return ResponseEntity с обновленным чатом и статусом 200 (OK) при успешном удалении, или 400/404/403 в случае ошибки.
     */
    @DeleteMapping("/{chatId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or " +
            "(hasRole('USER') and @chatOwnershipService.isFounder(#chatId, authentication.name))")
    public ResponseEntity<Chats> removeMemberFromChat(@PathVariable("chatId") Long chatId, @PathVariable("userId") Long userId) {
        try {
            Chats updatedChat = chatsService.removeUserFromChat(chatId, userId);
            return ResponseEntity.ok(updatedChat); // Возвращаем обновленный чат
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}