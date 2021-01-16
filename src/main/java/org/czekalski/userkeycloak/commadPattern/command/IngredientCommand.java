package org.czekalski.userkeycloak.commadPattern.command;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.model.Recipe;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@EqualsAndHashCode(exclude = {"dishCommands"})
@Data
public class IngredientCommand {

    private Long id;
    private String name;
    private BigDecimal cost;

    @NotNull
    @Min(0)
    @Max(3)
    private Integer quantity;

    private List<DishCommand> dishCommands=new ArrayList<>();

  //  private Set<OrderIngredientCommand> orderIngredients =new HashSet<>();

  private Set<RecipeCommand> recipeCommands=new HashSet<>();

    @Override
    public String toString() {
        return "IngredientCommand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", quantity=" + quantity +
                '}';
    }
}
