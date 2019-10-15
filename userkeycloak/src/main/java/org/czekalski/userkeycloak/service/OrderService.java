package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderMapper;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderService {

    private  Order shoppingCart;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentKindRepository paymentKindRepository;
    private final OrderDishRepository orderDishRepository;
    private final OrderIngredientRepository orderIngredientRepository;
    private final StatusRepository statusRepository;

    public OrderService(Order shoppingCart, OrderRepository orderRepository, OrderMapper orderMapper, PaymentKindRepository paymentKindRepository, OrderDishRepository orderDishRepository, OrderIngredientRepository orderIngredientRepository, StatusRepository statusRepository) {
        this.shoppingCart = shoppingCart;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.paymentKindRepository = paymentKindRepository;
        this.orderDishRepository = orderDishRepository;
        this.orderIngredientRepository = orderIngredientRepository;
        this.statusRepository = statusRepository;
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


    public Order addOrderToDatabase(OrderCommand orderCommand) {

        shoppingCart.setApartment(orderCommand.getApartment());
        shoppingCart.setHouseNr(orderCommand.getHouseNr());
        shoppingCart.setStreet(orderCommand.getStreet());
        shoppingCart.setCity(orderCommand.getCity());


        paymentKindRepository.findById(orderCommand.getPaymentKind().getId()).ifPresent(shoppingCart::setPaymentKind);
        statusRepository.findById(1L).ifPresent( shoppingCart::setStatus);
//TODO saving proxy bean
Order shoppingCartToSave=new Order();
shoppingCartToSave.setStatus(shoppingCart.getStatus());
shoppingCartToSave.setPaymentKind(shoppingCart.getPaymentKind());
shoppingCartToSave.setCity(shoppingCart.getCity());
shoppingCartToSave.setStreet(shoppingCart.getStreet());
shoppingCartToSave.setHouseNr(shoppingCart.getHouseNr());
shoppingCartToSave.setApartment(shoppingCart.getApartment());
shoppingCartToSave.setOrderDishes(shoppingCart.getOrderDishes());
shoppingCartToSave.setCreatedDate(shoppingCart.getCreatedDate());
shoppingCartToSave.setCreatedBy(shoppingCart.getCreatedBy());
shoppingCartToSave.setFinishedTime(shoppingCart.getFinishedTime());
shoppingCartToSave.setDescription(shoppingCart.getDescription());
shoppingCartToSave.setId(shoppingCart.getId());
shoppingCartToSave.setUser(shoppingCart.getUser());
shoppingCartToSave.setPayed(shoppingCart.getPayed());
shoppingCartToSave.setDescription(shoppingCart.getDescription());
shoppingCartToSave.setTelephone(shoppingCart.getTelephone());
shoppingCartToSave.setUser("uzyszkodnik");// TODO needs set
       orderRepository.save(shoppingCartToSave);
       orderDishRepository.saveAll(shoppingCart.getOrderDishes());
       shoppingCart.getOrderDishes().forEach(orderDish ->{

           orderIngredientRepository.saveAll( orderDish.getOrderIngredients());
       });


        return shoppingCart;
    }

    public void cleanShoppingCart(){
        shoppingCart=new Order();
    }
}
