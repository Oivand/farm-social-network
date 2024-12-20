package com.example.kolos.controller;
import com.example.kolos.service.RolesUsersService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usersRole")
public class RolesUsersContoller {
    private final RolesUsersService rolesUsersService;

    public RolesUsersContoller(RolesUsersService rolesUsersService) {
        this.rolesUsersService = rolesUsersService;
    }
}