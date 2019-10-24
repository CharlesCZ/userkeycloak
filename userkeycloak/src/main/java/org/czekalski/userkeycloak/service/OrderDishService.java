package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.model.*;

import java.util.List;

public interface OrderDishService {

    Order getShoppingCart();

    Boolean deleteFromCart(Long id);


    Order addToCart();

    List<OrderDish> getAllPossibilities();


    void deleteOrderDishById(Long orderDishId);
}
