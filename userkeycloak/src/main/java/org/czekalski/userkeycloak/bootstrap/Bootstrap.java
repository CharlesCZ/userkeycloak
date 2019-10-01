package org.czekalski.userkeycloak.bootstrap;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.*;
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
private final IngredientRepository ingredientRepository;
private final OrderIngredientRepository orderIngredientRepository;
private final PaymentKindRepository paymentKindRepository;
private final StatusRepository statusRepository;

    public Bootstrap(OrderRepository orderRepository, OrderAddressRepository orderAddressRepository, DishRepository dishRepository,
                     OrderDishRepository orderDishRepository, IngredientRepository ingredientRepository,
                     OrderIngredientRepository orderIngredientRepository, PaymentKindRepository paymentKindRepository, StatusRepository statusRepository) {

        this.orderRepository = orderRepository;
        this.orderAddressRepository = orderAddressRepository;
        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
        this.ingredientRepository=ingredientRepository;
        this.orderIngredientRepository=orderIngredientRepository;
        this.paymentKindRepository = paymentKindRepository;
        this.statusRepository = statusRepository;
    }


    @Override
    public void run(String... args) throws Exception {



        Order order1=new Order();
        order1.setDescription("nowe zamowienie");
        order1.setUser(USER1);
        order1.setFinishedTime(new Timestamp(System.currentTimeMillis()));
        order1.setPaymentKind(paymentKindRepository.findByName("Cash").get());


        order1.setStatus(statusRepository.findByName("Ready").get());
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


        Order order2=new Order();
        order2.setDescription("nowe zamowienie");
        order2.setUser(USER1);
        order2.setFinishedTime(new Timestamp(System.currentTimeMillis()));
        order2.setPaymentKind(paymentKindRepository.findByName("Cash").get());


        order2.setStatus(statusRepository.findByName("Ready").get());
        order2.setPayed(false);

        OrderAddress orderAddress2 = new OrderAddress();
        orderAddress2.setUser(USER1);
        orderAddress2.setApartment(11);
        orderAddress2.setCity("Poznan");
        orderAddress2.setHouseNr(23);
        orderAddress2.setStreet("Marszalkowska");
        orderAddress2.setTelephone("123123123");
        orderAddressRepository.save(orderAddress2);


        order2.setOrderAddress(orderAddress2);
        order2.setId(orderRepository.save(order2).getId());
        orderRepository.save(order2);





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


        Ingredient ingredient1=new Ingredient();
        ingredient1.setName("ser");
        ingredient1.setCost(new BigDecimal("0.60"));
      ingredient1=  ingredientRepository.save(ingredient1);

        Ingredient ingredient2=new Ingredient();
        ingredient2.setName("oregano");
        ingredient2.setCost(new BigDecimal("0.30"));
        ingredient2=  ingredientRepository.save(ingredient2);

        Ingredient ingredient3=new Ingredient();
        ingredient3.setName("pietruszka");
        ingredient3.setCost(new BigDecimal("99.77"));
        ingredient3=  ingredientRepository.save(ingredient3);

        OrderIngredient orderIngredient1=new OrderIngredient();
orderIngredient1.setIngredientDishOrderCost(ingredient1.getCost());
orderIngredient1.setIngredient(ingredient1);
orderIngredient1.setOrderDish(orderDish);
orderIngredient1=orderIngredientRepository.save(orderIngredient1);

orderDish.getOrderIngredients().add(orderIngredient1);

        OrderIngredient orderIngredient2=new OrderIngredient();
        orderIngredient2.setIngredientDishOrderCost(ingredient2.getCost());
        orderIngredient2.setIngredient(ingredient2);
        orderIngredient2.setOrderDish(orderDish);
        orderIngredient2=orderIngredientRepository.save(orderIngredient2);

        orderDish.getOrderIngredients().add(orderIngredient2);
        System.out.println("###################Bootstrap Loaded############################");
    }
}
