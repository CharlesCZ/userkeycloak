package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.Recipe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(exclude = {"orderDishes","ingredientCommands"})
@Data
public class DishCommand {


    private Long id;

    private  String name;
    private BigDecimal cost;

    private BigDecimal size; //small pizza is equal to 1, big is equal to size_big/size_small

    private Integer quantity;

    private String description;

    private BigDecimal totalPrice;

  //  private Set<OrderDishCommand> orderDishes =new HashSet<>();

    private List<IngredientCommand> ingredientCommands=new ArrayList<>();

    @Override
    public String toString() {
        return "DishCommand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", size=" + size +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
