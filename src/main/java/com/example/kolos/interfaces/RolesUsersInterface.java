package com.example.kolos.interfaces;

import com.example.kolos.model.RolesUsers;

import java.util.List;

public interface RolesUsersInterface {
    List<RolesUsers> getRolesUsersByIdRoleUser(Long idRoleUser);

    List<RolesUsers> getAllRolesUsers();
    List<RolesUsers> getRolesUsersByUserRole(String userRole);
}
