package org.czekalski.userkeycloak.model;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;


@Audited(targetAuditMode = NOT_AUDITED)
@Data
@Table(name = "ingredients")
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private BigDecimal cost;

}
