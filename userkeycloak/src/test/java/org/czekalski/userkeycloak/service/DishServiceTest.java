package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.DishMapper;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.Recipe;
import org.czekalski.userkeycloak.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class DishServiceTest {
    public static final String INGREDIENT_NAME1 = "ser";
    public static final String INGREDIENT_NAME2 = "oregano";
    public static final String DISH_NAME_1 = "MargheritaCheeseX2";

    @Mock
    private  DishRepository dishRepository;

    private DishService dishService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        dishService=new DishService(new Order(),dishRepository,DishMapper.INSTANCE, IngredientMapper.INSTANCE);

    }

    @Test
    void getAllDishes() {
        Dish dish = populatingDishRepo();

        given(dishRepository.findAll()).willReturn(Arrays.asList(dish));

        List<DishCommand> returnedDishes=dishService.getAllDishesWithIngredients();

        then(dishRepository).should().findAll();
        assertThat(returnedDishes.get(0).getDescription()).contains(INGREDIENT_NAME1+" x2");
        assertThat(returnedDishes.get(0).getDescription()).contains(INGREDIENT_NAME2+" x1");
        assertThat(returnedDishes.get(0).getDescription()).contains(", ");


    }

    @Test
    void addToCart() {

        DishCommand dishCommand=new DishCommand();
        dishCommand.setName(DISH_NAME_1);
     Order returnedCart=dishService.addToCart(dishCommand);


     assertEquals(DISH_NAME_1,returnedCart.getOrderDishes().iterator().next().getDish().getName());
    }


    @Test
    void getDishById() {
        Dish dish = populatingDishRepo();

        given(dishRepository.findById(1L)).willReturn(Optional.of(dish));

        DishCommand returnedDishCommand=dishService.getDishById(1L);

        assertEquals(DISH_NAME_1,returnedDishCommand.getName());
        assertEquals(INGREDIENT_NAME1,returnedDishCommand.getIngredientCommands().stream().filter(ingredientCommand -> ingredientCommand.getName().equals(INGREDIENT_NAME1)).findFirst().get().getName());

    }

    private Dish populatingDishRepo() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("20.99"));

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.60"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.30"));


        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setDish(dish);
        recipe1.setQuantity(2);
        recipe1.setIngredient(ingredient1);

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setQuantity(1);
        recipe2.setDish(dish);
        recipe2.setIngredient(ingredient2);


        Set<Recipe> recipes = new HashSet<>();
        recipes.add(recipe1);
        recipes.add(recipe2);

        dish.setRecipes(recipes);
        return dish;
    }
}