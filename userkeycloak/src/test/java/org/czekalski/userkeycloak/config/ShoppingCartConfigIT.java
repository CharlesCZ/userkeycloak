package org.czekalski.userkeycloak.config;

import org.czekalski.userkeycloak.controller.IngredientController;
import org.czekalski.userkeycloak.controller.TestSecurityConfig;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitWebConfig(classes = {OrderService.class,ShoppingCartConfig.class})
@TestPropertySource("classpath:application-development.properties")
class ShoppingCartConfigIT {

    @Autowired
    MockHttpSession session;

    @Autowired
    OrderService orderService;


    @Test
    void testShoppingCartBeanScope() {


        orderService.processShoppingCart().setId(1111L);
        System.out.println(session.getAttribute("scopedTarget.ShoppingCart"));
         //   System.out.println(session.getAttributeNames().nextElement());
          //  System.out.println(session.getValueNames()[0]);
            Order sessionOrder= (Order) session.getAttribute("scopedTarget.ShoppingCart");
            assertEquals(Long.valueOf(1111L),sessionOrder.getId());
    }
}