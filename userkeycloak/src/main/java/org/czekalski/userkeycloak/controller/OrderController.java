package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.service.*;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class OrderController {

    private final OrderService orderService;

    private final OrderDishService orderDishService;

    private final PaymentKindService paymentKindService;

    private final StatusService statusService;
    private final UserService userService;

    public OrderController(OrderService orderService, OrderDishService orderDishService, PaymentKindService paymentKindService, StatusService statusService, UserService userService) {
        this.orderService = orderService;
        this.orderDishService = orderDishService;
        this.paymentKindService = paymentKindService;
        this.statusService = statusService;
        this.userService = userService;
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
    public String checkout(@Valid @ModelAttribute("order") OrderCommand orderCommand,Principal principal, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {

            return "orders/addressAndPayment";
        }


        orderService.addOrderToDatabase(orderCommand,principal);
        orderService.cleanShoppingCart();
        return "orders/successView";
    }


    @GetMapping("/orders/allOrders")
    public String getAllOrders(Model model,Principal principal){

        KeycloakAuthenticationToken authToken=(KeycloakAuthenticationToken)principal;
        for (GrantedAuthority authority : authToken.getAuthorities()) {
            if(authority.getAuthority().equals("ROLE_admin")) {
                model.addAttribute("orders",orderService.getAllOrders());
                return "orders/ordersList";
            }
        }

        model.addAttribute("orders",orderService.getAllUserOrders(principal.getName()));
        return "orders/ordersList";
    }

    @GetMapping("/dashboard")
    public String getIndex(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("token", userService.getloggedInUser());



        KeycloakAuthenticationToken authToken=(KeycloakAuthenticationToken)principal;
        for (GrantedAuthority authority : authToken.getAuthorities()) {
            if(authority.getAuthority().equals("ROLE_admin")) {
                model.addAttribute("orders", orderService.getAllOrders());
                return "users/admin";
            }
        }
        model.addAttribute("orders", orderService.newOrderList(principal.getName()));
        return "users/user";

    }



    @GetMapping("/orders/{id}/details")
    public String getOrderDetails(@PathVariable Long id, Model model,Principal principal){

    OrderCommand orderCommand=orderService.getOrderDetailsById(id);

    //to pass template without error
    if(orderCommand.getFinishedTime()==null) {
        orderCommand.setFinishedTime(new Timestamp(0));
    }

        model.addAttribute("order",orderCommand);
        model.addAttribute("paymentKinds",paymentKindService.getListOfPaymentKinds());
        model.addAttribute("statuses",statusService.findAll());


        KeycloakAuthenticationToken authToken=(KeycloakAuthenticationToken)principal;
        for (GrantedAuthority authority : authToken.getAuthorities()) {
            if(authority.getAuthority().equals("ROLE_admin")) {
                model.addAttribute("orders", orderService.getAllOrders());
                return "orders/detailsForm";
            }
        }

     return "orders/detailsFormUser";
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

    @GetMapping("/newOrderListForUser")
    @ResponseBody
    public List<OrderCommand> newOrderListForUser(@RequestParam(name = "user") String user) {


        return orderService.newOrderList(user);
    }


}
