package org.czekalski.userkeycloak.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Audited(targetAuditMode = NOT_AUDITED)
@Table(name = "dishes")
@Data
@EqualsAndHashCode(exclude = {"orderDishes","recipes"})
@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private  String name;


    private BigDecimal cost;

        private BigDecimal size; //small pizza is equal to 1, big is equal to size_big/size_small

    @OneToMany(mappedBy = "dish")
    private Set<OrderDish> orderDishes= new HashSet<>();


   /* @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;*/

    @OneToMany(mappedBy = "dish",cascade = CascadeType.ALL)
    private Set<Recipe> recipes=new HashSet<>();


    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", size=" + size +
                '}';
    }
}
