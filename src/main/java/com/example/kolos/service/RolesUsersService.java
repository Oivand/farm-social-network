package com.example.kolos.service;

import com.example.kolos.model.RolesUsers;

import java.util.List;
import java.util.Optional;

public interface RolesUsersService {

    List<RolesUsers> searchRolesByName(String userRole);

    List<RolesUsers> findAll();

    Optional<RolesUsers> findById(Long idRoleUser);

}
