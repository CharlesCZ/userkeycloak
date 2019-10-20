package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.*;
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
import java.util.Date;

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

    @Test
    void getOrdersList() throws Exception {
        OrderCommand orderCommand=new OrderCommand();
        orderCommand.setId(1L);

        StatusCommand statusCommand=new StatusCommand();
        statusCommand.setId(1L);
        statusCommand.setName("ready");
        orderCommand.setStatus(statusCommand);
        orderCommand.setPayed(false);
        orderCommand.setCreatedBy("user");
        orderCommand.setCreatedDate(new Date(System.currentTimeMillis()));

        given(orderService.getAllOrders()).willReturn(Arrays.asList(orderCommand));

        mockMvc.perform(get("/orders/allOrders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("orders/ordersList"));

        then(orderService).should().getAllOrders();
    }
}