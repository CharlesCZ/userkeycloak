package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.DishMapper;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.DishRepository;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.czekalski.userkeycloak.repository.RecipeRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DishServiceTest {
    public static final String INGREDIENT_NAME1 = "ser";
    public static final String INGREDIENT_NAME2 = "oregano";
    public static final String DISH_NAME_1 = "MargheritaCheeseX2";
    private static final String INGREDIENT_NAME3 = "pieczarki" ;

    @Mock
    private  DishRepository dishRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
private RecipeRepository recipeRepository;

    private  DishMapper dishMapper=DishMapper.INSTANCE;

    private DishService dishService;

    private Order shoppingBag=new Order();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        dishService=new DishService(shoppingBag,dishRepository,dishMapper, IngredientMapper.INSTANCE, ingredientRepository,recipeRepository);

    }

    @Test
    void getAllDishes() {
        Dish dish = populatingDishRepo();

        given(dishRepository.InnerJoinRecipeAll()).willReturn(Arrays.asList(dish));

        List<DishCommand> returnedDishes=dishService.getAllDishesWithIngredients();

        then(dishRepository).should().InnerJoinRecipeAll();
        assertThat(returnedDishes.get(0).getDescription()).contains(INGREDIENT_NAME1+" x2");
        assertThat(returnedDishes.get(0).getDescription()).contains(INGREDIENT_NAME2+" x1");
        assertThat(returnedDishes.get(0).getDescription()).contains(", ");


    }

    @Test
    void addToEmptyCart() {
        IngredientCommand ingredient1 = new IngredientCommand();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.60"));
        ingredient1.setQuantity(2);



       IngredientCommand ingredient3 = new IngredientCommand();
        ingredient3.setId(3L);
        ingredient3.setName(INGREDIENT_NAME3);
        ingredient3.setCost(new BigDecimal("0.30"));
        ingredient3.setQuantity(0);

        DishCommand dishCommand=new DishCommand();
        dishCommand.setName(DISH_NAME_1);
        dishCommand.setIngredientCommands(Arrays.asList(ingredient1,ingredient3));
     Order returnedCart=dishService.addToCart(dishCommand);

     //checking deleting ingredients with 0 quantitty from dishcommand
     assertThat(returnedCart.getOrderDishes()).hasSize(1);
     //can do that because there is one OrderDish
     assertEquals(DISH_NAME_1,returnedCart.getOrderDishes().iterator().next().getDish().getName());

        assertEquals(Long.valueOf(1L),returnedCart.getOrderDishes().iterator().next().getId());
    }

    @Test
    void addToCart() {
        OrderDish orderDish=new OrderDish();
        orderDish.setId(1L);
        Set<OrderDish> orderDishes=new HashSet<>();
        orderDishes.add(orderDish);
        shoppingBag.setOrderDishes(orderDishes);

        IngredientCommand ingredient1 = new IngredientCommand();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.60"));
        ingredient1.setQuantity(2);



        IngredientCommand ingredient3 = new IngredientCommand();
        ingredient3.setId(3L);
        ingredient3.setName(INGREDIENT_NAME3);
        ingredient3.setCost(new BigDecimal("0.30"));
        ingredient3.setQuantity(0);

        DishCommand dishCommand=new DishCommand();
        dishCommand.setName(DISH_NAME_1);
        dishCommand.setIngredientCommands(Arrays.asList(ingredient1,ingredient3));
        Order returnedCart=dishService.addToCart(dishCommand);

        //checking deleting ingredients with 0 quantitty from dishcommand
        assertThat(dishCommand.getIngredientCommands()).hasSize(2);
        assertNotNull(returnedCart.getOrderDishes().stream().filter(orderDish1 -> orderDish1.getId().equals(2L)).findFirst().get());


    }

    @Test
    void getDishById() {
        Dish dish = populatingDishRepo();
        populatingIngredientRepo();

        given(dishRepository.InnerJoinRecipe(1L)).willReturn(Optional.of(dish));

        DishCommand returnedDishCommand=dishService.getDishById(1L);

        assertThat(returnedDishCommand.getIngredientCommands()).hasSize(4);
        assertEquals(DISH_NAME_1,returnedDishCommand.getName());
        assertEquals(INGREDIENT_NAME1,returnedDishCommand.getIngredientCommands().stream().filter(ingredientCommand -> ingredientCommand.getName().equals(INGREDIENT_NAME1)).findFirst().get().getName());

    }

    private void populatingIngredientRepo(){


        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.60"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.30"));

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);
        ingredient3.setName("jajko");
        ingredient3.setCost(new BigDecimal("3.33"));

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setId(4L);
        ingredient4.setName("ketchup");
        ingredient4.setCost(new BigDecimal("4.44"));

        given(ingredientRepository.findAll()).willReturn(Arrays.asList(ingredient1,ingredient2,ingredient3,ingredient4));

    }
    @Test
    void getDishByIdNullPath() {
       given(dishRepository.InnerJoinRecipe(1L)).willReturn(Optional.empty());

        DishCommand returnedDishCommand=dishService.getDishById(1L);

        assertNull(returnedDishCommand);

    }

    private Dish populatingDishRepo() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("20.99"));
        dish.setSize(new BigDecimal("1"));

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


    @Test
    void saveDishCommand() {
        DishCommand dishCommand=new DishCommand();
        dishCommand.setName("name");
        dishCommand.setId(1L);
        Dish returnedDish=dishMapper.dishCommandToDish(dishCommand);
        given(dishRepository.save(any())).willReturn(returnedDish);


        DishCommand returnedDishCommand=dishService.saveDishCommand(dishCommand);


        assertEquals("name",returnedDishCommand.getName());
        assertEquals(Long.valueOf(1L),returnedDishCommand.getId());
    }


    @Test
    void findDishCommandById() {


        Dish dish=new Dish();
        dish.setId(1L);
        dish.setName("nazwa");
        given(dishRepository.findById(1L)).willReturn(Optional.of(dish));

        DishCommand returnedDishCommand=dishService.findDishCommandById(1L);

        assertEquals(Long.valueOf(1L),returnedDishCommand.getId());
        assertEquals("nazwa",returnedDishCommand.getName());


    }

    @Test
    void deleteById() {
        Long deleteById=1L;

       dishService.deleteById(deleteById);

        verify(dishRepository).deleteById(1L);
        verify( recipeRepository).deleteByDishId(1L);
        //then(dishRepository).should().deleteById(1L);
    }
}