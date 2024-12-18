package com.example.kolos.service.impl;

import com.example.kolos.interfaces.RolesUsersInterface;
import com.example.kolos.model.RolesUsers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesUsersServiceImpl implements RolesUsersInterface {
    @Override
    public List<RolesUsers> searchRolesByName(String userRole) {
        return List.of();
    }
}
