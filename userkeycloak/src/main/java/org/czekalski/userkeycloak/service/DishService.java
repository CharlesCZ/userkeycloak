package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.DishMapper;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.DishRepository;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DishService {

    private final Order shoppingCart;
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;
    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;

    public DishService(Order shoppingCart, DishRepository dishRepository, DishMapper dishMapper, IngredientMapper ingredientMapper, IngredientRepository ingredientRepository) {
        this.shoppingCart = shoppingCart;
        this.dishRepository=dishRepository;
        this.dishMapper = dishMapper;
        this.ingredientMapper = ingredientMapper;
        this.ingredientRepository = ingredientRepository;
    }


    public List<DishCommand> getAllDishesWithIngredients() {
        List<Dish> dishes=dishRepository.findAll();
        List<DishCommand> dishCommands=new ArrayList<>();


        for (Dish dish : dishes) {
            DishCommand   dishCommand=dishMapper.dishToDishCommand(dish);
            dishCommand.setDescription("");

            //setting description and ingredientCommands to dishesCommand
         for(Iterator<Recipe> it = dish.getRecipes().iterator();it.hasNext();)
         {
            Recipe recipe=it.next();
           dishCommand.setDescription( dishCommand.getDescription().concat(recipe.getIngredient().getName()+" x"+recipe.getQuantity()+", ") );
            IngredientCommand ingredientCommand=ingredientMapper.ingredientToIngredientCommand( recipe.getIngredient());
            ingredientCommand.setQuantity(recipe.getQuantity());
            dishCommand.getIngredientCommands().add(ingredientCommand);
         }
         dishCommand.setDescription(dishCommand.getDescription().substring(0,dishCommand.getDescription().length()-2));
         dishCommands.add(dishCommand);


        }


        return dishCommands;
    }
public DishCommand getDishById(Long id){


   Optional<Dish> dishOptional=dishRepository.findById(id);

   if(dishOptional.isPresent()) {
       DishCommand dishCommand = dishMapper.dishToDishCommand(dishOptional.get());

       //adding ingredients from recipe
     //  addIngredientsFromRecipeToDishCommand(dishOptional, dishCommand);


       //addition ingredients with duplicates
       addAllIngredientsToDishCommand(dishCommand);


       //removing duplicates
       removingDuplicatesFromDishCommand(dishCommand);

       return dishCommand;

       }
return null;
   }

    private void addIngredientsFromRecipeToDishCommand(Optional<Dish> dishOptional, DishCommand dishCommand) {
        for (Iterator<Recipe> it = dishOptional.get().getRecipes().iterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            IngredientCommand ingredientCommand=ingredientMapper.ingredientToIngredientCommand(recipe.getIngredient());
            ingredientCommand.setQuantity(recipe.getQuantity());

             dishCommand.getIngredientCommands().add(ingredientCommand);
        }
    }

    private void addAllIngredientsToDishCommand(DishCommand dishCommand) {
        List<Ingredient> allIngredients= ingredientRepository.findAll();
        allIngredients.forEach(ingredient -> {

            IngredientCommand ingredientCommand=ingredientMapper.ingredientToIngredientCommand(ingredient);
            ingredientCommand.setQuantity(0);
            dishCommand.getIngredientCommands().add(ingredientCommand);

        });
    }

    private void removingDuplicatesFromDishCommand(DishCommand dishCommand) {
        HashSet<Long> ingredientCommandId=new HashSet<>();

        dishCommand.getIngredientCommands().forEach(ingredientCommand -> {
            if(!ingredientCommand.getQuantity().equals(0)){
                ingredientCommandId.add(ingredientCommand.getId());
            }
        });

        dishCommand.getIngredientCommands().removeIf(i -> !ingredientCommandId.add(i.getId()) && i.getQuantity().equals(0));
    }


    public Order addToCart(DishCommand dishCommand) {

        OrderDish orderDish=new OrderDish();
        orderDish.setDish(dishMapper.dishCommandToDish(dishCommand));
        orderDish.setQuantity(dishCommand.getQuantity());
        orderDish.setSingleDishCost(dishCommand.getCost());


        dishCommand.getIngredientCommands().forEach(ingredientCommand -> {

                if(!ingredientCommand.getQuantity().equals(0)) {
                    OrderIngredient orderIngredient = new OrderIngredient();
                    orderIngredient.setQuantity(ingredientCommand.getQuantity());
                    orderIngredient.setOrderDish(orderDish);
                    orderIngredient.setIngredientDishOrderCost(ingredientCommand.getCost());
                    orderIngredient.setIngredient(ingredientMapper.ingredientCommandToIngredient(ingredientCommand));
                    orderDish.getOrderIngredients().add(orderIngredient);
                }
        });

        //checking next id from ShoppingCart
        Long biggestOrderId=0L;

        if(shoppingCart.getOrderDishes()!=null)
        for(Iterator<OrderDish> it=shoppingCart.getOrderDishes().iterator();it.hasNext();){
            Long orderDishId=it.next().getId();
         //   if(orderDishId==null) {   <-never happen
        //        orderDishId = 0L;
     //       }
                      if(biggestOrderId<orderDishId)
                      {
                          biggestOrderId=orderDishId;
                      }


        }
        //adding temporary id to orderDish of Shopping Cart
        biggestOrderId+=1;
        orderDish.setId(biggestOrderId);

        shoppingCart.getOrderDishes().add(orderDish);


        return shoppingCart;
    }
}
