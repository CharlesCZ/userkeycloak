package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.czekalski.userkeycloak.model.OrderAddress;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.PaymentKind;
import org.czekalski.userkeycloak.model.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@EqualsAndHashCode(exclude = {"user","orderAddress","status","orderDishes"},callSuper = true)
@Data
public class OrderCommand extends AuditBaseCommand{


    private Long id;

    private String description;

    private Timestamp finishedTime;


    private PaymentKind paymentKind;

    private Timestamp deliveryTime;

    private String user;

    private OrderAddress orderAddress;

    private Status status;

    private Boolean payed;

    private Set<OrderDish> orderDishes=new HashSet<>();

}
