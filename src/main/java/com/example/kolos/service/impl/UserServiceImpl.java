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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final RolesUsersRepository rolesUsersRepository;
    private final SectorRepository sectorRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RegionRepository regionRepository,
                           RolesUsersRepository rolesUsersRepository,
                           SectorRepository sectorRepository) {
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.rolesUsersRepository = rolesUsersRepository;
        this.sectorRepository = sectorRepository;
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
            return userRepository.findByNameContainingOrderByNameAsc(name);
        }

        @Override
        public List<User> findUserBySurname(String surname) {
            if (surname == null || surname.trim().isEmpty()) {
                throw new IllegalArgumentException("Фамилия не может быть пустой.");
            }
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
            return userRepository.findByNameAndSurname(name, surname);
        }

        @Override
        public List<User> findUserByRegion(Region userRegion) {
            if (userRegion == null || userRegion.getIdRegion () == null) {
                throw new IllegalArgumentException("Регион или его ID не могут быть пустыми.");
            }
            return userRepository.findByUserRegion(userRegion);
        }

        @Override
        public List<User> findUserByRole(RolesUsers role) {
            if (role == null || role.getIdRoleUser () == null) {
                throw new IllegalArgumentException("Роль или её ID не могут быть пустыми.");
            }
            return userRepository.findByRole(role);
        }

        @Override
        public List<User> findUserBySector(Sector sector) {
            if (sector == null || sector.getIdSector () == null) {
                throw new IllegalArgumentException("Сектор или его ID не могут быть пустыми.");
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

        @Override
        public List<User> findUserByNickname(String nickname) {
            if (nickname == null || nickname.trim().isEmpty()) {
                throw new IllegalArgumentException("Никнейм не может быть пустым.");
            }
            return userRepository.findByNicknameContainingOrderByNicknameAsc(nickname);
        }

        // --- Реализация метода findUserByDateOfBirth ---
        @Override
        public List<User> findUserByDateOfBirth(LocalDate dateOfBirth) {
            if (dateOfBirth == null) {
                throw new IllegalArgumentException("Дата рождения не может быть пустой.");
            }
            return userRepository.findByDateOfBirth(dateOfBirth);
        }
        // --- Конец реализации метода findUserByDateOfBirth ---

        @Override
        public User save(User user) {
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
            if (user.getRole() == null || user.getRole().getIdRoleUser () == null) {
                throw new IllegalArgumentException("Роль пользователя обязательна.");
            }

            if (user.getIdUser() == null) {
                if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                    throw new IllegalArgumentException("Пользователь с таким email уже существует.");
                }
                if (userRepository.findByPhone(user.getPhone()).isPresent()) {
                    throw new IllegalArgumentException("Пользователь с таким телефоном уже существует.");
                }
                if (user.getNickname() != null && !user.getNickname().trim().isEmpty()) {
                    if (!userRepository.findByNicknameContainingOrderByNicknameAsc(user.getNickname()).isEmpty()) {
                        throw new IllegalArgumentException("Пользователь с таким никнеймом уже существует.");
                    }
                }
            }


            if (user.getUserRegion() != null && user.getUserRegion().getIdRegion () != null) {
                Region region = regionRepository.findById(user.getUserRegion().getIdRegion())
                        .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + user.getUserRegion().getIdRegion() + " не найден."));
                user.setUserRegion(region);
            } else if (user.getUserRegion() != null) {
                user.setUserRegion(null);
            }


            if (user.getRole() != null && user.getRole().getIdRoleUser() != null) {
                RolesUsers role = rolesUsersRepository.findById(user.getRole().getIdRoleUser())
                        .orElseThrow(() -> new IllegalArgumentException("Роль с ID " + user.getRole().getIdRoleUser() + " не найдена."));
                user.setRole(role);
            } else {
                throw new IllegalArgumentException("Роль пользователя обязательна.");
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
        public User update(Long idUser, User userDetails) {
            if (idUser == null) {
                throw new IllegalArgumentException("ID пользователя для обновления не может быть null.");
            }
            if (userDetails == null) {
                throw new IllegalArgumentException("Объект пользователя для обновления не может быть null.");
            }

            User existingUser = userRepository.findById(idUser)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + idUser + " не найден для обновления."));

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
            if (userDetails.getNickname() != null) {
                List<User> usersWithSameNickname = userRepository.findByNicknameContainingOrderByNicknameAsc(userDetails.getNickname());
                if (!usersWithSameNickname.isEmpty() && usersWithSameNickname.stream().anyMatch(u -> !u.getIdUser().equals(idUser))) {
                    throw new IllegalArgumentException("Пользователь с таким никнеймом уже существует.");
                }
                existingUser.setNickname(userDetails.getNickname().trim().isEmpty() ? null : userDetails.getNickname());
            } else {
                existingUser.setNickname(null);
            }
            if (userDetails.getDateOfBirth() != null) {
                existingUser.setDateOfBirth(userDetails.getDateOfBirth());
            }

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
                throw new IllegalArgumentException("Роль пользователя обязательна.");
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
        public void delete(Long idUser) {
            if (idUser == null) {
                throw new IllegalArgumentException("ID пользователя для удаления не может быть null.");
            }
            if (!userRepository.existsById(idUser)) {
                throw new IllegalArgumentException("Пользователь с ID " + idUser + " не найден для удаления.");
            }
            userRepository.deleteById(idUser);
        }
    }
