package org.czekalski.userkeycloak.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Audited(targetAuditMode = NOT_AUDITED)
@Table(name = "recipes")
@Data
@EqualsAndHashCode(exclude = {"ingredient","dish"})
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;


    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Integer quantity;



}
