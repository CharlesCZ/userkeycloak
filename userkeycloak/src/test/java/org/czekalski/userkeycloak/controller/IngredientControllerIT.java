package org.czekalski.userkeycloak.controller;


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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * workaround https://github.com/spring-projects/spring-boot/issues/6514
 * **/
@ActiveProfiles("application-development.properties")
@WebMvcTest(value = IngredientController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource("classpath:application-development.properties")
class IngredientControllerIT {

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

    }
    @Test
    public void testGetIngredientNotFound() throws Exception {

        when(ingredientService.findById(anyLong())).thenThrow(NotFoundIngredientException.class);

        mockMvc.perform(get("/ingredients/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("ingredients/404view"))
                .andExpect(model().attributeExists("ex"));

    }

    @Test
    void testGetNewIngredientForm() throws Exception {
        mockMvc.perform(get("/ingredients/new")).andExpect(status().isOk())
                .andExpect(view().name("ingredients/ingredientForm"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));
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
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));


        then(ingredientService).should().findById(anyLong());
    }


}