package org.czekalski.userkeycloak.controller;



import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.repository.OrderRepository;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @Autowired
OrderRepository orderRepository;





    @GetMapping("/")
    public String test(){

        return "helloKeyCloak";
    }

    @GetMapping("/test2")
    public String test1(@AuthenticationPrincipal Principal currentUser){
      Principal user= (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount  details = (SimpleKeycloakAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
        AccessToken accessToken= details.getKeycloakSecurityContext().getToken();


        Order order1=new Order();
        order1.setDescription("nowe zamowienie2");
        order1.setId(orderRepository.save(order1).getId());
        orderRepository.save(order1);
        return "test1";
    }
}
