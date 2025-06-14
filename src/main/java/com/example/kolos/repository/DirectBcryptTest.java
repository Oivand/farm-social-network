package com.example.kolos.repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Убедитесь, что импорт правильный
import java.security.SecureRandom;
import java.util.Base64;
public class DirectBcryptTest {

        public static void main(String[] args) {
            SecureRandom secureRandom = new SecureRandom();
            byte[] keyBytes = new byte[64]; // 512 bits = 64 bytes
            secureRandom.nextBytes(keyBytes);
            String base64Key = Base64.getEncoder().encodeToString(keyBytes);
            System.out.println("Generated Base64 Key: " + base64Key);
        }
    }
