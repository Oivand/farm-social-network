package com.example.kolos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.FetchType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "chats")
public class Chats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat")
    private Long idChat;

    // Связь с создателем чата (один пользователь - создатель)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "founder_chat", nullable = false)
    private User founderChat;

    // Связь с участниками чата (многие пользователи - участники)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chat_members", // Название промежуточной таблицы
            joinColumns = @JoinColumn(name = "chat_id"), // Связь с таблицей Chats
            inverseJoinColumns = @JoinColumn(name = "user_id") // Связь с таблицей User
    )
    private Set<User> members; // Коллекция участников чата

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Chats() {}
}
