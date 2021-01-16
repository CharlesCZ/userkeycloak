package org.czekalski.userkeycloak.config;

import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.service.UserService;
import org.czekalski.userkeycloak.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitWebConfig(classes = {UserServiceImpl.class, ShoppingCartConfig.class})
@TestPropertySource("classpath:application-development.properties")
class ShoppingCartConfigIT {

    @Autowired
    MockHttpSession session;



    @Autowired
    UserService userService;


    @Test
    void testShoppingCartBeanScope() {


       userService.getShoppingCart().setId(1111L);
        System.out.println(session.getAttribute("scopedTarget.ShoppingCart"));
         //   System.out.println(session.getAttributeNames().nextElement());
          //  System.out.println(session.getValueNames()[0]);
            Order sessionOrder= (Order) session.getAttribute("scopedTarget.ShoppingCart");
            assertEquals(Long.valueOf(1111L),sessionOrder.getId());
    }
}