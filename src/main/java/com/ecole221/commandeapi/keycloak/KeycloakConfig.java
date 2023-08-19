package com.ecole221.commandeapi.keycloak;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Getter
public class KeycloakConfig {
    //server url
    @Value("${app.keycloak.server.url}")
    public String serverUrl;
    //realm name
    @Value("${app.keycloak.realm.name}")
    public String realmName;
    //client ID
    @Value("${app.keycloak.client-id}")
    public String clientId;
    //client sercret key
    @Value("${app.keycloak.client-secret}")
    public String clientSecret;
    //username admin keycloak
    @Value("${app.keycloak.admin.username}")
    public String username;
    //password admin keycloak
    @Value("${app.keycloak.admin.password}")
    public String password;

    private static Keycloak keycloak = null;

    public KeycloakConfig(){}

    public Keycloak getInstance(){
        if(keycloak == null){
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm("master")
                    .username(username)
                    .password(password)
                    .clientId("admin-cli")
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        }
        return keycloak;
    }



}
