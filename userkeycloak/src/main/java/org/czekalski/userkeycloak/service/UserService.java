package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.model.Order;
import org.keycloak.representations.AccessToken;

public interface UserService {

    Order getShoppingCart();

    AccessToken getloggedInUser();
}
