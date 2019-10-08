package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.service.OrderDishService;
import org.czekalski.userkeycloak.service.OrderService;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    private final OrderService orderService;

    private final OrderDishService orderDishService;

    public OrderController(OrderService orderService, OrderDishService orderDishService) {
        this.orderService = orderService;
        this.orderDishService = orderDishService;
    }


}
