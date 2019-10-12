package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.model.OrderDish;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderIngredientCommand extends AuditBaseCommand{

    private Long id;

    private BigDecimal ingredientDishOrderCost;

  //  private OrderDishCommand orderDish;

    private IngredientCommand ingredient;

    private Integer quantity;
}
