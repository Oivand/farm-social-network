package com.example.kolos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.HashSet; // Changed from Set to HashSet for better initialization, though Set is fine.
import java.util.Set; // Keep Set for the type

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString
@Entity
@Table(name = "groups") // Explicitly names the database table
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_group")
    private Long idGroup;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    // Связь с мастером группы (один пользователь - создатель/владелец)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching for performance
    @JoinColumn(name = "group_master_id", nullable = false, referencedColumnName = "id_user") // Foreign key column for the master user
    private User groupMaster; // Field to hold the master User object

    @Column(name = "path_group_picture")
    private String pathGroupPicture;

    @CreationTimestamp // Automatically sets the creation timestamp
    @Column(name = "created_at", nullable = false, updatable = false) // Not updatable after creation
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates the timestamp on entity modification
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_since_time") // Purpose depends on your logic (e.g., last activity time)
    private LocalDateTime lastSinceTime;

    // Связь с участниками группы (многие пользователи - участники)
    @ManyToMany(fetch = FetchType.LAZY) // Lazy fetching for performance
    @JoinTable(
            name = "group_members", // Name of the join table (intermediate table)
            joinColumns = @JoinColumn(name = "group_id"), // Column in joinTable referring to this group's ID
            inverseJoinColumns = @JoinColumn(name = "user_id") // Column in joinTable referring to the member user's ID
    )
    private Set<User> members; // Collection to hold the User objects who are members of this group

    // Default constructor needed by JPA
    public Groups() {
        this.members = new HashSet<>(); // Initialize the set to avoid NullPointerExceptions
    }

    // --- Custom Constructor (Optional, but useful for creating new instances) ---
    public Groups(String groupName, User groupMaster, String pathGroupPicture) {
        this.groupName = groupName;
        this.groupMaster = groupMaster;
        this.pathGroupPicture = pathGroupPicture;
        this.members = new HashSet<>(); // Initialize members set
        // createdAt and updatedAt are handled by @CreationTimestamp/@UpdateTimestamp
    }

    // --- Getter and Setter for groupMaster ---
    // Lombok's @Data already generates these, but explicit definition for clarity or custom logic:
    public User getGroupMaster() {
        return groupMaster;
    }

    public void setGroupMaster(User groupMaster) {
        this.groupMaster = groupMaster;
    }

    // --- Utility Methods for Members (Recommended for managing ManyToMany) ---
    public void addMember(User user) {
        if (this.members == null) {
            this.members = new HashSet<>();
        }
        if (user != null && !this.members.contains(user)) {
            this.members.add(user);
        }
    }

    public void removeMember(User user) {
        if (this.members != null && user != null) {
            this.members.remove(user);
        }
    }
}