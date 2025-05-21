package com.example.kolos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "groups")
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_group")
    private Long idGroup;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    // Связь с мастером группы (один пользователь - создатель)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_master", nullable = false)
    private User groupMaster;

    @Column(name = "path_group_picture")
    private String pathGroupPicture;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_since_time")
    private LocalDateTime lastSinceTime;

    // Связь с участниками группы (многие пользователи - участники)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_members", // Промежуточная таблица
            joinColumns = @JoinColumn(name = "group_id"), // ID группы
            inverseJoinColumns = @JoinColumn(name = "user_id") // ID участника
    )
    private Set<User> members; // Коллекция участников группы

    public Groups() {}
}
