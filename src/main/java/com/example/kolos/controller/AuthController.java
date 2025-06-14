package com.example.kolos.controller;

import com.example.kolos.dto.JwtRequest;
import com.example.kolos.dto.JwtResponse;
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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final RolesUsersRepository roleRepository;
    private final SectorRepository sectorRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            RolesUsers role = roleRepository.findById(registrationDto.getIdRoleUser())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            Sector sector = sectorRepository.findById(registrationDto.getIdSector())
                    .orElseThrow(() -> new EntityNotFoundException("Sector not found"));
            Region region = regionRepository.findById(registrationDto.getIdRegion())
                    .orElseThrow(() -> new EntityNotFoundException("Region not found"));

            if (userService.findUserByEmail(registrationDto.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            if (userService.findByNicknameExact(registrationDto.getNickname()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname already exists");
            }
            log.info("Raw password: {}", registrationDto.getPassword());
            log.info("Encoded password: {}", passwordEncoder.encode(registrationDto.getPassword()));

            User newUser = User.builder()
                    .email(registrationDto.getEmail())
                    .phone(registrationDto.getPhone())
                    .name(registrationDto.getName())
                    .surname(registrationDto.getSurname())
                    .nickname(registrationDto.getNickname())
                    .password(passwordEncoder.encode(registrationDto.getPassword()))
                    .role(role)
                    .sector(sector)
                    .userRegion(region)
                    .dateOfBirth(registrationDto.getDateOfBirth())
                    .build();

            userService.save(newUser);
            log.info("User registered successfully: {}", registrationDto.getEmail());

            return ResponseEntity.ok("User registered successfully");

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Registration error", e);
            return ResponseEntity.internalServerError().body("Registration failed");
        }
    }
}