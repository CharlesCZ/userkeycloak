package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.exceptions.NotFoundIngredientException;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {



    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;


    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public List<IngredientCommand> findAll(){
        return ingredientRepository.findAll().stream()
                .map(ingredient -> ingredientMapper.ingredientToIngredientCommand(ingredient)).collect(Collectors.toList());
    }

    public IngredientCommand save(IngredientCommand ingredient) {

    return     ingredientMapper.ingredientToIngredientCommand(
            ingredientRepository.save(ingredientMapper.ingredientCommandToIngredient(ingredient)));
    }

    public IngredientCommand findById(long id) {
        Optional<Ingredient> returnedIngredient= ingredientRepository.findById(id);
        if(!returnedIngredient.isPresent()) {
            throw new NotFoundIngredientException("No such ingredient in database");
        }
        return ingredientMapper.ingredientToIngredientCommand(returnedIngredient.get());
    }


    public void deleteById(long id){
        ingredientRepository.deleteById(id);
    }
}
