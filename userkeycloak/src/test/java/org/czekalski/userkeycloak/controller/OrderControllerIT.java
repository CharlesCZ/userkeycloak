package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.service.*;
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
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    PaymentKindService paymentKindService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        reset(orderDishService);
        reset(orderService);
        reset(paymentKindService);
    }


 @Test
 void summaryOfOrder() throws Exception {
     given(orderService.convertedShoppingCar()).willReturn(new OrderCommand());

     mockMvc.perform(get("/orders/summary"))
             .andExpect(model().attributeExists("order"))
             .andExpect(status().isOk())
             .andExpect(view().name("orders/summary"));

     then(orderService).should().convertedShoppingCar();
 }


    @Test
    void getCheckout() throws Exception {
        OrderCommand orderCommand=new OrderCommand();
        orderCommand.setId(1L);
        orderCommand.setPaymentKind(new PaymentKindCommand());

        given(orderService.convertedShoppingCar()).willReturn(orderCommand);

        PaymentKindCommand paymentKindCommand=new PaymentKindCommand();
        paymentKindCommand.setId(2L);
        paymentKindCommand.setName("Card");

        PaymentKindCommand paymentKindCommand1=new PaymentKindCommand();
        paymentKindCommand1.setId(1L);
        paymentKindCommand1.setName("Cash");
        given(paymentKindService.getListOfPaymentKinds()).willReturn(Arrays.asList(paymentKindCommand,paymentKindCommand1));


        mockMvc.perform(get("/orders/checkout"))
                .andExpect(status().isOk())
                    .andExpect(view().name("orders/addressAndPayment"))
                .andExpect(model().attributeExists("paymentKinds","order"));


        then(orderService).should().convertedShoppingCar();
        then(paymentKindService).should().getListOfPaymentKinds();

    }

    @Test
    void postCheckout() throws Exception {
        given(orderService.addOrderToDatabase(any(OrderCommand.class))).willReturn(new Order());


        mockMvc.perform(post("/orders/checkout"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/successView"));

        then(orderService).should().addOrderToDatabase(any(OrderCommand.class));
        then(orderService).should().cleanShoppingCart();


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