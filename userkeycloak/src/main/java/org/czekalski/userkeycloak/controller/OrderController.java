package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.czekalski.userkeycloak.service.OrderService;
import org.czekalski.userkeycloak.service.PaymentKindService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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


    @GetMapping("/orders/allOrders")
    public String getAllOrders(Model model){

        model.addAttribute("orders",orderService.getAllOrders());

        return "orders/ordersList";
    }


    @GetMapping("/orders/{id}/details")
    public String getOrderDetails(@PathVariable Long id, Model model){


        model.addAttribute("order",orderService.getOrderDetailsById(id));

        return "orders/detailsForm";
    }

    @PostMapping("/orders/{id}/details")
    public String postOrderDetails(@PathVariable Long id, @ModelAttribute("order") OrderCommand orderCommand){

        OrderCommand orderCommand1=orderCommand;

        System.out.println(orderCommand1.getCreatedDate().toString());
        System.out.println(orderCommand1);




        return "orders/detailsForm";
    }
}
