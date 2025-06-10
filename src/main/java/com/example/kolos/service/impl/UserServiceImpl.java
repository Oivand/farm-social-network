package com.example.kolos.service.impl;

import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;
import com.example.kolos.repository.RegionRepository;
import com.example.kolos.repository.RolesUsersRepository;
import com.example.kolos.repository.SectorRepository;
import com.example.kolos.repository.UserRepository;
import com.example.kolos.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For transactional operations

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final RolesUsersRepository rolesUsersRepository;
    private final SectorRepository sectorRepository;
    private final PasswordEncoder passwordEncoder; // ADDED: PasswordEncoder dependency

    public UserServiceImpl(UserRepository userRepository,
                           RegionRepository regionRepository,
                           RolesUsersRepository rolesUsersRepository,
                           SectorRepository sectorRepository,
                           PasswordEncoder passwordEncoder) { // ADDED: PasswordEncoder in constructor
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.rolesUsersRepository = rolesUsersRepository;
        this.sectorRepository = sectorRepository;
        this.passwordEncoder = passwordEncoder; // Initialize PasswordEncoder
    }

    @Override
    public Optional<User> findByNicknameExact(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Никнейм не может быть пустым.");
        }
        return userRepository.findByNickname(nickname); // Используем Optional<User> findByNickname из репозитория
    }

    @Override
    @Transactional // Ensure this method is transactional for save operations
    public User save(User user) {
        // Basic null checks for required fields
        if (user == null) {
            throw new IllegalArgumentException("Объект пользователя не может быть null.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email пользователя обязателен.");
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон пользователя обязателен.");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя обязательно.");
        }
        if (user.getSurname() == null || user.getSurname().trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия пользователя обязательна.");
        }
        if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
            throw new IllegalArgumentException("Никнейм пользователя обязателен.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль пользователя обязателен.");
        }
        // Ensure role is provided or set default
        if (user.getRole() == null || user.getRole().getIdRoleUser() == null) {
            throw new IllegalArgumentException("Роль пользователя обязательна.");
        }

        // Uniqueness checks for new users (assuming idUser is null for new users)
        if (user.getIdUser() == null) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует.");
            }
            if (userRepository.findByPhone(user.getPhone()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с таким телефоном уже существует.");
            }
            // For nickname, it's better to use findByNickname (exact match for uniqueness) if available in repo.
            // If findByNicknameContainingOrderByNicknameAsc is the only option, ensure it returns 0 for no exact match.
            if (userRepository.findByNickname(user.getNickname()).isPresent()) { // Preferred for strict uniqueness
                throw new IllegalArgumentException("Пользователь с таким никнеймом уже существует.");
            }
            // Fallback if only 'Containing' is available:
            // if (!userRepository.findByNicknameContainingOrderByNicknameAsc(user.getNickname()).isEmpty() &&
            //     userRepository.findByNicknameContainingOrderByNicknameAsc(user.getNickname()).stream().anyMatch(u -> u.getNickname().equals(user.getNickname()))){
            //     throw new IllegalArgumentException("Пользователь с таким никнеймом уже существует.");
            // }
        }

        // --- PASSWORD ENCRYPTION ---
        // Only encrypt if the password is new or explicitly changed (for existing users, this should be handled carefully)
        // For new users, we always encrypt the provided password
        if (user.getIdUser() == null || (user.getPassword() != null && !user.getPassword().isEmpty())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // --- END PASSWORD ENCRYPTION ---

        // Set default role_rights if not provided (this is the string for Spring Security's GrantedAuthority)
        if (user.getRole_rights() == null || user.getRole_rights().trim().isEmpty()) {
            user.setRole_rights("ROLE_USER"); // Default role string for Spring Security
        }

        // Validate and set associated entities (Region, RolesUsers, Sector)
        // Ensure that the IDs provided for associated entities actually exist in their respective repositories.
        if (user.getUserRegion() != null && user.getUserRegion().getIdRegion() != null) {
            Region region = regionRepository.findById(user.getUserRegion().getIdRegion())
                    .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + user.getUserRegion().getIdRegion() + " не найден."));
            user.setUserRegion(region);
        } else if (user.getUserRegion() != null) { // If userRegion object is not null but ID is null
            user.setUserRegion(null); // Explicitly set to null if intended
        }

        // RolesUsers is mandatory, so validate and set it
        if (user.getRole() != null && user.getRole().getIdRoleUser() != null) {
            RolesUsers role = rolesUsersRepository.findById(user.getRole().getIdRoleUser())
                    .orElseThrow(() -> new IllegalArgumentException("Роль с ID " + user.getRole().getIdRoleUser() + " не найдена."));
            user.setRole(role);
        } else {
            throw new IllegalArgumentException("Роль пользователя обязательна и должна иметь действительный ID.");
        }

        if (user.getSector() != null && user.getSector().getIdSector() != null) {
            Sector sector = sectorRepository.findById(user.getSector().getIdSector())
                    .orElseThrow(() -> new IllegalArgumentException("Сектор с ID " + user.getSector().getIdSector() + " не найден."));
            user.setSector(sector);
        } else if (user.getSector() != null) {
            user.setSector(null);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(Long idUser, User userDetails) {
        if (idUser == null) {
            throw new IllegalArgumentException("ID пользователя для обновления не может быть null.");
        }
        if (userDetails == null) {
            throw new IllegalArgumentException("Объект пользователя для обновления не может быть null.");
        }

        User existingUser = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + idUser + " не найден для обновления."));

        // Update fields if provided and different
        if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
            Optional<User> userWithSameEmail = userRepository.findByEmail(userDetails.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getIdUser().equals(idUser)) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует.");
            }
            existingUser.setEmail(userDetails.getEmail());

        }
        if (userDetails.getPhone() != null && !userDetails.getPhone().trim().isEmpty()) {
            Optional<User> userWithSamePhone = userRepository.findByPhone(userDetails.getPhone());
            if (userWithSamePhone.isPresent() && !userWithSamePhone.get().getIdUser().equals(idUser)) {
                throw new IllegalArgumentException("Пользователь с таким телефоном уже существует.");
            }
            existingUser.setPhone(userDetails.getPhone());
        }
        if (userDetails.getName() != null && !userDetails.getName().trim().isEmpty()) {
            existingUser.setName(userDetails.getName());
        }
        if (userDetails.getSurname() != null && !userDetails.getSurname().trim().isEmpty()) {
            existingUser.setSurname(userDetails.getSurname());
        }
        if (userDetails.getNickname() != null && !userDetails.getNickname().trim().isEmpty()) {
            Optional<User> userWithSameNickname = userRepository.findByNickname(userDetails.getNickname()); // Preferred for exact match
            if (userWithSameNickname.isPresent() && !userWithSameNickname.get().getIdUser().equals(idUser)) {
                throw new IllegalArgumentException("Пользователь с таким никнеймом уже существует.");
            }
            // If only 'Containing' is available:
            // List<User> usersWithSameNickname = userRepository.findByNicknameContainingOrderByNicknameAsc(userDetails.getNickname());
            // if (!usersWithSameNickname.isEmpty() && usersWithSameNickname.stream().anyMatch(u -> !u.getIdUser().equals(idUser) && u.getNickname().equals(userDetails.getNickname()))) {
            //     throw new IllegalArgumentException("Пользователь с таким никнеймом уже существует.");
            // }
            existingUser.setNickname(userDetails.getNickname());
        } else if (userDetails.getNickname() != null) { // If nickname is explicitly set to empty/blank
            existingUser.setNickname(null); // Or throw error if nickname cannot be null
        }


        if (userDetails.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userDetails.getDateOfBirth());
        }
        if (userDetails.getBio() != null) {
            existingUser.setBio(userDetails.getBio());
        }

        // --- PASSWORD ENCRYPTION FOR UPDATE ---
        // Only update password if a new password is provided in userDetails
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        // --- END PASSWORD ENCRYPTION FOR UPDATE ---

        // Update role_rights string for Spring Security
        if (userDetails.getRole_rights() != null && !userDetails.getRole_rights().trim().isEmpty()) {
            existingUser.setRole_rights(userDetails.getRole_rights());
        } else if (userDetails.getRole_rights() != null) {
            existingUser.setRole_rights(null); // Or handle as needed if role_rights can't be null
        }

        // Validate and update associated entities (Region, RolesUsers, Sector)
        // Similar to save, ensure IDs are valid.
        if (userDetails.getUserRegion() != null && userDetails.getUserRegion().getIdRegion() != null) {
            Region newRegion = regionRepository.findById(userDetails.getUserRegion().getIdRegion())
                    .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + userDetails.getUserRegion().getIdRegion() + " не найден."));
            existingUser.setUserRegion(newRegion);
        } else if (userDetails.getUserRegion() != null) {
            existingUser.setUserRegion(null);
        }

        if (userDetails.getRole() != null && userDetails.getRole().getIdRoleUser() != null) {
            RolesUsers newRole = rolesUsersRepository.findById(userDetails.getRole().getIdRoleUser())
                    .orElseThrow(() -> new IllegalArgumentException("Роль с ID " + userDetails.getRole().getIdRoleUser() + " не найдена."));
            existingUser.setRole(newRole);
        } else {
            throw new IllegalArgumentException("Роль пользователя обязательна."); // Role cannot be null
        }

        if (userDetails.getSector() != null && userDetails.getSector().getIdSector() != null) {
            Sector newSector = sectorRepository.findById(userDetails.getSector().getIdSector())
                    .orElseThrow(() -> new IllegalArgumentException("Сектор с ID " + userDetails.getSector().getIdSector() + " не найден."));
            existingUser.setSector(newSector);
        } else if (userDetails.getSector() != null) {
            existingUser.setSector(null);
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void delete(Long idUser) {
        if (idUser == null) {
            throw new IllegalArgumentException("ID пользователя для удаления не может быть null.");
        }
        if (!userRepository.existsById(idUser)) {
            throw new IllegalArgumentException("Пользователь с ID " + idUser + " не найден для удаления.");
        }
        userRepository.deleteById(idUser);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым.");
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон не может быть пустым.");
        }
        return userRepository.findByPhone(phone);
    }

    @Override
    public List<User> findUserByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        // Assuming findByNameContainingOrderByNameAsc is the correct method in UserRepository
        return userRepository.findByNameContainingOrderByNameAsc(name);
    }

    @Override
    public List<User> findUserBySurname(String surname) {
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия не может быть пустой.");
        }
        // Assuming findBySurnameContainingOrderBySurnameAsc is the correct method in UserRepository
        return userRepository.findBySurnameContainingOrderBySurnameAsc(surname);
    }

    @Override
    public List<User> findUserByNameAndSurname(String name, String surname) {
        if ((name == null || name.trim().isEmpty()) && (surname == null || surname.trim().isEmpty())) {
            throw new IllegalArgumentException("Имя и фамилия не могут быть одновременно пустыми.");
        }
        if (name == null || name.trim().isEmpty()) {
            return findUserBySurname(surname);
        }
        if (surname == null || surname.trim().isEmpty()) {
            return findUserByName(name);
        }
        // Assuming findByNameAndSurname is the correct method in UserRepository
        return userRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public List<User> findUserByRegion(Region userRegion) {
        if (userRegion == null || userRegion.getIdRegion() == null) {
            throw new IllegalArgumentException("Регион или его ID не могут быть пустыми.");
        }
        // In a production scenario, you might want to fetch the Region entity from its repository
        // before passing it to userRepository.findByUserRegion(regionRepository.findById(userRegion.getIdRegion()).orElseThrow(...));
        return userRepository.findByUserRegion(userRegion);
    }

    @Override
    public List<User> findUserByRole(RolesUsers role) {
        if (role == null || role.getIdRoleUser() == null) {
            throw new IllegalArgumentException("Роль или её ID не могут быть пустыми.");
        }
        // Similar to Region, consider fetching the RolesUsers entity from its repository first.
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> findUserBySector(Sector sector) {
        if (sector == null || sector.getIdSector() == null) {
            throw new IllegalArgumentException("Сектор или его ID не могут быть пустыми.");
        }
        // Similar to Region, consider fetching the Sector entity from its repository first.
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

    @Override
    public List<User> findUserByNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Никнейм не может быть пустым.");
        }
        // Assuming findByNicknameContainingOrderByNicknameAsc is the correct method in UserRepository
        return userRepository.findByNicknameContainingOrderByNicknameAsc(nickname);
    }

    @Override
    public List<User> findUserByDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Дата рождения не может быть пустой.");
        }
        return userRepository.findByDateOfBirth(dateOfBirth);
    }
}