package com.example.kolos.repository;

import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByNickname(String nickname); // For exact nickname match (e.g., for uniqueness)

    // For methods returning List<User> that use 'Containing' and 'OrderBy'
    List<User> findByNameContainingOrderByNameAsc(String name);
    List<User> findBySurnameContainingOrderBySurnameAsc(String surname);
    List<User> findByNameAndSurname(String name, String surname); // If this is exact match, otherwise adjust

    List<User> findByUserRegion(Region userRegion);
    List<User> findByRole(RolesUsers role);
    List<User> findBySector(Sector sector);

    List<User> findByNicknameContainingOrderByNicknameAsc(String nickname); // For partial nickname search
    List<User> findByDateOfBirth(LocalDate dateOfBirth);

    // If you need more specific searches, e.g., case-insensitive containing:
    // List<User> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
    // List<User> findBySurnameContainingIgnoreCaseOrderBySurnameAsc(String surname);
    // List<User> findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname);
    // List<User> findByNicknameContainingIgnoreCaseOrderByNicknameAsc(String nickname);
}