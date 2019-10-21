package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.Set;

@Mapper(config=AuditBaseMapper.class)
public interface OrderMapper {

    OrderMapper  INSTANCE= Mappers.getMapper(OrderMapper.class);


    OrderCommand orderToOrderCommand(Order order);


    Order orderCommandToOrder(OrderCommand orderCommand);

  /*  @Named("finishedTimeToFinishedTimeCommand")
    default String finishedTimeToFinishedTimeCommand(Timestamp finishedTime){
        if(finishedTime==null){
            return null;
        }else{
            String finishedTimeCommand=finishedTime.toString();
            return finishedTimeCommand;

        }
    }


    @Named("finishedTimeCommandToFinishedTime")
    default Timestamp finishedTimeCommandToFinishedTime(String finishedTimeCommand)
    {
        if(finishedTimeCommand==null){
            return null;
        }else {

            Timestamp timestamp=new Timestamp(finishedTimeCommand);

        }

    }*/

    /*  @Named("orderDishToOrderDishCommand")
    default Set<OrderDishCommand> orderDishToOrderDishCommand(Set<OrderDish> orderDishes){
        if(orderDishes==null){
            return null;
        }else{
            OrderDishMapper orderDishMapper=OrderDishMapper.INSTANCE;


        }


    }*/
}
