package org.czekalski.userkeycloak.controller;


import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
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
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @WithMockUser(roles = {"admin"})
    void getAllIngredients() throws Exception {
        given(ingredientService.findAll()).willReturn(Arrays.asList(new IngredientCommand(), new IngredientCommand()));


        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));


        then(ingredientService).should().findAll();

    }
    @Test
    @WithMockUser(roles = {"admin"})
    public void testGetIngredientNotFound() throws Exception {

        when(ingredientService.findById(anyLong())).thenThrow(NotFoundIngredientException.class);

        mockMvc.perform(get("/ingredients/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("ingredients/404view"))
                .andExpect(model().attributeExists("ex"));

    }

    @Test
    @WithMockUser(roles = {"admin"})
    void testGetNewIngredientForm() throws Exception {
        mockMvc.perform(get("/ingredients/new")).andExpect(status().isOk())
                .andExpect(view().name("ingredients/ingredientForm"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));
    }


    @Test
    @WithMockUser(roles = {"admin"})
    void testPostNewIngredientForm() throws Exception {
        IngredientCommand ingredientCommand = new  IngredientCommand();
        ingredientCommand.setId(3L);
        ingredientCommand.setName("thymeleaf");
        ingredientCommand.setCost(new BigDecimal(2.25));
        given(ingredientService.save(any())).willReturn(ingredientCommand);


        mockMvc.perform(post("/ingredients/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("name", ingredientCommand.getName())
                .param("cost", ingredientCommand.getCost().toString())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ingredients/3/show"));


        then(ingredientService).should().save(any());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void testShowIngredient() throws Exception {
        IngredientCommand ingredientCommand = new  IngredientCommand();
        ingredientCommand.setId(3L);
        ingredientCommand.setName("thymeleaf");
        ingredientCommand.setCost(new BigDecimal(2.25));
        given(ingredientService.findById(3L)).willReturn(ingredientCommand);



        mockMvc.perform(get("/ingredients/3/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingredients/show"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));


        then(ingredientService).should().findById(anyLong());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void testDeleteIngredient() throws Exception {


        mockMvc.perform(get("/ingredients/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ingredients"));


        then(ingredientService).should().deleteById(1L);
    }
}