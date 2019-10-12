package org.czekalski.userkeycloak.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;


@Table(name = "orders")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_kind")
    private PaymentKind paymentKind;

    @Column(name = "delivery_time")
    private Timestamp deliveryTime;


   @Column(nullable =false,name="user_id")
    private String user;

    @ManyToOne
    @JoinColumn(name = "order_address_id")
    private OrderAddress orderAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private Status status;

    private Boolean payed;

    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order")
    private Set<OrderDish> orderDishes=new HashSet<>();


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", finishedTime=" + finishedTime +
                ", paymentKind=" + paymentKind +
                ", deliveryTime=" + deliveryTime +
                ", user='" + user + '\'' +
                ", orderAddress=" + orderAddress +
                ", status=" + status +
                ", payed=" + payed +
                ", totalPrice=" + totalPrice +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
