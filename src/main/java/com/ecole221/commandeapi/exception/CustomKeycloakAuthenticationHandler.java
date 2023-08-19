package com.ecole221.commandeapi.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomKeycloakAuthenticationHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"timestamp\": \"" + LocalDateTime.now() + "\", \"error\": \"" + "Unauthorized" + "\", \"status\": 401 , \"message\": \"" + "invalid token" + "\", \"path\": \"" + request.getServletPath() + "\" }");
    }
}
