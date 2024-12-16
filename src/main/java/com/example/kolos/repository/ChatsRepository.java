package com.example.kolos.repository;
import com.example.kolos.model.Chats;
import com.example.kolos.service.ChatsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsRepository extends JpaRepository<Chats, Long> {

}
