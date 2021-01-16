package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.model.OrderIngredient;

public interface OrderIngredientService {

       void deleteById(Long orderIngredientId);

       OrderIngredientCommand updateOrderIngredientById(Long orderIngredientId);

    OrderIngredient updateOrderIngredient(OrderIngredientCommand orderIngredient);
}
