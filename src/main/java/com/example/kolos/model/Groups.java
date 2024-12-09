package com.example.kolos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;

@Entity
@Table(name="groups")
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_group")
    private Long idGroup;

    @Column(name="group_name")
    private String groupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_master", nullable = false)
    private User groupMaster;

    @Column(name="path_group_picture")
    private String pathGroupPicture;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; 

    @Column(name = "last_since_time")
    private LocalDateTime lastSinceTime;

    @JoinColumn(name = "members", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private User members;

    public Groups(){}
    
}
