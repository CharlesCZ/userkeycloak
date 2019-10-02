package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {



    private final IngredientRepository ingredientRepository;


    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll(){
        return ingredientRepository.findAll();
    }
}
