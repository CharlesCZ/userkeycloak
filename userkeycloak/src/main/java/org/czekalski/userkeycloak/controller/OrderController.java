package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.czekalski.userkeycloak.service.OrderService;
import org.czekalski.userkeycloak.service.PaymentKindService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    private final OrderService orderService;

    private final OrderDishService orderDishService;

    private final PaymentKindService paymentKindService;

    public OrderController(OrderService orderService, OrderDishService orderDishService, PaymentKindService paymentKindService) {
        this.orderService = orderService;
        this.orderDishService = orderDishService;
        this.paymentKindService = paymentKindService;
    }


    @GetMapping("/orders/summary")
    public String summaryOfOrder(Model model){
        OrderCommand orderCommand= orderService.convertedShoppingCar();
        model.addAttribute("order",orderCommand);

        return "orders/summary";
    }

    @GetMapping("/orders/checkout")
    public String checkout(Model model){

        OrderCommand orderCommand= orderService.convertedShoppingCar();
        model.addAttribute("paymentKinds",paymentKindService.getListOfPaymentKinds());
        model.addAttribute("order",orderCommand);

        return "orders/addressAndPayment";
    }

    @PostMapping("/orders/checkout")
    public String checkout(OrderCommand orderCommand){

        orderService.addOrderToDatabase(orderCommand);
        orderService.cleanShoppingCart();
        return "orders/successView";
    }
}
