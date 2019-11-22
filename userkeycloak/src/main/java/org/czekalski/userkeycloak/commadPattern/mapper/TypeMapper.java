package org.czekalski.userkeycloak.commadPattern.mapper;


import org.czekalski.userkeycloak.commadPattern.command.TypeCommand;
import org.czekalski.userkeycloak.model.Type;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TypeMapper {

    TypeMapper INSTANCE= Mappers.getMapper(TypeMapper.class);

    Type typeCommandToType(TypeCommand typeCommand);

    TypeCommand typeToTypeCommand(Type type);
}
