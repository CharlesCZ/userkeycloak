package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Ingredient;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
public class RecipeCommand {

    private Long id;

    private IngredientCommand ingredientCommand;

    private DishCommand dishCommand;

    private Integer quantity;

}
