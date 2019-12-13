package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.model.Order;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface OrderService {


    Order getShoppingCart();


    BigDecimal calculateTotalPrice();

    BigDecimal totalPriceForOrderDishCommand(OrderDishCommand orderDishCommand);

    OrderCommand convertedShoppingCar();


    Order addOrderToDatabase(OrderCommand orderCommand, Principal principal);

    void cleanShoppingCart();

    List<OrderCommand> getAllOrders();

    void getTotalPriceForOrderCommand(OrderCommand orderCommand);

    OrderCommand getOrderDetailsById(long id);

    OrderCommand save(OrderCommand orderCommand);


    List<OrderCommand> newOrderList();

    List<OrderCommand> newOrderList(String id);
}
