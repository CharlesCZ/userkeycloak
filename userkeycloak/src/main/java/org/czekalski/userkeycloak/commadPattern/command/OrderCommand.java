package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.czekalski.userkeycloak.model.OrderAddress;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.PaymentKind;
import org.czekalski.userkeycloak.model.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@EqualsAndHashCode(exclude = {"paymentKindCommand","user","orderAddressCommand","statusCommand","orderDishCommands"},callSuper = true)
@Data
public class OrderCommand extends AuditBaseCommand{


    private Long id;

    private String description;

    private Timestamp finishedTime;


    private PaymentKindCommand paymentKindCommand;

    private Timestamp deliveryTime;

    private String user;

    private OrderAddressCommand orderAddressCommand;

    private StatusCommand statusCommand;

    private Boolean payed;

    private List<OrderDishCommand> orderDishCommands=new ArrayList<>();

}
