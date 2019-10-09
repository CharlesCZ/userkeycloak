package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.czekalski.userkeycloak.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderDishServiceTest {

    public static final String INGREDIENT_NAME1 = "ser";
    public static final String INGREDIENT_NAME2 = "oregano";
    public static final String DISH_NAME_1 = "MargheritaCheeseX2";
    public static final String DISH_NAME_2 = "Margherita";
    @Mock
    private  Order shoppingCart;
    @Mock
    private  RecipeRepository recipeRepository;

    @Mock
private IngredientRepository ingredientRepository;
    OrderDishService orderDishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        orderDishService=new OrderDishService(shoppingCart,recipeRepository, ingredientRepository);
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
        recipePart1.setIngredientQuantity(2);

        Recipe recipePart2=new Recipe();
        recipePart2.setId(2L);
        recipePart2.setDish(dish);
        recipePart2.setIngredient(ingredient2);
        recipePart2.setIngredientQuantity(1);


        Dish dish2=new Dish();
        dish2.setId(2L);
        dish2.setName(DISH_NAME_2);
        dish2.setCost(new BigDecimal("10.00"));


        Recipe recipeSimplePart1=new Recipe();
        recipeSimplePart1.setId(3L);
        recipeSimplePart1.setDish(dish2);
        recipeSimplePart1.setIngredient(ingredient1);
        recipeSimplePart1.setIngredientQuantity(1);

        Recipe  recipeSimplePart2=new Recipe();
        recipeSimplePart2.setId(4L);
        recipeSimplePart2.setDish(dish2);
        recipeSimplePart2.setIngredient(ingredient2);
        recipeSimplePart2.setIngredientQuantity(1);


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
    void addToCart() {
    }
}