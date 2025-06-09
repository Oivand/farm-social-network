package com.example.kolos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name="publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_publication")
    private Long idPublication;

    @ManyToOne(fetch = FetchType.LAZY) // Consider adding optional = false if author is always required
    @JoinColumn(name="id_author", nullable = false) // Added nullable = false assuming author is mandatory
    private User author;

    @Column(name="description", columnDefinition = "TEXT") // Use TEXT for potentially long descriptions
    private String description;

    @Column(name="title", nullable = false) // Title is usually mandatory
    private String title;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false) // created_at should not be updatable
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false) // updated_at should always be set
    private LocalDateTime updatedAt;

    @Column(name="path_media") // This could be a URL or a file path. Consider validation later.
    private String pathMedia;

    public Publication(){}
}