package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private  IngredientRepository ingredientRepository;


    private  IngredientMapper ingredientMapper=IngredientMapper.INSTANCE;

    private IngredientService ingredientService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientService=new IngredientService(ingredientRepository,ingredientMapper);
    }

    @Test
    void findAll() {
    given(ingredientRepository.findAll()).willReturn(Arrays.asList(new Ingredient(),new Ingredient()));


        List<IngredientCommand> ingredientCommands=ingredientService.findAll();


        then(ingredientRepository).should().findAll();
        assertThat(ingredientCommands).hasSize(2);
    }

    @Test
    void save() {
        IngredientCommand ingredient=new IngredientCommand();
        ingredient.setName("ser");

        Ingredient returnedIngredient=new Ingredient();
        returnedIngredient.setId(1L);
        returnedIngredient.setName("ser");
        given(ingredientRepository.save(any(Ingredient.class))).willReturn(returnedIngredient);


        IngredientCommand ingredientCommand=ingredientService.save(ingredient);


        then(ingredientRepository).should().save(any(Ingredient.class));
        assertEquals(Long.valueOf(1L),ingredientCommand.getId());
    }

    @Test
    void findById() {
        Ingredient ingredient=new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("ser");
        given(ingredientRepository.findById(1L)).willReturn(Optional.of(ingredient));


        IngredientCommand ingredientCommand=ingredientService.findById(1L);

        then(ingredientRepository).should().findById(1L);
        assertEquals("ser",ingredientCommand.getName());
    }

    @Test
    void deleteById(){


        ingredientService.deleteById(1L);


        then(ingredientRepository).should().deleteById(1L);
    }
}