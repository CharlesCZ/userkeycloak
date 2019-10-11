package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.mapper.DishMapper;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    public static final String INGREDIENT_NAME1 = "ser";
    public static final String INGREDIENT_NAME2 = "oregano";
    public static final String DISH_NAME_1 = "MargheritaCheeseX2";


    OrderService orderService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
   orderService=new OrderService(new Order());

    }

    private Order preparingShoppingCart() {
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


        Order order=new Order();

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
        orderIngredient2.setId(1L);

        Set<OrderIngredient> orderIngredients=new HashSet<>();
        orderIngredients.add(orderIngredient1);
        orderIngredients.add(orderIngredient2);

        orderDish.setOrderIngredients(orderIngredients);
        orderDish.setPriceCut(new BigDecimal(1));

        Set<OrderDish> orderDishes=new HashSet<>();
        orderDishes.add(orderDish);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("schabowy");
        dish2.setCost(new BigDecimal("20.00"));

        OrderDish orderDish1=new OrderDish();
        orderDish1.setPriceCut(new BigDecimal(1));
        orderDish1.setQuantity(1);
        orderDish1.setSingleDishCost(dish2.getCost());
        orderDishes.add(orderDish1);

        order.setOrderDishes(orderDishes);

        return order;
    }
    @Test
    void calculatePrice() {
       orderService=new OrderService(preparingShoppingCart());

        BigDecimal returnedPrice=orderService.calculatePrice();

        assertEquals("64.80",returnedPrice.toString());
    }
}