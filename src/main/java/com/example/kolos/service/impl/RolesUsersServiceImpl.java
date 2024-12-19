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
        return rolesUsersRepository.findByUserRoleContainingOrderByUserRoleAsc(userRole);  // Используем репозиторий
    }

    @Override
    public List<RolesUsers> findAll() {
        return rolesUsersRepository.findAll();  // Используем репозиторий
    }

    @Override
    public Optional<RolesUsers> findById(Long idRoleUser) {
        return rolesUsersRepository.findById(idRoleUser);  // Используем репозиторий
    }
}
