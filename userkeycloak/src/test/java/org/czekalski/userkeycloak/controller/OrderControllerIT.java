package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.service.DishService;
import org.czekalski.userkeycloak.service.IngredientService;
import org.czekalski.userkeycloak.service.OrderDishService;
import org.czekalski.userkeycloak.service.OrderService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * workaround https://github.com/spring-projects/spring-boot/issues/6514
 * **/
@ActiveProfiles("application-development.properties")
@WebMvcTest(value = OrderController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource("classpath:application-development.properties")
class OrderControllerIT {

   @MockBean
    OrderService orderService;

    @MockBean
    OrderDishService orderDishService;


    @MockBean
    IngredientService ingredientService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        reset(orderDishService);
        reset(orderService);
    }


 @Test
 void summaryOfOrder() throws Exception {
     given(orderService.convertedShoppingCar()).willReturn(new OrderCommand());

     mockMvc.perform(get("/orders/summary"))
             .andExpect(model().attributeExists("order"))
             .andExpect(view().name("orders/summary"));

     then(orderService).should().convertedShoppingCar();

 }


 /*   @Test
    void getAllIngredients() throws Exception {
        given(ingredientService.getAllIngredients()).willReturn(Arrays.asList(new IngredientCommand(),new IngredientCommand()));

        mockMvc.perform(get("/orders/dishes/2/ingredients/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredients"))
                .andExpect(view().name("orders/ingredientForm"));

        then(ingredientService).should().getAllDishesWithIngredients();
        assertThat(ingredientService.getAllDishesWithIngredients()).hasSize(2);
    }

    @Test
    void postChosenIngredients() throws Exception {
        Order order=new Order();
        OrderDish orderDish=new OrderDish();
        Dish dish=new Dish();
        dish.setId(1L);
        orderDish.setDish(dish);
        order.getOrderDishes().add(orderDish);
        given(ingredientService.addToCart()).willReturn(order);

        mockMvc.perform(get("/orders/dishes/2/ingredients/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/summary"));

        then(ingredientServie).should().addToCart();
        assertThat(ingredientService.addToCart()).hasSize(1);
        //sesja?
    }
*/
}