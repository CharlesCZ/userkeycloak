package org.czekalski.userkeycloak.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;




@Data
@Table(name = "order_ingredients")
@Entity
@EqualsAndHashCode(exclude = {"ingredient","orderDish"},callSuper = true)
@Audited
@EntityListeners(AuditingEntityListener.class)
public class OrderIngredient extends AuditBase{
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

    private Integer quantity;


    @Override
    public String toString() {
        return "OrderIngredient{" +
                "id=" + id +
                ", ingredientDishOrderCost=" + ingredientDishOrderCost +
              //  ", orderDish=" + orderDish +
                ", ingredient=" + ingredient +
                ", quantity=" + quantity +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
