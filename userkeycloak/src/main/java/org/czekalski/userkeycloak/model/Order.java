package org.czekalski.userkeycloak.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;


@Table(name = "order_table")
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"user","orderAddress","status","orderDishes"},callSuper = true)
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Order extends AuditBase {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name="finished_time")
    private Timestamp finishedTime;

    @Column(name = "payment_kind")
    private PaymentKind paymentKind;

    @Column(name = "delivery_time")
    private Timestamp deliveryTime;


   @Column(nullable =false,name="user_id")
    private String user;

    @ManyToOne
    @JoinColumn(name = "order_address_id")
    private OrderAddress orderAddress;

    private Status status;

    private Boolean payed;

    @OneToMany(mappedBy = "order")
    private Set<OrderDish> orderDishes=new HashSet<>();


}
