package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.model.OrderDish;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
public class OrderIngredientCommand extends AuditBaseCommand{

    private Long id;

    private BigDecimal ingredientDishOrderCost;

    private OrderDish orderDish;

    private Ingredient ingredient;

    private Integer quantity;
}
