package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EqualsAndHashCode(exclude = {"orderIngredients"},callSuper = true)
@Data
public class OrderDishCommand  extends AuditBaseCommand{

    private Long id;

    private Integer quantity;

    private BigDecimal singleDishCost;

    private BigDecimal priceCut;


    private DishCommand dish;

    private Set<OrderIngredientCommand> orderIngredients =new HashSet<>();


    @Override
    public String toString() {
        return "OrderDishCommand{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", singleDishCost=" + singleDishCost +
                ", priceCut=" + priceCut +
            //    ", order=" + order +
            //    ", dish=" + dish +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
