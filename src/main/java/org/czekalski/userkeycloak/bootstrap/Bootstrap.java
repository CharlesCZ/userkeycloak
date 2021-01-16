package org.czekalski.userkeycloak.bootstrap;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Component
public class Bootstrap implements CommandLineRunner {
    public static final String INGREDIENT_NAME1 = "mozzarella";
    public static final String INGREDIENT_NAME2 = "tomato sauce";
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
private final TypeRepository typeRepository;

    public Bootstrap(OrderRepository orderRepository, DishRepository dishRepository,
                     OrderDishRepository orderDishRepository, IngredientRepository ingredientRepository,
                     OrderIngredientRepository orderIngredientRepository, PaymentKindRepository paymentKindRepository, StatusRepository statusRepository, RecipeRepository recipeRepository, TypeRepository typeRepository) {

        this.orderRepository = orderRepository;

        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
        this.ingredientRepository=ingredientRepository;
        this.orderIngredientRepository=orderIngredientRepository;
        this.paymentKindRepository = paymentKindRepository;
        this.statusRepository = statusRepository;
        this.recipeRepository = recipeRepository;
        this.typeRepository = typeRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        Ingredient ingredient1=new Ingredient();
        ingredient1.setName("cheddar");
        ingredient1.setCost(new BigDecimal("0.60"));
       ingredientRepository.save(ingredient1);

        Ingredient ingredient2=new Ingredient();
        ingredient2.setName("oregano");
        ingredient2.setCost(new BigDecimal("0.30"));
      ingredientRepository.save(ingredient2);

        Ingredient ingredient3=new Ingredient();
        ingredient3.setName("parsley");
        ingredient3.setCost(new BigDecimal("1.24"));
         ingredientRepository.save(ingredient3);


        Ingredient ingredient4=new Ingredient();
        ingredient4.setName("prawns");
        ingredient4.setCost(new BigDecimal("2.0"));
        ingredientRepository.save(ingredient4);

        loadingRecipes();


        System.out.println("###################Bootstrap Loaded############################");
    }

    private void loadingRecipes() {
        Type type1=new Type();
        type1.setId(1L);
        type1.setName("dish");
        typeRepository.save(type1);
        System.out.println("/////////////////////Loading Recipes/////////////////////");
        Dish dish1=new Dish();
        dish1.setType(type1);
        dish1.setName(DISH_NAME_1);
        dish1.setCost(new BigDecimal("15"));
        dish1.setSize(new BigDecimal("1"));
        dishRepository.save(dish1);
        Dish dish1Big=new Dish();
        dish1Big.setType(type1);
        dish1Big.setName(DISH_NAME_1);
        dish1Big.setCost(new BigDecimal("30.00"));
        dish1Big.setSize(new BigDecimal("2"));
        dishRepository.save( dish1Big);



        Ingredient ingredient1=new Ingredient();
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.60"));
        ingredientRepository.save(ingredient1);

        Ingredient ingredient2=new Ingredient();
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.40"));
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

        Recipe recipe1Big=new Recipe();
        recipe1Big.setDish(dish1Big);
        recipe1Big.setQuantity(2);
        recipe1Big.setIngredient(ingredient1);

        Recipe recipe2Big=new Recipe();
        recipe2Big.setQuantity(1);
        recipe2Big.setDish(dish1Big);
        recipe2Big.setIngredient(ingredient2);

        recipes.add(recipe1Big);
        recipes.add(recipe2Big);

        Dish dish2=new Dish();
        dish2.setType(type1);
        dish2.setName("Funghi");
        dish2.setSize(new BigDecimal("1"));
        dish2.setCost(new BigDecimal("10"));
        dishRepository.save(dish2);

        Dish dish2Big=new Dish();
        dish2Big.setType(type1);
        dish2Big.setName("Funghi");
        dish2Big.setSize(new BigDecimal("2"));
        dish2Big.setCost(new BigDecimal("20"));
        dishRepository.save(dish2Big);

        Ingredient ingredient3=new Ingredient();
        ingredient3.setName("mushrooms");
        ingredient3.setCost(new BigDecimal("1.00"));
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

        Recipe recipe3Big=new Recipe();
        recipe3Big.setDish(dish2Big);
        recipe3Big.setQuantity(1);
        recipe3Big.setIngredient(ingredient3);

        Recipe recipe4Big=new Recipe();
        recipe4Big.setDish(dish2Big);
        recipe4Big.setQuantity(1);
        recipe4Big.setIngredient(ingredient1);

        Recipe recipe5Big=new Recipe();
        recipe5Big.setDish(dish2Big);
        recipe5Big.setQuantity(1);
        recipe5Big.setIngredient(ingredient2);

        recipes.add(recipe3Big);
        recipes.add(recipe4Big);
        recipes.add(recipe5Big);

        recipeRepository.saveAll(recipes);


        //fizy drinks
        Dish drink=new Dish();
        drink.setName("Cola");
        drink.setCost(new BigDecimal("1"));
        drink.setSize(new BigDecimal("1"));
        Type type2=new Type();
        type2.setId(2L);
        type2.setName("Drink");
        typeRepository.save(type2);

        drink.setType(type2);
        dishRepository.save( drink);
        drink.setId(null);
        drink.setName("Cola");
        drink.setCost(new BigDecimal("2"));
        drink.setSize(new BigDecimal("2"));
        dishRepository.save( drink);
        drink.setId(null);
        drink.setCost(new BigDecimal("1"));
        drink.setSize(new BigDecimal("1"));
        drink.setName("Black Tea");
        dishRepository.save( drink);
        drink.setId(null);
        drink.setName("Green Tea ");
        dishRepository.save( drink);
        drink.setId(null);
        drink.setName("Water");
        drink.setCost(new BigDecimal("0.3"));
        dishRepository.save( drink);




    }
}
