package org.czekalski.userkeycloak.controller;


import org.czekalski.userkeycloak.service.OrderDishService;
import org.springframework.stereotype.Controller;

@Controller
public class OrderDishController {

    private final OrderDishService orderDishService;

    public OrderDishController(OrderDishService orderDishService) {
        this.orderDishService = orderDishService;
    }


    //TODO delete OrderDish from cart

}
