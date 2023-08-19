package com.ecole221.commandeapi.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
