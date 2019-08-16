package org.czekalski.userkeycloak;



import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {



    @GetMapping("/index")
    public String index(){
        return "returned index";
    }

    @GetMapping("/logout")
    public String logout(){

        return "logged out";
    }


    @GetMapping("/")
    public String test(){

        return "helloKeyCloak";
    }

    @GetMapping("/test2")
    public String test1(@AuthenticationPrincipal Principal currentUser){
      Principal user= (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        SimpleKeycloakAccount  details = (SimpleKeycloakAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
        AccessToken accessToken= details.getKeycloakSecurityContext().getToken();

        return "test1";
    }
}
