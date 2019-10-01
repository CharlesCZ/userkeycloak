package org.czekalski.userkeycloak.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;



@Table(name = "order_dishes")
@Data
@EqualsAndHashCode(exclude = {"order","dish","orderIngredients"},callSuper = true)
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class OrderDish  extends AuditBase{

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
