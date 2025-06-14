package com.example.kolos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="kinds_complaint")
public class KindsComplaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_kind_complaint")
    private Long idKindComplaint;

    @Column(name="name_kind_complaint", nullable = false)
    private String nameKindComplaint;
}
