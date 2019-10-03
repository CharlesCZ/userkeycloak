package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.exceptions.NotFoundIngredientException;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {



    private final IngredientRepository ingredientRepository;


    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll(){
        return ingredientRepository.findAll();
    }

    public Ingredient save(Ingredient ingredient) {
    return     ingredientRepository.save(ingredient);
    }

    public Ingredient findById(long id) {
        Optional<Ingredient> returnedIngredient= ingredientRepository.findById(id);
        if(!returnedIngredient.isPresent()) {
            throw new NotFoundIngredientException("No such ingredient in database");
        }
        return returnedIngredient.get();
    }

}
