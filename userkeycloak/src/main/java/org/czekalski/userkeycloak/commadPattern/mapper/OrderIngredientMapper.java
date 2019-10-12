package org.czekalski.userkeycloak.commadPattern.mapper;


import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.model.OrderIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config=AuditBaseMapper.class)
public interface OrderIngredientMapper {

    OrderIngredientMapper INSTANCE= Mappers.getMapper(OrderIngredientMapper.class);

    OrderIngredient orderIngredientCommandToOrderIngredient(OrderIngredientCommand orderIngredientCommand);

    OrderIngredientCommand orderIngredientToOrderIngredientCommand(OrderIngredient orderIngredient);

}
