package org.czekalski.userkeycloak.model;


import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Audited(targetAuditMode = NOT_AUDITED)
@Table(name = "order_addresses")
@Data
@Entity
public class OrderAddress {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    private String City;
    private String Street;

    @Column(name = "house_nr")
    private Integer houseNr;

    private Integer apartment;
    private String telephone;


    @Column(nullable =false,name="user_id")
    private String user;

    @OneToMany(mappedBy = "orderAddress")
    Set<Order> orders=new HashSet<>();
}
