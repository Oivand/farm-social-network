package com.example.kolos.repository;

import com.example.kolos.model.RolesUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesUsersRepository extends JpaRepository<RolesUsers, Long> {
    RolesUsers findByUserRole(String userRole);

    List<RolesUsers> findByUserRoleContainingOrderByUserRoleAsc(String userRole);

}
