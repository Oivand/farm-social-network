package com.example.kolos.service.impl;

import com.example.kolos.model.RolesUsers;
import com.example.kolos.repository.RolesUsersRepository;  // Репозиторий
import com.example.kolos.service.RolesUsersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesUsersServiceImpl implements RolesUsersService {

    private final RolesUsersRepository rolesUsersRepository;  // Репозиторий

    // Конструктор для внедрения репозитория
    public RolesUsersServiceImpl(RolesUsersRepository rolesUsersRepository) {
        this.rolesUsersRepository = rolesUsersRepository;
    }

    @Override
    public List<RolesUsers> searchRolesByName(String userRole) {
        if (userRole == null || userRole.isEmpty()) {
            throw new IllegalArgumentException("User Role cannot be null or empty.");
        }
        return rolesUsersRepository.findByUserRoleContainingOrderByUserRoleAsc(userRole);  // Используем репозиторий
    }

    @Override
    public List<RolesUsers> findAll() {

        return rolesUsersRepository.findAll();  // Используем репозиторий
    }

    @Override
    public Optional<RolesUsers> findById(Long idRoleUser) {
        if (idRoleUser == null) {
            throw new IllegalArgumentException("ID  of user role cannot be null");
        }
        return rolesUsersRepository.findById(idRoleUser);  // Используем репозиторий
    }
}
