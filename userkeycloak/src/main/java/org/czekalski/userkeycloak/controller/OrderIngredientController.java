package org.czekalski.userkeycloak.controller;


import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.service.OrderIngredientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderIngredientController {



private final OrderIngredientService orderIngredientService;

    public OrderIngredientController(OrderIngredientService orderIngredientService) {
        this.orderIngredientService = orderIngredientService;
    }

    @GetMapping("/orders/{order.id}/details/orderDish/{orderDish.id}/orderIngredient/{orderIngredient.id}/delete")
    public String deleteOrderIngredinet(@PathVariable("order.id") Long orderId,@PathVariable("orderIngredient.id") Long orderIngredientId){

  orderIngredientService.deleteById(orderIngredientId);


    return "redirect:/orders/"+orderId+"/details";
    }


    @GetMapping("/orders/{order.id}/details/orderDish/{orderDish.id}/orderIngredient/{orderIngredient.id}/update")
    public String updateIngredientFromOrderIngredient(@PathVariable("order.id") Long orderId,@PathVariable("orderDish.id") Long orderDishId,@PathVariable("orderIngredient.id") Long orderIngredientId, Model model){

        model.addAttribute("orderId",orderId);
        model.addAttribute("orderDishId",orderDishId);
       model.addAttribute("orderIngredient", orderIngredientService.updateOrderIngredientById(orderIngredientId));

        return "orders/orderIngredientForm";
    }


    @PostMapping("/orders/{order.id}/details/orderDish/{orderDish.id}/orderIngredient/{orderIngredient.id}/update")
    public String updateIngredientFromOrderIngredient(@PathVariable("order.id") Long orderId, @ModelAttribute("orderIngredient")OrderIngredientCommand orderIngredient){

        orderIngredientService.updateOrderIngredient(orderIngredient);

        return "redirect:/orders/"+orderId+"/details";
    }
}
