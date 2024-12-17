package com.example.kolos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="complaints")
public class Complaints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_complaint")
    private Long idComplaint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kind_complaint", nullable = false)
    private KindsComplaint kindComplaint; // С латинской "C"

    @Column(name="description_complaint")
    private String descriptionComplaint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_accused", nullable = false)
    private User idAccused;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_accuser", nullable = false)
    private User idAccuser;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Complaints(){}
}

