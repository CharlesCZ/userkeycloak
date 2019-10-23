package org.czekalski.userkeycloak.controller;


import org.czekalski.userkeycloak.repository.OrderIngredientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderIngredientController {

private final OrderIngredientRepository orderIngredientRepository;

    public OrderIngredientController(OrderIngredientRepository orderIngredientRepository) {
        this.orderIngredientRepository = orderIngredientRepository;
    }

    @GetMapping("/orders/{order.id}/details/orderDish/{orderDish.id}/orderIngredient/{orderIngredient.id}/delete")
    public String deleteOrderIngredinet(@PathVariable("order.id") Long orderId,@PathVariable("orderIngredient.id") Long orderIngredientId){

    orderIngredientRepository.deleteById(orderIngredientId);

    return "redirect:/orders/"+orderId+"/details";
    }
}
