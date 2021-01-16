package org.czekalski.userkeycloak.controller;


import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderDishController {

    private final OrderDishService orderDishService;

    public OrderDishController(OrderDishService orderDishService) {
        this.orderDishService = orderDishService;
    }


    @GetMapping("/orders/orderDish/{id}/update")
    public String    getUpdateOrderDishShoppingBag(@PathVariable Long id, Model model){
        OrderDishCommand orderDishCommand=orderDishService.getOrderDishCartById(id);
        model.addAttribute("orderDish",orderDishCommand);

        return "orders/OrderDishAndIngredientsForm";
    }

    @PostMapping("/orders/orderDish/{id}/update")
    public String    postUpdateOrderDishShoppingBag(@ModelAttribute("orderDish") OrderDishCommand orderDish){

        orderDishService.updateOrderDishCart(orderDish);

        return "redirect:/orders/summary";
    }

    @GetMapping("/orders/orderDish/{id}/delete")
   public String deleteFromCart(@PathVariable Long id){

        orderDishService.deleteFromCart(id);

        return "redirect:/orders/summary";

    }

    @GetMapping("/orders/{order.id}/details/orderDish/{orderDish.id}/delete")
    public String getDeleteOrderDishById(@PathVariable("order.id") Long orderId,@PathVariable("orderDish.id") Long orderDishId){

        orderDishService.deleteOrderDishById(orderDishId);

        return "redirect:/orders/"+orderId+"/details";

    }

}
