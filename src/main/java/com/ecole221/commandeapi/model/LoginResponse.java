package com.ecole221.commandeapi.model;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String refresh_expires_in;
    private String token_type;
}
