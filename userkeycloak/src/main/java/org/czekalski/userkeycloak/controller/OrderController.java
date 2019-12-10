package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.czekalski.userkeycloak.service.OrderService;
import org.czekalski.userkeycloak.service.PaymentKindService;
import org.czekalski.userkeycloak.service.StatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

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

    @ModelAttribute("paymentKinds")
public List<PaymentKindCommand> getListOfPayments(){
return paymentKindService.getListOfPaymentKinds();

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
        //model.addAttribute("paymentKinds",paymentKindService.getListOfPaymentKinds());
        model.addAttribute("order",orderCommand);

        return "orders/addressAndPayment";
    }

    @PostMapping("/orders/checkout")
    public String checkout(@Valid @ModelAttribute("order") OrderCommand orderCommand, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {

            return "orders/addressAndPayment";
        }


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


        orderService.save(orderCommand);


        return "redirect:/orders/allOrders";
    }

    @GetMapping("/policy")
    public String getPolicy(){

        return "policy";
    }

    @GetMapping("/newOrderList")
    @ResponseBody
    public List<OrderCommand> newOrderList() {


        return orderService.newOrderList();
    }


}
