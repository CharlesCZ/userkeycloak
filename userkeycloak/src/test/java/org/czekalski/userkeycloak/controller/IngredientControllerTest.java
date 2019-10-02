package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.controller.IngredientController;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        given(ingredientService.findAll()).willReturn(Arrays.asList(new Ingredient(),new Ingredient()));


        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));


    then(ingredientService).should().findAll();
   assertThat(ingredientService.findAll()).hasSize(2);

    }



}