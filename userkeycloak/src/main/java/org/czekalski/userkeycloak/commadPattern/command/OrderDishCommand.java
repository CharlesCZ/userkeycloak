package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(exclude = {"dishCommand","orderCommand","orderIngredientCommands"},callSuper = true)
@Data
public class OrderDishCommand  extends AuditBaseCommand{


    private Long id;


    private Integer quantity;


    private BigDecimal singleDishCost;


    private BigDecimal priceCut;



    private OrderCommand orderCommand;



    private DishCommand dishCommand;



    private List<OrderIngredientCommand> orderIngredientCommands=new ArrayList<>();
}
