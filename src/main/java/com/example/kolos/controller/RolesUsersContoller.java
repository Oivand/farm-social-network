package com.example.kolos.controller;

import com.example.kolos.service.RolesUsersService;
import org.springframework.stereotype.Controller;

@Controller
public class RolesUsersContoller {
    private final RolesUsersService rolesUsersService;

    public RolesUsersContoller(RolesUsersService rolesUsersService) {
        this.rolesUsersService = rolesUsersService;
    }
}
