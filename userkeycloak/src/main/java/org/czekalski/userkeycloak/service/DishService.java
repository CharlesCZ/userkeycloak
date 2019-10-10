package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.DishMapper;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    private final Order shoppingCart;
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;
    private final IngredientMapper ingredientMapper;

    public DishService(Order shoppingCart, DishRepository dishRepository, DishMapper dishMapper, IngredientMapper ingredientMapper) {
        this.shoppingCart = shoppingCart;
        this.dishRepository=dishRepository;
        this.dishMapper = dishMapper;
        this.ingredientMapper = ingredientMapper;
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
//addition ingredients

        return dishCommands;
    }
public DishCommand getDishById(Long id){


   Optional<Dish> dishOptional=dishRepository.findById(id);

   if(dishOptional.isPresent()) {
       DishCommand dishCommand = dishMapper.dishToDishCommand(dishOptional.get());

           for (Iterator<Recipe> it =   dishOptional.get().getRecipes().iterator(); it.hasNext(); ) {
               Recipe recipe = it.next();
               IngredientCommand ingredientCommand=ingredientMapper.ingredientToIngredientCommand(recipe.getIngredient());
               ingredientCommand.setQuantity(recipe.getQuantity());

                dishCommand.getIngredientCommands().add(ingredientCommand);
           }

return dishCommand;

       }
return null;
   }






    public Order addToCart(DishCommand dishCommand) {

        OrderDish orderDish=new OrderDish();
        orderDish.setDish(dishMapper.dishCommandToDish(dishCommand));
        orderDish.setQuantity(dishCommand.getQuantity());
        orderDish.setSingleDishCost(dishCommand.getCost());

        shoppingCart.getOrderDishes().add(orderDish);

        return shoppingCart;
    }
}
