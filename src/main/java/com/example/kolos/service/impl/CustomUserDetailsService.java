package com.example.kolos.service.impl;

import com.example.kolos.model.User;
import com.example.kolos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        logger.debug("--- DIAGNOSTIC LOGS for user: {} ---", username);
        logger.debug("User fetched from DB: {}", user.getNickname());
        logger.debug("Password from DB (hashed): {}", user.getPassword());
        logger.debug("Role from DB: {}", user.getRole_rights());

        if (passwordEncoder != null) {
            logger.debug("PasswordEncoder CLASS in CustomUserDetailsService: {}", passwordEncoder.getClass().getName());
            logger.debug("Is it DelegatingPasswordEncoder? {}", (passwordEncoder instanceof org.springframework.security.crypto.password.DelegatingPasswordEncoder));
        } else {
            logger.debug("PasswordEncoder is NULL in CustomUserDetailsService!");
        }
        // --- КОНЕЦ НОВОГО ЛОГА ---

        logger.debug("--- END DIAGNOSTIC LOGS ---");

        String roleName = user.getRole_rights();

        return new org.springframework.security.core.userdetails.User(
                user.getNickname(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName))
        );
    }
}