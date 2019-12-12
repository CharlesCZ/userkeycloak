package org.czekalski.userkeycloak.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.czekalski.userkeycloak.service.UserService;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


    private final KeycloakRestTemplate keycloakRestTemplate;

    private String keycloakServerUrl;

    private final UserService userService;

    public UserController(KeycloakRestTemplate keycloakRestTemplate, @Value("${keycloak.auth-server-url}") String keycloakServerUrl, UserService userService) {
        this.keycloakRestTemplate = keycloakRestTemplate;
        this.keycloakServerUrl = keycloakServerUrl;
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/api/users")
    public List users() {
        return keycloakRestTemplate.getForEntity(URI.create(keycloakServerUrl + "/admin/realms/restaurant/users"), List.class)
                .getBody();
    }

    @GetMapping("/logout/index")
    public String logout(){
        return "users/logout";
    }


    @GetMapping("/logged")
    public String loggedIn(Model model){
        model.addAttribute("token",   userService.getloggedInUser());
        return "users/logged";
    }





    @GetMapping({"/login"})
    public String getlogin(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("token", userService.getloggedInUser());

        return "login";
    }


    @GetMapping({"/charts"})
    public String getCharts(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("token", userService.getloggedInUser());


        return "users/charts";
    }

    @GetMapping({"/blank"})
    public String getBlank(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("token", userService.getloggedInUser());


        return "users/blank";
    }

    @GetMapping({"/tables"})
    public String getTables(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("token", userService.getloggedInUser());

        return "users/tables";
    }



    @GetMapping({"/register"})
    public String getreg(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("token", userService.getloggedInUser());

        return "users/register";
    }



}