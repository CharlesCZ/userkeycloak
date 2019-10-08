package org.czekalski.userkeycloak.commadPattern.command;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientCommand {

    private Long id;
    private String name;
    private BigDecimal cost;
    private Integer quantity;
}
