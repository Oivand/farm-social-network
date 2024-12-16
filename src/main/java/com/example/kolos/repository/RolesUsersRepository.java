package com.example.kolos.repository;

import com.example.kolos.model.RolesUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesUsersRepository extends JpaRepository<RolesUsers, Long> {
    //найти регион по айди
    List<RolesUsers> findByIdRoleUser(Long idRoleUser);

    //найти роли по названию
    List<RolesUsers> findByUserRole(String userRole);

}
