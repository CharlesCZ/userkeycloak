package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.czekalski.userkeycloak.service.OrderService;
import org.czekalski.userkeycloak.service.PaymentKindService;
import org.czekalski.userkeycloak.service.StatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;

@Controller
public class OrderController {

    private final OrderService orderService;

    private final OrderDishService orderDishService;

    private final PaymentKindService paymentKindService;

    private final StatusService statusService;

    public OrderController(OrderService orderService, OrderDishService orderDishService, PaymentKindService paymentKindService, StatusService statusService) {
        this.orderService = orderService;
        this.orderDishService = orderDishService;
        this.paymentKindService = paymentKindService;
        this.statusService = statusService;
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

    OrderCommand orderCommand=orderService.getOrderDetailsById(id);

    //to pass template without error
    if(orderCommand.getFinishedTime()==null) {
        orderCommand.setFinishedTime(new Timestamp(0));
    }

        model.addAttribute("order",orderCommand);
        model.addAttribute("paymentKinds",paymentKindService.getListOfPaymentKinds());
        model.addAttribute("statuses",statusService.findAll());

        return "orders/detailsForm";
    }

    @PostMapping("/orders/{id}/details")
    public String postOrderDetails(@PathVariable Long id, @ModelAttribute("order") OrderCommand orderCommand){

        if(orderCommand.getFinishedTime().getTime()==0) {
            orderCommand.setFinishedTime(null);
        }

        OrderCommand orderCommand1=orderCommand;
        orderService.save(orderCommand);
    //    System.out.println(orderCommand1.getCreatedDate().toString());
    //    System.out.println(orderCommand1);




        return "redirect:/orders/allOrders";
    }
}
