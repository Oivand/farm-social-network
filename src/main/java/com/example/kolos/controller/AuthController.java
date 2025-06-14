package com.example.kolos.controller;

import com.example.kolos.dto.UserRegistrationDto;
import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;
import com.example.kolos.repository.RolesUsersRepository;
import com.example.kolos.repository.RegionRepository;
import com.example.kolos.repository.SectorRepository;
import com.example.kolos.security.JwtTokenUtil;
import com.example.kolos.service.UserService;
import com.example.kolos.service.impl.CustomUserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // Важно добавить этот импорт!
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final RolesUsersRepository roleRepository; // Добавлен
    private final SectorRepository sectorRepository; // Добавлен
    private final RegionRepository regionRepository; // Добавлен
    private final PasswordEncoder passwordEncoder; // Добавлен

    // Конструктор теперь принимает все необходимые зависимости, включая репозитории и PasswordEncoder
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          CustomUserDetailsService userDetailsService,
                          UserService userService,
                          RolesUsersRepository roleRepository, // Добавлен
                          SectorRepository sectorRepository, // Добавлен
                          RegionRepository regionRepository, // Добавлен
                          PasswordEncoder passwordEncoder) { // Добавлен
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.sectorRepository = sectorRepository;
        this.regionRepository = regionRepository;
        this.passwordEncoder = passwordEncoder; // Инициализируем PasswordEncoder
    }

    // DTO для запроса аутентификации
    public static class JwtRequest {
        private String username;
        private String password;

        // геттеры и сеттеры
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO для ответа аутентификации
    public static class JwtResponse {
        private final String jwttoken;

        public JwtResponse(String jwttoken) {
            this.jwttoken = jwttoken;
        }

        public String getToken() {
            return this.jwttoken;
        }
    }

    // In AuthController.java, method createAuthenticationToken (@PostMapping("/authenticate"))
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String rawPasswordFromRequest = authenticationRequest.getPassword();
        String storedHashedPassword = userDetails.getPassword();

        // Debugging the actual incoming password and the stored hash
        System.out.println("DEBUG: User fetched from DB: " + authenticationRequest.getUsername());
        System.out.println("DEBUG: Password from DB (hashed): " + storedHashedPassword);
        System.out.println("DEBUG: Login attempt raw password: '" + rawPasswordFromRequest + "'");
        System.out.println("DEBUG: Login raw password length: " + rawPasswordFromRequest.length());
        System.out.println("DEBUG: Stored hashed password length: " + storedHashedPassword.length());
        log.info("");
        // This is the actual check done by Spring Security's DaoAuthenticationProvider
        boolean matches = passwordEncoder.matches(rawPasswordFromRequest, storedHashedPassword);
        System.out.println("DEBUG: Direct passwordEncoder.matches() result: " + matches); // This *should* be true if passwords match

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), rawPasswordFromRequest)
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            // If authentication fails, the passwordEncoder.matches() above should have been false
            throw new Exception("INVALID_CREDENTIALS", e);
        }


        // If authentication passes, generate token
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @PostMapping("/register") // ЭТОТ МЕТОД ДОЛЖЕН БЫТЬ ВАШИМ ЭНДПОИНТОМ РЕГИСТРАЦИИ
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            // Проверяем существование роли, сектора и региона по ID
            RolesUsers role = roleRepository.findById(registrationDto.getIdRoleUser())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + registrationDto.getIdRoleUser()));
            Sector sector = sectorRepository.findById(registrationDto.getIdSector())
                    .orElseThrow(() -> new EntityNotFoundException("Sector not found with ID: " + registrationDto.getIdSector()));
            Region region = regionRepository.findById(registrationDto.getIdRegion())
                    .orElseThrow(() -> new EntityNotFoundException("Region not found with ID: " + registrationDto.getIdRegion()));

            // Проверяем, существует ли пользователь с таким email или nickname
            if (userService.findUserByEmail(registrationDto.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists!");
            }
            if (userService.findByNicknameExact(registrationDto.getNickname()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname already exists!");
            }

            // Создаем нового пользователя
            User newUser = new User();
            newUser.setEmail(registrationDto.getEmail());
            newUser.setPhone(registrationDto.getPhone());
            newUser.setName(registrationDto.getName());
            newUser.setSurname(registrationDto.getSurname());
            newUser.setNickname(registrationDto.getNickname());
            // Кодируем пароль перед сохранением
            newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            newUser.setRole(role); // Устанавливаем найденную роль
            newUser.setSector(sector); // Устанавливаем найденный сектор
            newUser.setUserRegion(region); // Устанавливаем найденный регион
            newUser.setDateOfBirth(registrationDto.getDateOfBirth());
            userService.save(newUser); // Сохраняем пользователя через UserService

            System.out.println("DEBUG: Raw password for registration: " + registrationDto.getPassword());
            System.out.println("DEBUG: Encoded password for saving: " + newUser.getPassword());


            return ResponseEntity.ok("User registered successfully!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Общий обработчик для других ошибок
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

}