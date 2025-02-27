package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderDishMapper;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.czekalski.userkeycloak.repository.OrderDishRepository;
import org.czekalski.userkeycloak.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.ap.internal.util.Collections;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class OrderDishServiceTest {

    private static final String INGREDIENT_NAME1 = "ser";
    private static final String INGREDIENT_NAME2 = "oregano";
    private static final String DISH_NAME_1 = "MargheritaCheeseX2";
    private static final String DISH_NAME_2 = "Margherita";

    private  Order shoppingCart=new Order();
    @Mock
    private  RecipeRepository recipeRepository;

    @Mock
private IngredientRepository ingredientRepository;

    @Mock
    private OrderDishRepository orderDishRepository;

    OrderDishService orderDishService;

    OrderDishMapper orderDishMapper=OrderDishMapper.INSTANCE;

    IngredientMapper ingredientMapper=IngredientMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        orderDishService=new OrderDishServiceImpl(shoppingCart,recipeRepository, ingredientRepository, orderDishRepository, orderDishMapper, ingredientMapper);
    }

    @Test
    void getAllPossibilities() {
        //given
        populatingRepository();



         List<OrderDish> returnedOrderDishes= orderDishService.getAllPossibilities();

         //doubled object removed?
        assertThat(returnedOrderDishes.get(0).getOrderIngredients()).hasSize(4);
        assertThat(returnedOrderDishes.get(1).getOrderIngredients()).hasSize(4);


assertAll("Testing conversion from Recipes to OrderDishes",
        ()->assertAll("First recipe",
                ()->assertEquals(DISH_NAME_1,returnedOrderDishes.get(0).getDish().getName(),"Name of first dish"),
                ()->assertEquals(INGREDIENT_NAME1,
                        returnedOrderDishes.get(0).getOrderIngredients().stream()
                                .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME1)).findFirst().get().getIngredient().getName(),
                        "Name of first ingredient"),
                ()->assertEquals(Integer.valueOf(2),      returnedOrderDishes.get(0).getOrderIngredients().stream()
                                .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME1)).findFirst().get().getQuantity(),
                        "Quantity of first ingredient"),
                ()->assertEquals(INGREDIENT_NAME2,    returnedOrderDishes.get(0).getOrderIngredients().stream()
                        .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME2)).findFirst().get().getIngredient().getName(),
                        "Name of second ingredient"),
                ()->assertEquals(Integer.valueOf(1),
                        returnedOrderDishes.get(0).getOrderIngredients().stream()
                        .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME2)).findFirst().get().getQuantity(),
                        "Quantity of second ingredient")  ),
        ()->assertAll("Second recipe",
                ()->assertEquals(DISH_NAME_2,returnedOrderDishes.get(1).getDish().getName(),"Name of first dish"),
                ()->assertEquals(INGREDIENT_NAME1,
                        returnedOrderDishes.get(1).getOrderIngredients().stream()
                        .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME1)).findFirst().get().getIngredient().getName(),
                        "Name of first ingredient"),
                ()->assertEquals(Integer.valueOf(1),
                        returnedOrderDishes.get(1).getOrderIngredients().stream()
                                .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME1)).findFirst().get().getQuantity(),
                        "Quantity of first ingredient"),
                ()->assertEquals(INGREDIENT_NAME2,
                        returnedOrderDishes.get(1).getOrderIngredients().stream()
                        .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME2)).findFirst().get().getIngredient().getName(),
                        "Name of second ingredient"),
                ()->assertEquals(Integer.valueOf(1),
                        returnedOrderDishes.get(1).getOrderIngredients().stream()
                        .filter(orderIngredient -> orderIngredient.getIngredient().getName().equals(INGREDIENT_NAME2)).findFirst().get().getQuantity(),
                        "Quantity of second ingredient")  )
        );



    }

    private void populatingRepository() {
        Dish dish=new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("20.99"));

        Ingredient ingredient1=new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.60"));

        Ingredient ingredient2=new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.30"));


        Recipe recipePart1=new Recipe();
        recipePart1.setId(1L);
        recipePart1.setDish(dish);
        recipePart1.setIngredient(ingredient1);
        recipePart1.setQuantity(2);

        Recipe recipePart2=new Recipe();
        recipePart2.setId(2L);
        recipePart2.setDish(dish);
        recipePart2.setIngredient(ingredient2);
        recipePart2.setQuantity(1);


        Dish dish2=new Dish();
        dish2.setId(2L);
        dish2.setName(DISH_NAME_2);
        dish2.setCost(new BigDecimal("10.00"));


        Recipe recipeSimplePart1=new Recipe();
        recipeSimplePart1.setId(3L);
        recipeSimplePart1.setDish(dish2);
        recipeSimplePart1.setIngredient(ingredient1);
        recipeSimplePart1.setQuantity(1);

        Recipe  recipeSimplePart2=new Recipe();
        recipeSimplePart2.setId(4L);
        recipeSimplePart2.setDish(dish2);
        recipeSimplePart2.setIngredient(ingredient2);
        recipeSimplePart2.setQuantity(1);


        given(recipeRepository.findAll()).willReturn(Arrays.asList(recipePart1,recipePart2,recipeSimplePart1,recipeSimplePart2));

        Ingredient notInRecipe1= new Ingredient();
        notInRecipe1.setId(3L);
        notInRecipe1.setCost(new BigDecimal("33.33"));
        notInRecipe1.setName("Krakersy");

        Ingredient notInRecipe2= new Ingredient();
        notInRecipe2.setId(4L);
        notInRecipe2.setCost(new BigDecimal("44.44"));
        notInRecipe2.setName("Ketchup");



        given(ingredientRepository.findAll()).willReturn(Arrays.asList(ingredient2,notInRecipe1,notInRecipe2,ingredient1,ingredient2));

    }


    @Test
    void deleteFromCart() {
        OrderDish orderDish=new OrderDish();
        orderDish.setId(1L);
        OrderDish orderDish2=new OrderDish();
        orderDish2.setId(2L);
        OrderDish orderDish3=new OrderDish();
        orderDish3.setId(3L);
        shoppingCart.getOrderDishes().add(orderDish);
        shoppingCart.getOrderDishes().add(orderDish2);
        shoppingCart.getOrderDishes().add(orderDish3);



        assertTrue( orderDishService.deleteFromCart(2L));
        assertThat(shoppingCart.getOrderDishes()).hasSize(2);
        assertNotNull(shoppingCart.getOrderDishes().stream().filter(orderDish1 -> orderDish1.getId().equals(1L)).findFirst().get());




    }
    @Test
    void deleteOrderDishById(){

        orderDishService.deleteOrderDishById(1L);

        then(orderDishRepository).should().deleteById(1L);
    }


    @Test
    void addToCart() {


    }
   // given(orderDishService.getOrderDishCartById(1L)).willReturn(new OrderDishCommand());

    @Test
    void getOrderDishCartById() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("20.99"));
        dish.setSize(new BigDecimal("1"));


        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("1.2"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.30"));


        Order order=new Order();
        order.setId(1L);

        OrderDish orderDish=new OrderDish();
        orderDish.setId(1L);
        orderDish.setQuantity(4);
        orderDish.setDish(dish);

        orderDish.setSingleDishCost(new BigDecimal("2.0"));
        orderDish.setOrder(order);

        OrderIngredient orderIngredient1=new OrderIngredient();
        orderIngredient1.setIngredient(ingredient1);
        orderIngredient1.setOrderDish(orderDish);
        orderIngredient1.setQuantity(2);
        orderIngredient1.setId(1L);
        orderIngredient1.setIngredientDishOrderCost(new BigDecimal("4.0"));
        orderDish.getOrderIngredients().add(orderIngredient1);
        order.getOrderDishes().add(orderDish);


        orderDishService=new OrderDishServiceImpl(order,recipeRepository, ingredientRepository, orderDishRepository, orderDishMapper, ingredientMapper);

        given(ingredientRepository.findAll()).willReturn(Arrays.asList( ingredient2));

        OrderDishCommand orderDishCommand=orderDishService.getOrderDishCartById(1L);


       then(ingredientRepository).should().findAll();
        assertEquals(Long.valueOf(1L),orderDishCommand.getId());
        assertThat(orderDishCommand.getDish().getIngredientCommands()).hasSize(2);
    }


    @Test
    void updateOrderDishCart() {
        DishCommand dishCommand=new DishCommand();
        dishCommand.setName("name");
        dishCommand.setId(1L);
        dishCommand.setQuantity(4);

        IngredientCommand ingredientCommand1 = new IngredientCommand();
        ingredientCommand1.setId(1L);
        ingredientCommand1.setName(INGREDIENT_NAME1);
        ingredientCommand1.setCost(new BigDecimal("0.60"));
        ingredientCommand1.setQuantity(2);
        dishCommand.getIngredientCommands().add(ingredientCommand1);

        OrderDishCommand  orderDish=new OrderDishCommand();
        orderDish.setId(1L);
        orderDish.setDish(dishCommand);
        Order order=new Order();
        order.setId(1L);

        OrderDish orderDishshop=new OrderDish();
        orderDishshop.setId(1L);
        orderDishshop.setQuantity(0);
        order.setOrderDishes(Collections.asSet(orderDishshop));


        Order returendShopping =orderDishService.updateOrderDishCart(orderDish);


        assertEquals(Integer.valueOf(4),shoppingCart.getOrderDishes().iterator().next().getQuantity());


    }


}