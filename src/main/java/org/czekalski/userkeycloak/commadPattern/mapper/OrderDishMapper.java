package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(config=AuditBaseMapper.class)
public interface OrderDishMapper {

    OrderDishMapper INSTANCE= Mappers.getMapper(OrderDishMapper.class);


    OrderDish orderDishCommandToOrderDish(OrderDishCommand orderDishCommand);


    OrderDishCommand orderDishToOrderDishCommand(OrderDish orderDish);


}
