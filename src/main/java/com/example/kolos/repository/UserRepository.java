package com.example.kolos.repository;

import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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

//    // Обновление имени
//    @Modifying
//    @Query("UPDATE User u SET u.name = :name WHERE u.idUser = :id")
//    void updateUserName(@Param("id") Long id, @Param("name") String name);
//
//    // Обновление фамилии
//    @Modifying
//    @Query("UPDATE User u SET u.surname = :surname WHERE u.idUser = :id")
//    void updateUserSurname(@Param("id") Long id, @Param("surname") String surname);
//
//    // Обновление email
//    @Modifying
//    @Query("UPDATE User u SET u.email = :email WHERE u.idUser = :id")
//    void updateUserEmail(@Param("id") Long id, @Param("email") String email);
//
//    // Обновление номера телефона
//    @Modifying
//    @Query("UPDATE User u SET u.phone = :phone WHERE u.idUser = :id")
//    void updateUserPhone(@Param("id") Long id, @Param("phone") String phone);
//
//    // Обновление биографии
//    @Modifying
//    @Query("UPDATE User u SET u.bio = :bio WHERE u.idUser = :id")
//    void updateUserBio(@Param("id") Long id, @Param("bio") String bio);
//
//    // Обновление региона
//    @Modifying
//    @Query("UPDATE User u SET u.userRegion = :region WHERE u.idUser = :id")
//    void updateUserRegion(@Param("id") Long id, @Param("region") Region region);
//
//    // Обновление роли пользователя
//    @Modifying
//    @Query("UPDATE User u SET u.role = :role WHERE u.idUser = :id")
//    void updateUserRole(@Param("id") Long id, @Param("role") RolesUsers role);
//
//    // Обновление сектора пользователя
//    @Modifying
//    @Query("UPDATE User u SET u.sector = :sector WHERE u.idUser = :id")
//    void updateUserSector(@Param("id") Long id, @Param("sector") Sector sector);
//
//    // Удаление пользователя по ID
//    @Modifying
//    @Query("DELETE FROM User u WHERE u.idUser = :id")
//    void deleteUserById(@Param("id") Long id);
}

