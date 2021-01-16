package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.*;
import org.czekalski.userkeycloak.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("application-development.properties")
class OrderMapperTest {
    public static final String INGREDIENT_NAME1 = "ser";
    public static final String INGREDIENT_NAME2 = "oregano";
    public static final String DISH_NAME_1 = "MargheritaCheeseX2";
    private static final String NAME = "Mr. Kucinski";
    private static final long ID = 1L;
    public static final String DISH_NAME3 = "schabowy";
    Timestamp finishedTime=new Timestamp(System.currentTimeMillis());
    Timestamp createdDate=new  Timestamp(System.currentTimeMillis());

    private OrderMapper orderMapper=OrderMapper.INSTANCE;

    @BeforeEach
    void setUp() {
    }


    private Order preparingOrder(){

        Order order=new Order();
        order.setId(ID);

        order.setFinishedTime(finishedTime);
        order.setCreatedBy(NAME);
        order.setCreatedDate(createdDate);

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("10.00"));



        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.50"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.20"));




        OrderDish orderDish=new OrderDish();
        orderDish.setId(1L);
        orderDish.setSingleDishCost(dish.getCost());
        orderDish.setQuantity(4);
        orderDish.setDish(dish);
        orderDish.setOrder(order);


        OrderIngredient orderIngredient1=new OrderIngredient();
        orderIngredient1.setIngredient(ingredient1);
        orderIngredient1.setIngredientDishOrderCost(ingredient1.getCost());
        orderIngredient1.setOrderDish(orderDish);
        orderIngredient1.setQuantity(2);
        orderIngredient1.setId(1L);

        OrderIngredient orderIngredient2=new OrderIngredient();
        orderIngredient2.setIngredient(ingredient2);
        orderIngredient2.setIngredientDishOrderCost(ingredient2.getCost());
        orderIngredient2.setOrderDish(orderDish);
        orderIngredient2.setQuantity(1);
        orderIngredient2.setId(2L);

        Set<OrderIngredient> orderIngredients=new HashSet<>();
        orderIngredients.add(orderIngredient1);
        orderIngredients.add(orderIngredient2);

        orderDish.setOrderIngredients(orderIngredients);


        Set<OrderDish> orderDishes=new HashSet<>();
        orderDishes.add(orderDish);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("schabowy");
        dish2.setCost(new BigDecimal("20.00"));

        OrderDish orderDish1=new OrderDish();
        orderDish1.setQuantity(1);
        orderDish1.setId(2L);
        orderDish1.setSingleDishCost(dish2.getCost());
        orderDish1.setDish(dish2);
        orderDishes.add(orderDish1);


        order.setOrderDishes(orderDishes);

        return order;


    }

    @Test
    void orderToOrderCommandTest() {
       Order order=preparingOrder();


        OrderCommand orderCommand =orderMapper.orderToOrderCommand(order);

        assertEquals(Long.valueOf(1L),orderCommand.getId());
        assertEquals(NAME,orderCommand.getCreatedBy());
        assertEquals(finishedTime,orderCommand.getFinishedTime());
        assertEquals(createdDate,order.getCreatedDate());

        //testing nested objects
        assertThat(orderCommand.getOrderDishes()).hasSize(2);
        assertThat(orderCommand.getOrderDishes().stream()
                .filter(orderDish -> orderDish.getId().equals(1L)).findFirst().get().getOrderIngredients()).hasSize(2);


       assertEquals(INGREDIENT_NAME2,orderCommand.getOrderDishes().stream()
                .filter(orderDish -> orderDish.getId().equals(1L)).findFirst().get().getOrderIngredients()
                .stream().filter(orderIngredientCommand -> orderIngredientCommand.getId().equals(2L)).findFirst().get()
                .getIngredient().getName());

    }

    private OrderCommand preparingOrdeCommand() {
        OrderCommand orderCommand=new OrderCommand();
        orderCommand.setId(ID);
        orderCommand.setFinishedTime(finishedTime);
        orderCommand.setCreatedBy(NAME);
        orderCommand.setCreatedDate(createdDate);

        DishCommand dishCommand=new DishCommand();
        dishCommand.setId(1L);
        dishCommand.setName(DISH_NAME_1);
        dishCommand.setCost(new BigDecimal("10.00"));
        dishCommand.setQuantity(4);

        DishCommand dishCommand2=new DishCommand();
        dishCommand2.setId(1L);
        dishCommand2.setName(DISH_NAME3);
        dishCommand2.setCost(new BigDecimal("10.00"));
        dishCommand2.setQuantity(7);

        IngredientCommand ingredientCommand1=new IngredientCommand();
        ingredientCommand1.setId(1L);
        ingredientCommand1.setQuantity(2);
        ingredientCommand1.setName(INGREDIENT_NAME1);
        ingredientCommand1.setCost(new BigDecimal("0.50"));

        IngredientCommand ingredientCommand2=new IngredientCommand();
        ingredientCommand2.setId(2L);
        ingredientCommand2.setQuantity(1);
        ingredientCommand2.setName(INGREDIENT_NAME2);
        ingredientCommand2.setCost(new BigDecimal("0.20"));


        OrderIngredientCommand orderIngredientCommand1=new OrderIngredientCommand();
        orderIngredientCommand1.setId(1L);
        orderIngredientCommand1.setIngredientDishOrderCost(ingredientCommand1.getCost());
        orderIngredientCommand1.setIngredient(ingredientCommand1);
        orderIngredientCommand1.setQuantity(ingredientCommand1.getQuantity());

        OrderIngredientCommand orderIngredientCommand2=new OrderIngredientCommand();
        orderIngredientCommand2.setId(2L);
        orderIngredientCommand2.setQuantity(ingredientCommand2.getQuantity());
        orderIngredientCommand2.setIngredientDishOrderCost(ingredientCommand2.getCost());
        orderIngredientCommand2.setIngredient(ingredientCommand2);

        Set<OrderIngredientCommand> orderIngredientCommands=new HashSet<>();
        orderIngredientCommands.add(orderIngredientCommand1);
        orderIngredientCommands.add(orderIngredientCommand2);

        OrderDishCommand orderDishCommand=new OrderDishCommand();
        orderDishCommand.setOrderIngredients(orderIngredientCommands);
        orderDishCommand.setDish(dishCommand);
        orderDishCommand.setQuantity(dishCommand.getQuantity());
        orderDishCommand.setSingleDishCost(dishCommand.getCost());
        orderDishCommand.setId(1L);

        OrderDishCommand orderDishCommand2=new OrderDishCommand();
        orderDishCommand2.setDish(dishCommand2);
        orderDishCommand2.setQuantity(dishCommand2.getQuantity());
        orderDishCommand2.setSingleDishCost(dishCommand2.getCost());
        orderDishCommand2.setId(2L);

        Set<OrderDishCommand> orderDishCommands=new HashSet<>();
        orderDishCommands.add(orderDishCommand);
        orderDishCommands.add(orderDishCommand2);

        orderCommand.setOrderDishes(orderDishCommands);

        return orderCommand;

    }
    @Test
    void orderCommandToOrder() {
   OrderCommand orderCommand=preparingOrdeCommand();


        Order convertedToOrder=orderMapper.orderCommandToOrder(orderCommand);

        assertEquals(Long.valueOf(1L),convertedToOrder.getId());
        assertEquals(NAME, convertedToOrder.getCreatedBy());
        assertEquals(finishedTime,convertedToOrder.getFinishedTime());
        assertEquals(createdDate,convertedToOrder.getCreatedDate());


        //testing inner objects
        assertThat(convertedToOrder.getOrderDishes()).hasSize(2);
        assertThat(convertedToOrder.getOrderDishes().stream().filter(orderDish -> orderDish.getId().equals(1L)).findFirst().get()
                .getOrderIngredients()).hasSize(2);

        assertEquals(INGREDIENT_NAME2,convertedToOrder.getOrderDishes().stream()
                .filter(orderDish -> orderDish.getId().equals(1L)).findFirst().get().getOrderIngredients()
                .stream().filter(orderIngredient -> orderIngredient.getId().equals(2L)).findFirst().get()
                .getIngredient().getName());


    }
}