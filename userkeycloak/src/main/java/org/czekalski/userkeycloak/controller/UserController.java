package org.czekalski.userkeycloak.controller;

import java.net.URI;
import java.util.List;

import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


    private final KeycloakRestTemplate keycloakRestTemplate;

    private String keycloakServerUrl;


    public UserController(KeycloakRestTemplate keycloakRestTemplate, @Value("${keycloak.auth-server-url}") String keycloakServerUrl) {
        this.keycloakRestTemplate = keycloakRestTemplate;
        this.keycloakServerUrl = keycloakServerUrl;
    }

    @ResponseBody
    @GetMapping("/api/users")
    public List users() {
        return keycloakRestTemplate.getForEntity(URI.create(keycloakServerUrl + "/admin/realms/restaurant/users"), List.class)
                .getBody();
    }

    @GetMapping("/index")
    public String logout(){
        return "users/logout";
    }


    @GetMapping("/logged")
    public String loggedIn(Model model){
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
        model.addAttribute("token",   details.getKeycloakSecurityContext().getToken());
        return "users/logged";
    }

}