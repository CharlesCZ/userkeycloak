package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.Recipe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class DishCommand {


    private Long id;

    private  String name;
    private BigDecimal cost;

    private BigDecimal size; //small pizza is equal to 1, big is equal to size_big/size_small

    private Integer quantity;
    private String description;

  private List<IngredientCommand> ingredientCommands=new ArrayList<>();



}
