package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderMapper;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.OrderIngredient;
import org.czekalski.userkeycloak.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final Order shoppingCart;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(Order shoppingCart, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.shoppingCart = shoppingCart;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }


    public Order getShoppingCart(){
        return shoppingCart;
    }

public BigDecimal calculateTotalPrice(){
BigDecimal price=new BigDecimal(0);

    for (OrderDish orderDish : shoppingCart.getOrderDishes())
     {
        price=  price.add(    orderDish.getSingleDishCost().multiply( new BigDecimal(orderDish.getQuantity()) ).multiply(orderDish.getPriceCut())    );

         for (OrderIngredient orderIngredient : orderDish.getOrderIngredients()) {
                price=  price.add(    orderIngredient.getIngredient().getCost().multiply(new BigDecimal(orderIngredient.getQuantity())).multiply(new BigDecimal(orderDish.getQuantity()))   );
            }


        }

//shoppingCart.setTotalPrice(price);
        return price;
}

    public BigDecimal totalPriceForOrderDishCommand(OrderDishCommand orderDishCommand){
        BigDecimal price=new BigDecimal(0);


            price=  price.add(    orderDishCommand.getSingleDishCost().multiply( new BigDecimal(orderDishCommand.getQuantity()) ).multiply(orderDishCommand.getPriceCut())    );

            for (OrderIngredientCommand orderIngredient : orderDishCommand.getOrderIngredients()) {
                price=  price.add(    orderIngredient.getIngredient().getCost().multiply(new BigDecimal(orderIngredient.getQuantity())).multiply(new BigDecimal( orderDishCommand.getQuantity()))   );
            }


        return price;
    }



public OrderCommand convertedShoppingCar(){

        OrderCommand orderCommand=orderMapper.orderToOrderCommand(shoppingCart);
        orderCommand.getOrderDishes().forEach(orderDishCommand -> {
            orderDishCommand.setTotalPrice( totalPriceForOrderDishCommand(orderDishCommand) );
                });

    orderCommand.setPaymentKind(new PaymentKindCommand());
        return orderCommand;

}




}
