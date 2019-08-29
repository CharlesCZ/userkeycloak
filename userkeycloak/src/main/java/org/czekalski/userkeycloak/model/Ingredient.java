package org.czekalski.userkeycloak.model;

import lombok.Data;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;




@Data
@Table(name = "ingredient_table")
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private BigDecimal cost;

}
