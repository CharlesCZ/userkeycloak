package org.czekalski.userkeycloak.controller;


import org.czekalski.userkeycloak.service.OrderDishService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderDishController {

    private final OrderDishService orderDishService;

    public OrderDishController(OrderDishService orderDishService) {
        this.orderDishService = orderDishService;
    }


    @GetMapping("/orders/orderDish/{id}/delete")
   public String deleteFromCart(@PathVariable Long id){

        orderDishService.deleteFromCart(id);

        return "redirect:/orders/summary";

    }

}
