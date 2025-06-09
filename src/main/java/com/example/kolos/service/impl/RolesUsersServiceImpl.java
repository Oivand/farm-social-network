package com.example.kolos.service.impl;

import com.example.kolos.model.RolesUsers;
import com.example.kolos.repository.RolesUsersRepository;
import com.example.kolos.service.RolesUsersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesUsersServiceImpl implements RolesUsersService {

    private final RolesUsersRepository rolesUsersRepository;

    public RolesUsersServiceImpl(RolesUsersRepository rolesUsersRepository) {
        this.rolesUsersRepository = rolesUsersRepository;
    }

    @Override
    public List<RolesUsers> searchRolesByName(String userRole) {
        if (userRole == null || userRole.trim().isEmpty()) {
            throw new IllegalArgumentException("User Role cannot be null or empty.");
        }
        // Изменение: Теперь просто возвращаем список. Если ничего не найдено,
        // вернется пустой список, что будет обработано контроллером.
        return rolesUsersRepository.findByUserRoleContainingOrderByUserRoleAsc(userRole);
    }

    @Override
    public List<RolesUsers> findAll() {
        return rolesUsersRepository.findAll();
    }

    @Override
    public Optional<RolesUsers> findById(Long idRoleUser) {
        if (idRoleUser == null) {
            throw new IllegalArgumentException("ID of user role cannot be null.");
        }
        return rolesUsersRepository.findById(idRoleUser);
    }
}