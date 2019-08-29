package org.czekalski.userkeycloak.bootstrap;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.DishRepository;
import org.czekalski.userkeycloak.repository.OrderAddressRepository;
import org.czekalski.userkeycloak.repository.OrderDishRepository;
import org.czekalski.userkeycloak.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Component
public class Bootstrap implements CommandLineRunner {


    private static final String USER1 = "Czarek11";

    private final OrderRepository orderRepository;
    private final OrderAddressRepository orderAddressRepository;
private final DishRepository dishRepository;
private final OrderDishRepository orderDishRepository;
    public Bootstrap(OrderRepository orderRepository, OrderAddressRepository orderAddressRepository, DishRepository dishRepository, OrderDishRepository orderDishRepository) {

        this.orderRepository = orderRepository;


        this.orderAddressRepository = orderAddressRepository;
        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        Order order1=new Order();
        order1.setDescription("nowe zamowienie");
        order1.setUser(USER1);
        order1.setFinishedTime(new Timestamp(System.currentTimeMillis()));
        order1.setPaymentKind(PaymentKind.CARD);


        order1.setStatus(Status.READY);
        order1.setPayed(false);

        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setUser(USER1);
        orderAddress.setApartment(11);
        orderAddress.setCity("Poznan");
        orderAddress.setHouseNr(23);
        orderAddress.setStreet("Marszalkowska");
        orderAddress.setTelephone("123123123");
        orderAddressRepository.save(orderAddress);


        order1.setOrderAddress(orderAddress);
        order1.setId(orderRepository.save(order1).getId());
        orderRepository.save(order1);

        orderAddress.getOrders().add(order1);

        Dish dish=new Dish();
        dish.setName("Lasagna");
        dish.setCost(new BigDecimal("20.99"));
        dishRepository.save(dish);

        OrderDish orderDish = new OrderDish();
        orderDish.setDish(dish);
        orderDish.setOrder(order1);
        orderDish.setPriceCut(new BigDecimal("0.7"));
        orderDish.setSingleDishCost(new BigDecimal("25"));
        orderDish.setQuantity(4);
        orderDishRepository.save(orderDish);

        dish.getOrderDishes().add(orderDish);


    }
}
