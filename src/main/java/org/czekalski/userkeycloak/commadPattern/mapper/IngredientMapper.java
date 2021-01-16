package org.czekalski.userkeycloak.commadPattern.mapper;


import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientMapper {

    IngredientMapper INSTANCE= Mappers.getMapper(IngredientMapper.class);



    Ingredient ingredientCommandToIngredient(IngredientCommand ingredientCommand);



    IngredientCommand ingredientToIngredientCommand(Ingredient ingredient);

}
