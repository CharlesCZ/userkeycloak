package org.czekalski.userkeycloak.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;


@Audited(targetAuditMode = NOT_AUDITED)
@Data
@Table(name = "ingredients")
@EqualsAndHashCode(exclude = {"recipes","orderIngredients"})
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private BigDecimal cost;

   /* @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;*/

  @OneToMany(mappedBy = "ingredient",cascade = CascadeType.ALL)
  private Set<Recipe> recipes=new HashSet<>();

    @OneToMany(mappedBy = "ingredient")
    private Set<OrderIngredient> orderIngredients= new HashSet<>();




    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }
}
