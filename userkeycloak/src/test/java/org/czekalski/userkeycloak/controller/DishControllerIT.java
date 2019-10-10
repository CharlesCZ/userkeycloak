package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.service.DishService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * workaround https://github.com/spring-projects/spring-boot/issues/6514
 * **/
@ActiveProfiles("application-development.properties")
@WebMvcTest(value = DishController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource("classpath:application-development.properties")
class DishControllerIT {

    @MockBean
    DishService dishService;

    @Autowired
    MockMvc mockMvc;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        reset(dishService);
    }


    @Test
    void getAllDishes() throws Exception {
        given(dishService.getAllDishesWithIngredients()).willReturn(Arrays.asList(new DishCommand(),new DishCommand()));

        mockMvc.perform(get("/orders/dishes/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dishes"))
                .andExpect(view().name("orders/dishes"));

        then(dishService).should().getAllDishesWithIngredients();
    }

    @Test
    void postChosenDish() throws Exception {
        Order order=new Order();
        OrderDish orderDish=new OrderDish();
        Dish dish=new Dish();
        dish.setId(2L);
        orderDish.setDish(dish);
        order.getOrderDishes().add(orderDish);
        given(dishService.addToCart(any())).willReturn(order);

        mockMvc.perform(post("/orders/dishes/2/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/summary"));

        then(dishService).should().addToCart(any());


    }
}