package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.OrderIngredient;
import org.czekalski.userkeycloak.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper
public interface DishMapper {

    DishMapper INSTANCE= Mappers.getMapper(DishMapper.class);


    Dish dishCommandToDish(DishCommand dishCommand);



    @Mapping(source = "recipes", target = "ingredientCommands",qualifiedByName = "recipesToIngredientCommands")
    DishCommand dishToDishCommand(Dish dish);

    @Named("recipesToIngredientCommands")
    default List<IngredientCommand> recipesToIngredientCommands(Set<Recipe> recipes) {
        if (recipes == null) {
            return null;
        } else {
            List<IngredientCommand> ingredientCommands = new ArrayList<>();

            for (Iterator<Recipe> it = recipes.iterator(); it.hasNext(); ) {
                IngredientMapper ingredientMapper = IngredientMapper.INSTANCE;
                Recipe recipe = it.next();
                IngredientCommand ingredientCommand = ingredientMapper.ingredientToIngredientCommand(recipe.getIngredient());
                ingredientCommand.setQuantity(recipe.getQuantity());
                ingredientCommands.add(ingredientCommand);
            }

            return ingredientCommands;
        }
    }

/*    @Named("orderDishesToOrderDishCommands")
    default List<OrderDishCommand> orderDishesToOrderDishCommands(Set<OrderDish> orderDishes){
        if(orderDishes==null){
            return null;
        }
        else {
            List<OrderDishCommand> orderDishes=new ArrayList<>();
            OrderDishMapper orderDishMapper=OrderDishMapper.INSTANCE;
            for (Iterator<OrderDish> it = orderDishes.iterator(); it.hasNext(); ) {
                OrderDish orderDishIterator = it.next();
               orderDishes.add( orderDishMapper.orderDishToOrderDishCommand(orderDishIterator));

            }

            return orderDishes;
        }

    }*/




}
