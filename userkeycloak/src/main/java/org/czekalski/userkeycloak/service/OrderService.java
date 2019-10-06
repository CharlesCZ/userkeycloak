package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final Order shoppingCart;

    public OrderService(Order shoppingCart) {
        this.shoppingCart = shoppingCart;
    }


    public Order processShoppingCart(){
        return shoppingCart;
    }
}
