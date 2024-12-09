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

@Entity
@Table(name="publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_publication")
    private Long idPublication;

    @JoinColumn(name="id_author")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(name="description")
    private String description;

    @Column(name="title")
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt; 

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 

    @Column(name="path_media")
    private String pathMedia;

    public Publication(){}
    
}
