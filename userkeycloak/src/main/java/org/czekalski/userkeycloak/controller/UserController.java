package org.czekalski.userkeycloak.controller;

import java.net.URI;
import java.util.List;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    private KeycloakRestTemplate keycloakRestTemplate;

    private String keycloakServerUrl;


    public UserController(KeycloakRestTemplate keycloakRestTemplate, @Value("${keycloak.auth-server-url}") String keycloakServerUrl) {
        this.keycloakRestTemplate = keycloakRestTemplate;
        this.keycloakServerUrl = keycloakServerUrl;
    }

    @GetMapping("/api/users")
    public List users() {
        return keycloakRestTemplate.getForEntity(URI.create(keycloakServerUrl + "/admin/realms/restaurant/users"), List.class)
                .getBody();
    }
}