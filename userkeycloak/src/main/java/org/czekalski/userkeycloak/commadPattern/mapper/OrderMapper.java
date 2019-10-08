package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config=AuditBaseMapper.class)
public interface OrderMapper {

    OrderMapper  INSTANCE= Mappers.getMapper(OrderMapper.class);

    OrderCommand orderToOrderCommand(Order order);

    Order orderCommandToOrder(OrderCommand orderCommand);
}
