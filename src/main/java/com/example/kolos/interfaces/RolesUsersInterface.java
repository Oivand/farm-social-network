package com.example.kolos.interfaces;

import com.example.kolos.model.RolesUsers;

import java.util.List;

public interface RolesUsersInterface {

    List<RolesUsers> searchRolesByName(String userRole);
}
