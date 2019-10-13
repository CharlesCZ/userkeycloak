package org.czekalski.userkeycloak.commadPattern.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;



@EqualsAndHashCode(exclude = {"orderDishes"},callSuper = true)
@Data
public class OrderCommand extends AuditBaseCommand{


    private Long id;

    private String description;

    private Timestamp finishedTime;


    private PaymentKindCommand paymentKind;

    private Timestamp deliveryTime;

    private String user;

    private String City;
    private String Street;


    private Integer houseNr;

    private Integer apartment;
    private String telephone;


    private StatusCommand status;

    private Boolean payed;

    private Set<OrderDishCommand> orderDishes =new HashSet<>();

    private BigDecimal totalPrice;


    @Override
    public String toString() {
        return "OrderCommand{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", finishedTime=" + finishedTime +
                ", paymentKind=" + paymentKind +
                ", deliveryTime=" + deliveryTime +
                ", user='" + user + '\'' +
              //  ", orderAddress=" + orderAddress +
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
