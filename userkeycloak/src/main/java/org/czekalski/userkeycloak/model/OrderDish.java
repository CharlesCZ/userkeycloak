package org.czekalski.userkeycloak.model;


import lombok.Data;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;


@Table(name = "order_dish_table")
@Data
@Entity
public class OrderDish {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;


    private Integer quantity;

    @Column(name = "single_dish_cost")
    private BigDecimal singleDishCost;

    @Column(name = "price_cut")
    private BigDecimal priceCut;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;


    @OneToMany(mappedBy = "orderDish")
    private Set<OrderIngredient> orderIngredients=new HashSet<>();

}
