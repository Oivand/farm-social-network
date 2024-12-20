package com.example.kolos.service.impl;

import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;
import com.example.kolos.repository.UserRepository;
import com.example.kolos.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email не может быть пустым.");
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserByPhone(String phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Телефон не может быть пустым.");
        }
        return userRepository.findByPhone(phone);
    }

    @Override
    public List<User> findUserByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        return userRepository.findByNameContainingOrderByNameAsc(name);
    }

    @Override
    public List<User> findUserBySurname(String surname) {
        if (surname == null || surname.isEmpty()) {
            throw new IllegalArgumentException("Фамилия не может быть пустой.");
        }
        return userRepository.findBySurnameContainingOrderBySurnameAsc(surname);
    }

    @Override
    public List<User> findUserByNameAndSurname(String name, String surname) {
        if ((name == null || name.isEmpty()) && (surname == null || surname.isEmpty())) {
            throw new IllegalArgumentException("Имя и фамилия не могут быть пустыми.");
        }
        if (surname == null || surname.isEmpty()) {
            return findUserByName(name);
        }
        if (name == null || name.isEmpty()) {
            return findUserBySurname(surname);
        }
        return userRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public List<User> findUserByRegion(Region userRegion) {
        if (userRegion == null) {
            throw new IllegalArgumentException("Регион не может быть пустым.");
        }
        return userRepository.findByUserRegion(userRegion);
    }

    @Override
    public List<User> findUserByRole(RolesUsers role) {
        if (role == null) {
            throw new IllegalArgumentException("Роль не может быть пустой.");
        }
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> findUserBySector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("Сектор не может быть пустым.");
        }
        return userRepository.findBySector(sector);
    }

    @Override
    public Optional<User> findById(Long idUser) {
        if (idUser == null) {
            throw new IllegalArgumentException("ID пользователя не может быть пустым.");
        }
        return userRepository.findById(idUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
