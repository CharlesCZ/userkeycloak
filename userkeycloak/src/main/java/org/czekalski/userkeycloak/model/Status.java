package org.czekalski.userkeycloak.model;


import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;


@Table(name = "statuses")
@Audited(targetAuditMode = NOT_AUDITED)
@Data
@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
