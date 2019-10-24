package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;

import java.util.List;

public interface IngredientService {

    List<IngredientCommand> findAll();

    IngredientCommand save(IngredientCommand ingredient);

    IngredientCommand findById(long id);

    void deleteById(long id);
}
