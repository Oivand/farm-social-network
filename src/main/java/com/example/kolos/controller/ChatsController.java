package com.example.kolos.controller;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User; // На всякий случай, если потребуется User DTO
import com.example.kolos.service.ChatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI; // Для ResponseEntity.created()
import java.util.List;

@RestController
@RequestMapping("/chats") // Базовый путь для всех эндпоинтов, связанных с чатами
public class ChatsController {

    private final ChatsService chatsService;

    // Внедряем сервис ChatsService через конструктор
    public ChatsController(ChatsService chatsService) {
        this.chatsService = chatsService;
    }

    // --- CRUD-операции ---

    /**
     * Создает новый чат.
     * POST /chats
     * @param chat Объект чата из тела запроса. founderChat.idUser должен быть заполнен.
     * @return ResponseEntity с созданным чатом и статусом 201 (Created), или 400 (Bad Request) в случае ошибки валидации.
     */
    @PostMapping
    public ResponseEntity<Chats> createChat(@RequestBody Chats chat) {
        try {
            Chats savedChat = chatsService.save(chat);
            // Возвращаем 201 Created со ссылкой на новый ресурс
            return ResponseEntity.created(URI.create("/chats/" + savedChat.getIdChat())).body(savedChat);
        } catch (IllegalArgumentException e) {
            // Возвращаем 400 Bad Request для ошибок валидации из сервиса
            return ResponseEntity.badRequest().body(null); // Можно добавить сообщение об ошибке: .body(e.getMessage())
        }
    }

    /**
     * Обновляет существующий чат.
     * PUT /chats/{id}
     * @param id ID чата для обновления из пути запроса.
     * @param chatDetails Объект чата с обновленными данными из тела запроса.
     * @return ResponseEntity с обновленным чатом и статусом 200 (OK), 400 (Bad Request) или 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Chats> updateChat(@PathVariable("id") Long id, @RequestBody Chats chatDetails) {
        try {
            Chats updatedChat = chatsService.update(id, chatDetails);
            return ResponseEntity.ok(updatedChat);
        } catch (IllegalArgumentException e) {
            // Простая проверка на сообщение "not found" для возврата 404
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(null); // Ошибки валидации или другие неверные данные
        }
    }

    /**
     * Удаляет чат по его ID.
     * DELETE /chats/{id}
     * @param id ID чата для удаления из пути запроса.
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении, или 404 (Not Found), если чат не существует.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable("id") Long id) {
        try {
            chatsService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content для успешного удаления
        } catch (IllegalArgumentException e) {
            // Если чат не найден
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Возвращает чат по его ID.
     * GET /chats/{id}
     * @param id ID чата из пути запроса.
     * @return ResponseEntity с объектом чата и статусом 200 (OK), или 404 (Not Found), если чат не существует.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Chats> getChatById(@PathVariable("id") Long id) {
        return chatsService.findById(id)
                .map(ResponseEntity::ok) // Если найден, возвращаем 200 OK с чатом
                .orElse(ResponseEntity.notFound().build()); // Если не найден, возвращаем 404 Not Found
    }

    /**
     * Возвращает все чаты, отсортированные по дате создания.
     * GET /chats
     * @return ResponseEntity со списком чатов и статусом 200 (OK), или 204 (No Content), если чаты не найдены.
     */
    @GetMapping
    public ResponseEntity<List<Chats>> getAllChats() {
        List<Chats> chats = chatsService.findAllChatsOrderedByDate();
        return chats.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(chats);
    }

    // --- Операции поиска ---

    /**
     * Находит чаты, созданные конкретным пользователем, по его ID.
     * GET /chats/search/by-founder-id?founderId=1
     * @param founderId ID пользователя-основателя.
     * @return ResponseEntity со списком чатов, или 204 (No Content).
     */
    @GetMapping("/search/by-founder-id")
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
     * GET /chats/search/by-member-id?userId=1
     * @param userId ID пользователя, который является участником.
     * @return ResponseEntity со списком чатов, или 204 (No Content).
     */
    @GetMapping("/search/by-member-id")
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
     * POST /chats/{chatId}/members/{userId}
     * @param chatId ID чата.
     * @param userId ID пользователя для добавления.
     * @return ResponseEntity с обновленным чатом и статусом 200 (OK), или 400/404 в случае ошибки.
     */
    @PostMapping("/{chatId}/members/{userId}")
    public ResponseEntity<Chats> addMemberToChat(@PathVariable("chatId") Long chatId, @PathVariable("userId") Long userId) {
        try {
            Chats updatedChat = chatsService.addUserToChat(chatId, userId);
            return ResponseEntity.ok(updatedChat);
        } catch (IllegalArgumentException e) {
            // Здесь может быть более детальная обработка для 404 vs 400
            // Например: if (e.getMessage().contains("not found")) return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Удаляет пользователя из чата.
     * DELETE /chats/{chatId}/members/{userId}
     * @param chatId ID чата.
     * @param userId ID пользователя для удаления.
     * @return ResponseEntity с обновленным чатом и статусом 200 (OK) при успешном удалении, или 400/404 в случае ошибки.
     */
    @DeleteMapping("/{chatId}/members/{userId}")
    public ResponseEntity<Chats> removeMemberFromChat(@PathVariable("chatId") Long chatId, @PathVariable("userId") Long userId) {
        try {
            Chats updatedChat = chatsService.removeUserFromChat(chatId, userId);
            return ResponseEntity.ok(updatedChat); // Возвращаем обновленный чат
        } catch (IllegalArgumentException e) {
            // Здесь также может быть более детальная обработка для 404 vs 400
            return ResponseEntity.badRequest().body(null);
        }
    }
}