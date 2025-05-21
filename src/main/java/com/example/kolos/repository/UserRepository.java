package com.example.kolos.repository;

import com.example.kolos.model.User;
import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Найти пользователя по email
    Optional<User> findByEmail(String email);

    // Найти пользователя по номеру телефона
    Optional<User> findByPhone(String phone);

    // Найти пользователя по имени (с частичным совпадением) и отсортировать по имени
    List<User> findByNameContainingOrderByNameAsc(String name);

    // Найти пользователя по фамилии (с частичным совпадением) и отсортировать по фамилии
    List<User> findBySurnameContainingOrderBySurnameAsc(String surname);

    // Найти пользователя по имени и фамилии
    List<User> findByNameAndSurname(String name, String surname);

    // Найти пользователя по региону
    List<User> findByUserRegion(Region userRegion);

    // Найти пользователя по роли
    List<User> findByRole(RolesUsers role);

    // Найти пользователя по сектору
    List<User> findBySector(Sector sector);

    // Найти пользователя по никнейму (с частичным совпадением)
    List<User> findByNicknameContainingOrderByNicknameAsc(String nickname);

    // Найти пользователя по дате рождения
    List<User> findByDateOfBirth(LocalDate dateOfBirth);
}
