package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
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

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
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
        DishCommand dishCommand=new DishCommand();
        dishCommand.setId(2L);
        dishCommand.setSize(new BigDecimal("1"));
        given(dishService.getAllDishesWithIngredients()).willReturn(Arrays.asList( dishCommand));

        mockMvc.perform(get("/orders/dishes/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dishes"))
                .andExpect(view().name("orders/dishes"));

        then(dishService).should().getAllDishesWithIngredients();
    }

    @Test
    void getDishDetails() throws Exception {
        DishCommand dishCommand=new DishCommand();
        dishCommand.setId(2L);
        dishCommand.setSize(new BigDecimal("1"));
        given(dishService.getDishById(2L)).willReturn(dishCommand);

        mockMvc.perform(get("/orders/dishes/2/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dish"))
                .andExpect(view().name("orders/dishAndIngredientsForm"));


        then(dishService).should().getDishById(2L);
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


    @Test
    void getNewDish() throws Exception {
     mockMvc.perform(get("/dish/new"))
             .andExpect(status().isOk())
             .andExpect(model().attributeExists("dish"))
             .andExpect(view().name("dishes/dishForm"));
    }


    @Test
    void postNewDish() throws Exception {
        DishCommand dishCommand=new DishCommand();
        dishCommand.setName("name");
        dishCommand.setId(1L);

        given(dishService.saveDishCommand(any())).willReturn(dishCommand);

        mockMvc.perform(post("/dish/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("name",dishCommand.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dish/1/show"));


        then(dishService).should().saveDishCommand(any());

    }


    @Test
    void updateDish() throws Exception {
        DishCommand dishCommand=new DishCommand();
        dishCommand.setName("name");
        dishCommand.setId(1L);

        given(dishService.findDishCommandById(anyLong())).willReturn(dishCommand);

        mockMvc.perform(get("/dish/1/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dish"))
                .andExpect(view().name("dishes/dishForm"));


        then(dishService).should().findDishCommandById(anyLong());
    }


    @Test
    void deleteDish() throws Exception {

        mockMvc.perform(get("/dish/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/dishes/new"));

        then(dishService).should().deleteById(anyLong());
    }
}