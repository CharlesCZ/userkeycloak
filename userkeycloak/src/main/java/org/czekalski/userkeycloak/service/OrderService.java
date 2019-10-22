package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderMapper;
import org.czekalski.userkeycloak.config.audit.JpaAuditingConfig;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private  Order shoppingCart;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentKindRepository paymentKindRepository;
    private final OrderDishRepository orderDishRepository;
    private final OrderIngredientRepository orderIngredientRepository;
    private final StatusRepository statusRepository;
    private final JpaAuditingConfig jpaAuditingConfig;


    public OrderService(Order shoppingCart, OrderRepository orderRepository, OrderMapper orderMapper, PaymentKindRepository paymentKindRepository, OrderDishRepository orderDishRepository, OrderIngredientRepository orderIngredientRepository, StatusRepository statusRepository, JpaAuditingConfig jpaAuditingConfig) {
        this.shoppingCart = shoppingCart;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.paymentKindRepository = paymentKindRepository;
        this.orderDishRepository = orderDishRepository;
        this.orderIngredientRepository = orderIngredientRepository;
        this.statusRepository = statusRepository;

        this.jpaAuditingConfig = jpaAuditingConfig;
    }


    public Order getShoppingCart(){
        return shoppingCart;
    }

public BigDecimal calculateTotalPrice(){
BigDecimal price=new BigDecimal(0);

    for (OrderDish orderDish : shoppingCart.getOrderDishes())
     {
        price=  price.add(    orderDish.getSingleDishCost().multiply( new BigDecimal(orderDish.getQuantity()) )    );

         for (OrderIngredient orderIngredient : orderDish.getOrderIngredients()) {
                price=  price.add(    orderIngredient.getIngredient().getCost().multiply(new BigDecimal(orderIngredient.getQuantity())).multiply(new BigDecimal(orderDish.getQuantity()))   );
            }


        }

//shoppingCart.setTotalPrice(price);
        return price;
}

    public BigDecimal totalPriceForOrderDishCommand(OrderDishCommand orderDishCommand) {
        BigDecimal price = new BigDecimal(0);

        if (orderDishCommand.getQuantity() == null || orderDishCommand.getQuantity() == 0) {
            throw new RuntimeException("orderDishQuantity:" + orderDishCommand.getQuantity());
        } else {
            price = price.add(orderDishCommand.getSingleDishCost().multiply(new BigDecimal(orderDishCommand.getQuantity())));

            for (OrderIngredientCommand orderIngredient : orderDishCommand.getOrderIngredients()) {
                price = price.add(orderIngredient.getIngredient().getCost().multiply(new BigDecimal(orderIngredient.getQuantity())).multiply(new BigDecimal(orderDishCommand.getQuantity())));
            }


            return price;
        }
    }



public OrderCommand convertedShoppingCar(){

        OrderCommand orderCommand=orderMapper.orderToOrderCommand(shoppingCart);
        orderCommand.getOrderDishes().forEach(orderDishCommand -> {
            orderDishCommand.setTotalPrice( totalPriceForOrderDishCommand(orderDishCommand) );
                });

    orderCommand.setPaymentKind(new PaymentKindCommand());
        return orderCommand;

}

    private  Order convertFromProxy(Order shoppingCart,JpaAuditingConfig jpaAuditingConfig){
//TODO better solution
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
        jpaAuditingConfig.auditorAwareBean().getCurrentAuditor().ifPresent(shoppingCartToSave::setUser);

        return shoppingCartToSave;

    }

    @Transactional
    public Order addOrderToDatabase(OrderCommand orderCommand) {

        shoppingCart.setApartment(orderCommand.getApartment());
        shoppingCart.setHouseNr(orderCommand.getHouseNr());
        shoppingCart.setStreet(orderCommand.getStreet());
        shoppingCart.setCity(orderCommand.getCity());
        shoppingCart.setDescription(orderCommand.getDescription());
        paymentKindRepository.findById(orderCommand.getPaymentKind().getId()).ifPresent(shoppingCart::setPaymentKind);
        shoppingCart.setPayed(false);
        statusRepository.findById(1L).ifPresent( shoppingCart::setStatus);


        Order shoppingCartToSave =convertFromProxy(shoppingCart,jpaAuditingConfig);

        for(Iterator<OrderDish> it = shoppingCart.getOrderDishes().iterator(); it.hasNext();){
            OrderDish orderDish=it.next();
            //removing temporary id
            orderDish.setId(null);
            orderDish.setOrder(shoppingCartToSave);
        }

        orderRepository.save(shoppingCartToSave);

        return shoppingCart;
    }


    public void cleanShoppingCart(){


        shoppingCart.setStatus(null);
        shoppingCart.setPaymentKind(null);
        shoppingCart.setCity(null);
        shoppingCart.setStreet(null);
        shoppingCart.setHouseNr(null);
        shoppingCart.setApartment(null);
        shoppingCart.setOrderDishes(new HashSet<>());
        shoppingCart.setCreatedDate(null);
        shoppingCart.setCreatedBy(null);
        shoppingCart.setFinishedTime(null);
        shoppingCart.setDescription(null);
        shoppingCart.setId(null);
        shoppingCart.setPayed(null);
        shoppingCart.setDescription(null);
        shoppingCart.setTelephone(null);
        jpaAuditingConfig.auditorAwareBean().getCurrentAuditor().ifPresent(shoppingCart::setUser);
    }


    public List<OrderCommand> getAllOrders(){

       return orderRepository.findAll().stream()
               .map(orderMapper::orderToOrderCommand).collect(Collectors.toList());
    }

    public OrderCommand getOrderDetailsById(long id) {


        return orderRepository.getOrderDetailsById(id)
                .map(orderMapper::orderToOrderCommand).orElse(null);
    }
}
