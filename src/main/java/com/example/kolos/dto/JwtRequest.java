package com.example.kolos.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
