package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.OrderIngredient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final Order shoppingCart;

    public OrderService(Order shoppingCart) {
        this.shoppingCart = shoppingCart;
    }


    public Order processShoppingCart(){
        return shoppingCart;
    }

public BigDecimal calculatePrice(){
BigDecimal price=new BigDecimal(0);

    for (OrderDish orderDish : shoppingCart.getOrderDishes())
     {
        price=  price.add(    orderDish.getSingleDishCost().multiply( new BigDecimal(orderDish.getQuantity()) ).multiply(orderDish.getPriceCut())    );

         for (OrderIngredient orderIngredient : orderDish.getOrderIngredients()) {
                price=  price.add(    orderIngredient.getIngredient().getCost().multiply(new BigDecimal(orderIngredient.getQuantity())).multiply(new BigDecimal(orderDish.getQuantity()))   );
            }


        }

shoppingCart.setTotalPrice(price);
        return price;
}




}
