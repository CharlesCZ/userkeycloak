package org.czekalski.userkeycloak.commadPattern.mapper;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("application-development.properties")
class OrderMapperTest {

    private static final String NAME = "Mr. Kucinski";
    private static final long ID = 1L;
    private OrderMapper orderMapper=OrderMapper.INSTANCE;
    @BeforeEach
    void setUp() {
    }

    @Test
    void orderToOrderCommandTest() {
        Order order=new Order();
        order.setId(ID);
        Timestamp finishedTime=new Timestamp(System.currentTimeMillis());
        order.setFinishedTime(finishedTime);
        order.setCreatedBy(NAME);
        Date createdDate=new Date(System.currentTimeMillis());
        order.setCreatedDate(createdDate);
        OrderCommand orderCommand =orderMapper.orderToOrderCommand(order);

        assertEquals(Long.valueOf(1L),orderCommand.getId());
        assertEquals(NAME,orderCommand.getCreatedBy());
        assertEquals(finishedTime,orderCommand.getFinishedTime());
        assertEquals(createdDate,order.getCreatedDate());
    }

    @Test
    void orderCommandToOrder() {
        OrderCommand orderCommand=new OrderCommand();
        orderCommand.setId(1L);
        Timestamp finishedTime=new Timestamp(System.currentTimeMillis());
        orderCommand.setFinishedTime(finishedTime);
        orderCommand.setCreatedBy(NAME);
        Date createdDate=new Date(System.currentTimeMillis());
        orderCommand.setCreatedDate(createdDate);
        Order convertedToOrder=orderMapper.orderCommandToOrder(orderCommand);

        assertEquals(Long.valueOf(1L),convertedToOrder.getId());
        assertEquals(NAME, convertedToOrder.getCreatedBy());
        assertEquals(finishedTime,convertedToOrder.getFinishedTime());
        assertEquals(createdDate,convertedToOrder.getCreatedDate());

    }
}