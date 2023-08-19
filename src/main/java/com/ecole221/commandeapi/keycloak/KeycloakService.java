package com.ecole221.commandeapi.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class KeycloakService {
    //realm name
    @Value("${app.keycloak.realm.name}")
    public String realmName;
    @Value("${app.keycloak.client-id}")
    public String clientId;

    private final KeycloakConfig keycloakConfig;

    public KeycloakService(KeycloakConfig keycloakConfig) {
        this.keycloakConfig = keycloakConfig;
    }

    public void addUser(UserDTO userDTO){
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        user.setEmailVerified(true);

        Keycloak keycloak = keycloakConfig.getInstance();
        UsersResource usersResource = keycloak.realm(realmName).users();

        usersResource.create(user);
        user = keycloak.realm(realmName).users().search(user.getUsername()).get(0);
        UserResource userResource =
                keycloak.realm(realmName).users().get(user.getId());
        //RoleRepresentation rp = keycloak.realm(realmName).roles().get("app_admin").toRepresentation();


        //userResource.roles().realmLevel().add(Arrays.asList(rp));

        // Get client
        ClientRepresentation app1Client = keycloak.realm(realmName).clients() //
                .findByClientId("order-app").get(0);
        for (String role: userDTO.getRoles()) {
            // Get client level role (requires view-clients role)
            RoleRepresentation userClientRole = keycloak.realm(realmName).clients()
                    .get(app1Client.getId()) //
                    .roles().get(role).toRepresentation();

            // Assign client level role to user
            userResource.roles() //
                    .clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
        }


    }

    public UserRepresentation getUser(String userName){
        UsersResource usersResource = keycloakConfig.getInstance().realm(realmName).users();
        List<UserRepresentation> users = usersResource.search(userName, true);
        return users.size()>0?users.get(0):null;
    }

    public List<UserRepresentation> getUsers(){
        Keycloak kc = keycloakConfig.getInstance();
        RealmResource realmResource = kc.realm(realmName);
        UsersResource userRessource = realmResource.users();
        System.out.println("Count: " + userRessource.count());

        //UsersResource usersResource = keycloakConfig.getInstance().realm(realmName).users();
        //List<UserRepresentation> users = usersResource.list();
        return userRessource.list();
    }
}
