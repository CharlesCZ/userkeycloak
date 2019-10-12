package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.OrderAddressCommand;
import org.czekalski.userkeycloak.model.OrderAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderAddressMapper {

    OrderAddressMapper INSTANCE= Mappers.getMapper(OrderAddressMapper.class);


    OrderAddress orderAddressCommandToOrderAddress(OrderAddressCommand orderAddressCommand);

    OrderAddressCommand orderAddressToOrderAddressCommand(OrderAddress orderAddress);
}
