package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.RecipeCommand;
import org.czekalski.userkeycloak.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {

    RecipeMapper INSTANCE= Mappers.getMapper(RecipeMapper.class);

    Recipe recipeCommandToRecipe(RecipeCommand recipeCommand);

    RecipeCommand recipeCommandToRecipe(Recipe recipe);

}
