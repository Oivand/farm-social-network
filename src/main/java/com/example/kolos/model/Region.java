package com.example.kolos.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_region")
    private Long idRegion;

    @Column(name="name_region", nullable = false, unique = true)
    private String nameRegion;

}
