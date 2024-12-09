package com.example.kolos.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles_users")
public class RolesUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role_user")
    private Long idRoleUser;

    @Column(nullable = false, unique = true, name = "name_user_role")
    private String userRole;

    public RolesUsers() {}
}
