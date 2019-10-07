package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.service.OrderService;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


}
