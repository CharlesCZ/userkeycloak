package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DishMapper {

    DishMapper INSTANCE= Mappers.getMapper(DishMapper.class);

    Dish dishCommandToDish(DishCommand dishCommand);

    DishCommand dishToDishCommand(Dish dish);
}
