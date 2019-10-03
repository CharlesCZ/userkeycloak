package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.controller.IngredientController;
import org.czekalski.userkeycloak.exceptions.NotFoundIngredientException;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.service.IngredientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("application-development.properties")
@WebMvcTest(value = IngredientController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource("classpath:application-development.properties")
class IngredientControllerTest {

    @MockBean
    IngredientService ingredientService;

    @Autowired
    MockMvc mockMvc;


    @AfterEach
    void tearDown() {
        reset(ingredientService);
    }

    @Test
    void getAllIngredients() throws Exception {
        given(ingredientService.findAll()).willReturn(Arrays.asList(new Ingredient(), new Ingredient()));


        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));


        then(ingredientService).should().findAll();
        assertThat(ingredientService.findAll()).hasSize(2);

    }


    @Test
    void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/ingredients/new")).andExpect(status().isOk())
                .andExpect(view().name("ingredients/ingredientForm"))
                .andExpect(model().attributeExists("ingredient"));
    }


    @Test
    void testPostNewIngredientForm() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3L);
        ingredient.setName("thymeleaf");
        ingredient.setCost(new BigDecimal(2.25));
        given(ingredientService.save(any())).willReturn(ingredient);


        mockMvc.perform(post("/ingredients/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("name", ingredient.getName())
                .param("cost", ingredient.getCost().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ingredients/3/show"));


        then(ingredientService).should().save(any());
    }

    @Test
    void testShowIngredient() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3L);
        ingredient.setName("thymeleaf");
        ingredient.setCost(new BigDecimal(2.25));
        given(ingredientService.findById(3L)).willReturn(ingredient);



        mockMvc.perform(get("/ingredients/3/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredients/show"))
                .andExpect(model().attributeExists("ingredient"));


        then(ingredientService).should().findById(anyLong());
    }


}