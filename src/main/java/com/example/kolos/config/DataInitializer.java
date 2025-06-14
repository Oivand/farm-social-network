package com.example.kolos.config;

import com.example.kolos.model.*;
import com.example.kolos.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RegionRepository regionRepository;
    private final RolesUsersRepository rolesUsersRepository;
    private final SectorRepository sectorRepository;
    private final UserRepository userRepository;
    private final KindsComplaintRepository kindsComplaintRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        log.info("Application is ready, starting data initialization at {}", event.getTimestamp());
        initializeRegions();
        initializeRolesUsers();
        initializeSectors();
        initializeKindsComplaint();
        initializeUsers();
        log.info("Data initialization completed successfully");
    }

    private void initializeRegions() {
        if (regionRepository.count() == 0) {
            String[] regionNames = {
                    "город Челябинск", "Агаповский", "Варненский", "Верхнеуральский",
                    "Еманжелинский", "Еткульский", "Карталинский", "Каслинский"
            };
            long[] regionIds = {1, 4, 5, 6, 7, 8, 9, 10};
            for (int i = 0; i < regionNames.length; i++) {
                Region region = Region.builder()
                        .idRegion(regionIds[i])
                        .nameRegion(regionNames[i])
                        .build();
                regionRepository.save(region);
            }
            log.info("Initialized {} regions", regionNames.length);
        } else {
            log.info("Regions already initialized, skipping");
        }
    }

    private void initializeRolesUsers() {
        if (rolesUsersRepository.count() == 0) {
            String[] roleNames = {"другое", "фермер", "поставщик", "покупатель"};
            long[] roleIds = {1, 6, 7, 8};
            for (int i = 0; i < roleNames.length; i++) {
                RolesUsers role = RolesUsers.builder()
                        .idRoleUser(roleIds[i])
                        .userRole(roleNames[i])
                        .build();
                rolesUsersRepository.save(role);
            }
            log.info("Initialized {} roles", roleNames.length);
        } else {
            log.info("Roles already initialized, skipping");
        }
    }

    private void initializeSectors() {
        if (sectorRepository.count() == 0) {
            String[] sectorNames = {
                    "рыбоводство", "грибоводство", "животноводство", "козоводство",
                    "коневодство", "муловодство", "овцеводство", "птицеводство",
                    "пчеловодство", "свиноводство", "скотоводство", "семеноводство",
                    "виноградарство", "овощеводство", "садоводство", "другое"
            };
            long id = 1;
            for (String name : sectorNames) {
                Sector sector = Sector.builder()
                        .idSector(id++)
                        .nameSector(name)
                        .build();
                sectorRepository.save(sector);
            }
            log.info("Initialized {} sectors", sectorNames.length);
        } else {
            log.info("Sectors already initialized, skipping");
        }
    }

    private void initializeKindsComplaint() {
        if (kindsComplaintRepository.count() == 0) {
            String[] complaintNames = {
                    "Оскорбление", "Насилие", "Терроризм", "Персональные данные",
                    "Насилие над детьми", "Порнография", "Распространие накротиков",
                    "Спам", "Другое"
            };
            long id = 1;
            for (String name : complaintNames) {
                KindsComplaint complaint = KindsComplaint.builder()
                        .idKindComplaint(id++)
                        .nameKindComplaint(name)
                        .build();
                kindsComplaintRepository.save(complaint);
            }
            log.info("Initialized {} complaint kinds", complaintNames.length);
        } else {
            log.info("Complaint kinds already initialized, skipping");
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .idUser(19L)
                    .email("testuser@example.com")
                    .phone("+79001234567")
                    .name("Тест")
                    .surname("Пользователь")
                    .nickname("test_user_1")
                    .dateOfBirth(LocalDate.now())
                    .userRegion(regionRepository.findById(1L).orElseThrow())
                    .sector(sectorRepository.findById(1L).orElseThrow())
                    .role(rolesUsersRepository.findById(1L).orElseThrow())
                    .password(passwordEncoder.encode("SecurePassword123"))
                    .createdAt(LocalDateTime.now())
                    .role_rights("ROLE_USER")
                    .build();
            userRepository.save(user1);

            User user2 = User.builder()
                    .idUser(20L)
                    .email("new.final.user@example.com")
                    .phone("88888888888")
                    .name("New")
                    .surname("Final")
                    .nickname("new_final_user")
                    .dateOfBirth(LocalDate.now())
                    .userRegion(regionRepository.findById(1L).orElseThrow())
                    .sector(sectorRepository.findById(1L).orElseThrow())
                    .role(rolesUsersRepository.findById(1L).orElseThrow())
                    .password(passwordEncoder.encode("SecurePassword123"))
                    .createdAt(LocalDateTime.now())
                    .role_rights("ROLE_USER")
                    .build();
            userRepository.save(user2);

            log.info("Initialized 2 users");
        } else {
            log.info("Users already initialized, skipping");
        }
    }
}
