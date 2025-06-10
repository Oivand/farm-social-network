package com.example.kolos.controller;

import com.example.kolos.model.User;
import com.example.kolos.security.JwtTokenUtil;
import com.example.kolos.service.UserService; // Возможно, вам понадобится UserService для регистрации
import com.example.kolos.service.impl.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService; // Для регистрации, если это входит в функционал AuthController

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          CustomUserDetailsService userDetailsService,
                          UserService userService) { // Внедряем UserService
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    // DTO для запроса аутентификации
    public static class JwtRequest {
        private String username;
        private String password;

        // геттеры и сеттеры
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // DTO для ответа аутентификации
    public static class JwtResponse {
        private final String jwttoken;
        public JwtResponse(String jwttoken) { this.jwttoken = jwttoken; }
        public String getToken() { return this.jwttoken; }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            // Аутентификация пользователя с помощью AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        // Если аутентификация прошла успешно, генерируем токен
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Добавьте валидацию полей user (username, password, email)
        if (userService.findByNicknameExact(user.getUsername()).isPresent()) { // Предполагаем findByUsername в UserService
            return ResponseEntity.badRequest().body("Username already exists!");
        }
        if (userService.findUserByEmail(user.getEmail()).isPresent()) { // Предполагаем findByEmail в UserService
            return ResponseEntity.badRequest().body("Email already exists!");
        }

        User registeredUser = userService.save(user); // Пароль должен быть зашифрован внутри userService.save()
        return ResponseEntity.ok("User registered successfully!");
    }
}