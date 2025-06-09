package com.example.kolos.repository;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User; // Important for founderChat and members relationships
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatsRepository extends JpaRepository<Chats, Long> {

    // Find chats created by a specific user (User object)
    List<Chats> findByFounderChatOrderByCreatedAtDesc(User founderChat);

    // Find chats created by a specific user (by User ID)
    List<Chats> findByFounderChat_IdUserOrderByCreatedAtDesc(Long founderChatId);

    // Find chats where a specific user is a member
    // This leverages the ManyToMany relationship
    List<Chats> findByMembersContainingOrderByCreatedAtDesc(User user);

    // Find chats where a specific user (by ID) is a member
    List<Chats> findByMembers_IdUserOrderByCreatedAtDesc(Long userId);

    // Find all chats ordered by creation date (descending)
    List<Chats> findAllByOrderByCreatedAtDesc();

    // You might also want to find chats by the number of members, etc., but these cover the basics.
}