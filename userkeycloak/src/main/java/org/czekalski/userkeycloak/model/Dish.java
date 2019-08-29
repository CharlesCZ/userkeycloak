package org.czekalski.userkeycloak.model;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Audited(targetAuditMode = NOT_AUDITED)
@Table(name = "dish_table")
@Data
@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private  String name;


    private BigDecimal cost;


    @OneToMany(mappedBy = "dish")
    private Set<OrderDish> orderDishes= new HashSet<>();

}
