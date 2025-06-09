package com.example.kolos.service.impl;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User;
import com.example.kolos.repository.ChatsRepository;
import com.example.kolos.repository.UserRepository; // Import UserRepository to validate User objects
import com.example.kolos.service.ChatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For transactional methods

import java.util.List;
import java.util.Optional;

@Service
public class ChatsServiceImpl implements ChatsService {

    private final ChatsRepository chatsRepository;
    private final UserRepository userRepository; // Inject UserRepository

    public ChatsServiceImpl(ChatsRepository chatsRepository, UserRepository userRepository) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
    }

    // --- CRUD Operations ---

    @Override
    @Transactional
    public Chats save(Chats chat) {
        if (chat == null) {
            throw new IllegalArgumentException("Chat object cannot be null.");
        }
        if (chat.getFounderChat() == null || chat.getFounderChat().getIdUser() == null) {
            throw new IllegalArgumentException("Chat must have a valid founder (user ID).");
        }

        // Validate and set the Chat Founder (User must exist)
        User founder = userRepository.findById(chat.getFounderChat().getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Chat founder with ID " + chat.getFounderChat().getIdUser() + " not found."));
        chat.setFounderChat(founder);

        // If initial members are provided during save, ensure they exist
        if (chat.getMembers() != null && !chat.getMembers().isEmpty()) {
            chat.getMembers().forEach(member -> {
                userRepository.findById(member.getIdUser())
                        .orElseThrow(() -> new IllegalArgumentException("Member with ID " + member.getIdUser() + " not found."));
            });
        }

        return chatsRepository.save(chat);
    }

    @Override
    @Transactional
    public Chats update(Long chatId, Chats updatedChat) {
        if (chatId == null) {
            throw new IllegalArgumentException("Chat ID for update cannot be null.");
        }
        if (updatedChat == null) {
            throw new IllegalArgumentException("Updated chat object cannot be null.");
        }

        Chats existingChat = chatsRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat with ID " + chatId + " not found for update."));

        // Update founder if provided (and validated)
        if (updatedChat.getFounderChat() != null && updatedChat.getFounderChat().getIdUser() != null) {
            User newFounder = userRepository.findById(updatedChat.getFounderChat().getIdUser())
                    .orElseThrow(() -> new IllegalArgumentException("New chat founder with ID " + updatedChat.getFounderChat().getIdUser() + " not found."));
            existingChat.setFounderChat(newFounder);
        } else if (updatedChat.getFounderChat() != null) {
            throw new IllegalArgumentException("Chat founder ID cannot be null if founder object is provided for update.");
        }

        // Note: Members are typically managed via separate add/remove methods, not directly via a full chat update.
        // If you send members in updatedChat, they will overwrite the existing set.
        // It's generally safer to manage ManyToMany relationships with dedicated methods.

        return chatsRepository.save(existingChat);
    }

    @Override
    @Transactional
    public void deleteById(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("Chat ID for deletion cannot be null.");
        }
        if (!chatsRepository.existsById(chatId)) {
            throw new IllegalArgumentException("Chat with ID " + chatId + " not found for deletion.");
        }
        chatsRepository.deleteById(chatId);
    }

    // --- Search Operations ---

    @Override
    public List<Chats> findChatsByFounder(User founderChat) {
        if (founderChat == null || founderChat.getIdUser() == null) {
            throw new IllegalArgumentException("Founder User object or its ID cannot be null.");
        }
        // Ensure the User object is a managed entity or exists in the DB
        User existingFounder = userRepository.findById(founderChat.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Founder with ID " + founderChat.getIdUser() + " not found."));

        return chatsRepository.findByFounderChatOrderByCreatedAtDesc(existingFounder);
    }

    @Override
    public List<Chats> findChatsByFounderId(Long founderChatId) {
        if (founderChatId == null) {
            throw new IllegalArgumentException("Founder User ID cannot be null.");
        }
        // It's good practice to ensure the user actually exists before searching for chats they founded
        userRepository.findById(founderChatId)
                .orElseThrow(() -> new IllegalArgumentException("Founder with ID " + founderChatId + " not found to search for chats."));

        return chatsRepository.findByFounderChat_IdUserOrderByCreatedAtDesc(founderChatId);
    }

    @Override
    public List<Chats> findChatsByMember(User member) {
        if (member == null || member.getIdUser() == null) {
            throw new IllegalArgumentException("Member User object or its ID cannot be null.");
        }
        // Ensure the User object is a managed entity or exists in the DB
        User existingMember = userRepository.findById(member.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Member with ID " + member.getIdUser() + " not found."));

        return chatsRepository.findByMembersContainingOrderByCreatedAtDesc(existingMember);
    }

    @Override
    public List<Chats> findChatsByMemberId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Member User ID cannot be null.");
        }
        // Ensure the user exists before searching for chats they are a member of
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found to search for chat membership."));

        return chatsRepository.findByMembers_IdUserOrderByCreatedAtDesc(userId);
    }

    @Override
    public Optional<Chats> findById(Long idChat) {
        if (idChat == null) {
            throw new IllegalArgumentException("Chat ID cannot be null.");
        }
        return chatsRepository.findById(idChat);
    }

    @Override
    public List<Chats> findAllChatsOrderedByDate() {
        return chatsRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Chats> findAll() {
        // This calls the default JpaRepository.findAll(), no specific order guaranteed.
        // Your previous logic threw an error if empty, which is unusual for findAll().
        // Returning an empty list is generally preferred if no entities are found.
        return chatsRepository.findAll();
    }

    // --- Methods for Managing Members ---

    @Override
    @Transactional
    public Chats addUserToChat(Long chatId, Long userId) {
        Chats chat = chatsRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat with ID " + chatId + " not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        if (chat.getMembers().contains(user)) {
            throw new IllegalArgumentException("User with ID " + userId + " is already a member of chat " + chatId + ".");
        }

        chat.getMembers().add(user);
        return chatsRepository.save(chat); // Save to update the ManyToMany relationship
    }

    @Override
    @Transactional
    public Chats removeUserFromChat(Long chatId, Long userId) {
        Chats chat = chatsRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat with ID " + chatId + " not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        if (!chat.getMembers().contains(user)) {
            throw new IllegalArgumentException("User with ID " + userId + " is not a member of chat " + chatId + ".");
        }

        chat.getMembers().remove(user);
        return chatsRepository.save(chat); // Save to update the ManyToMany relationship
    }
}