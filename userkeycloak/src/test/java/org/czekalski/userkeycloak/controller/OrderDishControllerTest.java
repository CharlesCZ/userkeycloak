package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("application-development.properties")
@WebMvcTest(value = OrderDishController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource("classpath:application-development.properties")
class OrderDishControllerTest {



    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderDishService orderDishService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        reset(orderDishService);
    }


    @Test
    void deleteOrderDishFromShoppingBag() throws Exception {
      given(orderDishService.deleteFromCart(1L)).willReturn(true);


        mockMvc.perform(get("/orders/orderDish/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/summary"));

        verify(orderDishService).deleteFromCart(anyLong());
    }
}