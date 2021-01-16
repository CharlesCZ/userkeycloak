package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.model.Order;

import java.util.List;

public interface DishService {


    List<DishCommand> getAllDishesWithIngredients();

    DishCommand getDishById(Long id);

    Order addToCart(DishCommand dishCommand);

    DishCommand saveDishCommand(DishCommand dishCommand);


    public void deleteById(long id);

    public DishCommand findDishCommandById(long id);

    DishCommand getDishByIdToEdit(Long id);


    List<DishCommand> getAllDrinks();
}
