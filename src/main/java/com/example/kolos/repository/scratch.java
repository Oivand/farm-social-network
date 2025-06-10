package com.example.kolos.repository;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;

public class scratch { // В скрэтч-файле имя класса может быть любым или отсутствовать
    public static void main(String[] args) {
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Сгенерированный JWT Secret (Base64):");
        System.out.println(base64Key);
        System.out.println("\nДлина ключа в байтах: " + keyBytes.length);
    }
}