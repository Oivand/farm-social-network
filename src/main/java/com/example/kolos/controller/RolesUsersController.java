package com.example.kolos.controller;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.service.RolesUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesUsersController {
    private final RolesUsersService rolesUsersService;

    public RolesUsersController(RolesUsersService rolesUsersService) {
        this.rolesUsersService = rolesUsersService;
    }
    @GetMapping
    public List<RolesUsers> getAllRolesUsers(){
        return rolesUsersService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesUsers> getUserRoleById(@PathVariable Long id) {
        return rolesUsersService.findById(id).
                map(ResponseEntity::ok).
                orElse(ResponseEntity.notFound().build());
    }

    /*
    * http://localhost:8080/roles/search?name=%D1%84%D0%B5%D1%80%D0%BC%D0%B5%D1%80
    * */
    @GetMapping("/search")
    public ResponseEntity<List<RolesUsers>> getRolesByName(@RequestParam String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Возвращаем 400, если имя не указано
        }
        List<RolesUsers> roles = rolesUsersService.searchRolesByName(name);
        return roles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(roles);
    }


}