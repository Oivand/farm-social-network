package com.example.kolos.repository;
import com.example.kolos.model.Chats;
import com.example.kolos.model.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsRepository extends JpaRepository<Chats, Long> {
    // Найти все чаты, созданные конкретным пользователем
    List<Chats> findByFounderChat(User founderChat);

    // Найти все чаты, в которых участвует конкретный пользователь
    List<Chats> findByMembers(User member);
}

