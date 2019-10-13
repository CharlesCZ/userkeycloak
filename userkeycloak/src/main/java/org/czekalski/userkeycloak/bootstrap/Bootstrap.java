package org.czekalski.userkeycloak.bootstrap;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Component
public class Bootstrap implements CommandLineRunner {
    public static final String INGREDIENT_NAME1 = "Cheddar";
    public static final String INGREDIENT_NAME2 = "sos pomidorowy";
    public static final String DISH_NAME_1 = "MargheritaCheeseX2";

    private static final String USER1 = "Czarek11";

    private final OrderRepository orderRepository;
private final DishRepository dishRepository;
private final OrderDishRepository orderDishRepository;
private final IngredientRepository ingredientRepository;
private final OrderIngredientRepository orderIngredientRepository;
private final PaymentKindRepository paymentKindRepository;
private final StatusRepository statusRepository;
private final RecipeRepository recipeRepository;

    public Bootstrap(OrderRepository orderRepository,  DishRepository dishRepository,
                     OrderDishRepository orderDishRepository, IngredientRepository ingredientRepository,
                     OrderIngredientRepository orderIngredientRepository, PaymentKindRepository paymentKindRepository, StatusRepository statusRepository, RecipeRepository recipeRepository) {

        this.orderRepository = orderRepository;

        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
        this.ingredientRepository=ingredientRepository;
        this.orderIngredientRepository=orderIngredientRepository;
        this.paymentKindRepository = paymentKindRepository;
        this.statusRepository = statusRepository;
        this.recipeRepository = recipeRepository;
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
        order1.setUser(USER1);
        order1.setApartment(11);
        order1.setCity("Poznan");
        order1.setHouseNr(23);
        order1.setStreet("Marszalkowska");
        order1.setTelephone("123123123");
        order1.setId(orderRepository.save(order1).getId());
        orderRepository.save(order1);


        Order order2=new Order();
        order2.setDescription("nowe zamowienie");
        order2.setUser(USER1);
        order2.setFinishedTime(new Timestamp(System.currentTimeMillis()));
        order2.setPaymentKind(paymentKindRepository.findByName("Cash").get());
        order2.setStatus(statusRepository.findByName("Ready").get());
        order2.setPayed(false);
        order2.setUser(USER1);
        order2.setApartment(11);
        order2.setCity("Poznan");
        order2.setHouseNr(23);
        order2.setStreet("Marszalkowska");
        order2.setTelephone("123123123");
        order2.setId(orderRepository.save(order2).getId());
        orderRepository.save(order2);



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
        orderDish.setPriceCut(new BigDecimal(1));
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


        loadingRecipes();


        System.out.println("###################Bootstrap Loaded############################");
    }

    private void loadingRecipes() {
        System.out.println("/////////////////////Loading Recipes/////////////////////");
        Dish dish1=new Dish();
        dish1.setName(DISH_NAME_1);
        dish1.setCost(new BigDecimal("20.99"));
        dishRepository.save(dish1);

        Ingredient ingredient1=new Ingredient();
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.60"));
        ingredientRepository.save(ingredient1);

        Ingredient ingredient2=new Ingredient();
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.30"));
        ingredientRepository.save(ingredient2);

        Recipe recipe1=new Recipe();
        recipe1.setDish(dish1);
        recipe1.setQuantity(2);
        recipe1.setIngredient(ingredient1);

        Recipe recipe2=new Recipe();
        recipe2.setQuantity(1);
        recipe2.setDish(dish1);
        recipe2.setIngredient(ingredient2);


        Set<Recipe> recipes=new HashSet<>();
        recipes.add(recipe1);
        recipes.add(recipe2);



        Dish dish2=new Dish();
        dish2.setName("Funghi");
        dish2.setCost(new BigDecimal("14"));
        dishRepository.save(dish2);

        Ingredient ingredient3=new Ingredient();
        ingredient3.setName("pieczarki");
        ingredient3.setCost(new BigDecimal("1.12"));
        ingredientRepository.save(ingredient3);



        Recipe recipe3=new Recipe();
        recipe3.setDish(dish2);
        recipe3.setQuantity(1);
        recipe3.setIngredient(ingredient3);

        Recipe recipe4=new Recipe();
        recipe4.setDish(dish2);
        recipe4.setQuantity(1);
        recipe4.setIngredient(ingredient1);

        Recipe recipe5=new Recipe();
        recipe5.setDish(dish2);
        recipe5.setQuantity(1);
        recipe5.setIngredient(ingredient2);

        recipes.add(recipe3);
        recipes.add(recipe4);
        recipes.add(recipe5);


        recipeRepository.saveAll(recipes);
    }
}
