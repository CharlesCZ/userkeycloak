package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.model.Order;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private  Order shoppingCart;

    public UserService(Order shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Order getShoppingCart(){
        return shoppingCart;
    }

    public AccessToken getloggedInUser(){
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();

         return details.getKeycloakSecurityContext().getToken();
    }
}
