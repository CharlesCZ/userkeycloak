package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.*;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.service.*;
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

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @MockBean
    StatusService statusService;

    @MockBean
    UserService userService;

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
    @WithMockUser(roles = {"admin"})
    void postCheckout() throws Exception {

        given(orderService.addOrderToDatabase(any(OrderCommand.class), any(Principal.class))).willReturn(new Order());


        mockMvc.perform(post("/orders/checkout") .with(csrf()))
                .andExpect(status().isOk())

                .andExpect(view().name("orders/successView"));

        then(orderService).should().addOrderToDatabase(any(OrderCommand.class),  any(Principal.class));
        then(orderService).should().cleanShoppingCart();


    }

    @Test
    @WithMockUser(roles = {"admin"})
    void getOrdersList() throws Exception {
        OrderCommand orderCommand = preparingOrderCommand();

        given(orderService.getAllOrders()).willReturn(Arrays.asList(orderCommand));

        mockMvc.perform(get("/orders/allOrders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("orders/ordersList"));

        then(orderService).should().getAllOrders();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void getOrderDetails() throws Exception {
        OrderCommand orderCommand = preparingOrderCommand();
        orderCommand.setId(1L);

        given(orderService.getOrderDetailsById(anyLong())).willReturn(orderCommand);

        mockMvc.perform(get("/orders/1/details"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("orders/detailsForm"));

        then(orderService).should().getOrderDetailsById(anyLong());
    }

  /*  @Test
    void postOrderDetails() throws Exception {
        OrderCommand orderCommand = preparingOrderCommand();
        orderCommand.setId(1L);

        given(orderService.updateOrder(any(OrderCommand.class))).willReturn(orderCommand);

        mockMvc.perform(post("/orders/1/details"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(view().name("redirect:/orders/allOrders"));

        then(orderService).should().updateOrder(any(OrderCommand.class));

    }*/

    private OrderCommand preparingOrderCommand() {
        OrderCommand orderCommand = new OrderCommand();
        orderCommand.setId(1L);

        StatusCommand statusCommand = new StatusCommand();
        statusCommand.setId(1L);
        statusCommand.setName("ready");
        orderCommand.setStatus(statusCommand);
        orderCommand.setPayed(false);
        orderCommand.setCreatedBy("user");
        Timestamp createdDate = new  Timestamp(System.currentTimeMillis());
        orderCommand.setCreatedDate(createdDate);
        orderCommand.setLastModifiedBy("user2");
        Timestamp lastModifiedDate = new  Timestamp(System.currentTimeMillis());
        orderCommand.setLastModifiedDate(lastModifiedDate);
        return orderCommand;
    }
}