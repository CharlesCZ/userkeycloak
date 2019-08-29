package org.czekalski.userkeycloak.model;


import lombok.Data;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Table(name = "order_ingredient_table")
@Entity
public class OrderIngredient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "ingredient_dish_order_cost")
    private BigDecimal ingredientDishOrderCost;


    @ManyToOne
    @JoinColumn(name = "order_dish_id")
private OrderDish orderDish;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
}
