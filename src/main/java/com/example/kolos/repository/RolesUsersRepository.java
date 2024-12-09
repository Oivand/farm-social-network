package com.example.kolos.repository;

import com.example.kolos.model.RolesUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesUsersRepository extends JpaRepository<RolesUsers, Long> {
}
