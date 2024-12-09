package com.example.kolos.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="complaints")
public class Complaints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_complaint")
    private Long idComplaint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kind_complaint", nullable = false)
    private KindsComplaint kindcomplaint;

    @Column(name="description_complaint")
    private String descriptionComplaint;

    @Column(name="id_accused")
    private int idAccused;

    @Column(name="id_accuser")
    private int idAccuser;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Complaints(){}
}
