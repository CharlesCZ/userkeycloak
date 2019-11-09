package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void getUpdateOrderDishShoppingBag() throws Exception {
        DishCommand dishCommand=new DishCommand();
        dishCommand.setName("name");
        dishCommand.setId(1L);
        dishCommand.setSize(new BigDecimal("2"));

        OrderDishCommand orderDishCommand=new OrderDishCommand();
        orderDishCommand.setId(1L);
        orderDishCommand.setDish(dishCommand);

        given(orderDishService.getOrderDishCartById(1L)).willReturn(orderDishCommand);


        mockMvc.perform(get("/orders/orderDish/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orderDish"))
                .andExpect(view().name("orders/OrderDishAndIngredientsForm"));

       then(orderDishService).should().getOrderDishCartById(1L);
    }

    @Test
    void postUpdateOrderDishShoppingBag() throws Exception {
        given(orderDishService.updateOrderDishCart(new OrderDishCommand())).willReturn(new Order());


        mockMvc.perform(post("/orders/orderDish/1/update").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/summary"));

        then(orderDishService).should().updateOrderDishCart(any(OrderDishCommand.class));
    }



    @Test
    @WithMockUser(roles = {"admin"})
    void getDeleteOrderDishById() throws Exception {

        mockMvc.perform(get("/orders/1/details/orderDish/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/1/details"));

            verify(orderDishService).deleteOrderDishById(2L);
}
}