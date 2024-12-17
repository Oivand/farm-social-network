package com.example.kolos.service;
import com.example.kolos.interfaces.RolesUsersInterface;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.repository.RolesUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolesUsersService /*implements RolesUsersInterface {
    private final RolesUsersRepository rolesUsersRepository;

    @Override
    public List<RolesUsers> getRolesUsersByIdRoleUser(Long idRoleUser) {
        return rolesUsersRepository.findByIdRoleUser(idRoleUser);
    }

    @Override
    public List<RolesUsers> getAllRolesUsers() {
        return rolesUsersRepository.findAll();
    }

    @Override
    public List<RolesUsers> getRolesUsersByUserRole(String userRole) {
        return rolesUsersRepository.findByUserRole(userRole);
    }
}*/ {}
