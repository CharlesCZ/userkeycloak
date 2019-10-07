package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * workaround https://github.com/spring-projects/spring-boot/issues/6514
 * **/
@ActiveProfiles("application-development.properties")
@WebMvcTest(value = OrderController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource("classpath:application-development.properties")
class OrderControllerTest {

    @Autowired
    OrderService orderService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCurrentOrderPage() throws Exception {
        mockMvc.perform(get("/orders/current"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));
    }


   /* @Test
    void getDesignPage() throws Exception {
        given(ingredientService.findAll()).willReturn(Arrays.asList(new Ingredient(), new Ingredient()));
        given(dishService.findAll()).willReturn(Arrays.asList(new Dish(), new Dish()));

        mockMvc.perform(get("/orders/design"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")));

        then(ingredientService).should().findAll();
        assertThat(ingredientService.findAll()).hasSize(2);
        then(dishService).should().findAll();
        assertThat(dishService.findAll()).hasSize(2);
    }

    @Test
    void testPostToShoppingCart() throws Exception {

        mockMvc.perform(post("/orders/addToCart")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("ingredients",)
        )

    }*/
}