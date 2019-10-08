package org.czekalski.userkeycloak.model;


import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/*@Audited(targetAuditMode = NOT_AUDITED)
@Table(name = "categories")
@Data
@Entity
public class Category {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private  String name;

    @OneToMany(mappedBy = "category")
    private Set<Dish> dishes=new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Ingredient> ingredients=new HashSet<>();
}
*/