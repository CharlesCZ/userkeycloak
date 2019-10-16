package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderMapper;
import org.czekalski.userkeycloak.config.audit.AuditorAwareBean;
import org.czekalski.userkeycloak.config.audit.JpaAuditingConfig;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.AuditorAware;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    public static final String INGREDIENT_NAME1 = "ser";
    public static final String INGREDIENT_NAME2 = "oregano";
    public static final String DISH_NAME_1 = "MargheritaCheeseX2";
    public static final int HOUSE_NR = 21;


    @Mock
    OrderRepository orderRepository;
    @Mock
    PaymentKindRepository paymentKindRepository;
    @Mock
    OrderDishRepository orderDishRepository;
    @Mock
     OrderIngredientRepository orderIngredientRepository;
    @Mock
    StatusRepository statusRepository;
    @Mock
    JpaAuditingConfig jpaAuditingConfig;


    OrderService orderService;
    OrderMapper orderMapper=OrderMapper.INSTANCE;

    @Captor
    ArgumentCaptor<Order> houseNrCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
   orderService=new OrderService(new Order(), orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository, jpaAuditingConfig);

    }

    private Order preparingShoppingCart() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("10.00"));



        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.50"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.20"));


        Order order=new Order();

        OrderDish orderDish=new OrderDish();
        orderDish.setId(1L);
        orderDish.setSingleDishCost(dish.getCost());
        orderDish.setQuantity(4);
        orderDish.setDish(dish);
        orderDish.setOrder(order);


        OrderIngredient orderIngredient1=new OrderIngredient();
        orderIngredient1.setIngredient(ingredient1);
        orderIngredient1.setIngredientDishOrderCost(ingredient1.getCost());
        orderIngredient1.setOrderDish(orderDish);
        orderIngredient1.setQuantity(2);
        orderIngredient1.setId(1L);

        OrderIngredient orderIngredient2=new OrderIngredient();
        orderIngredient2.setIngredient(ingredient2);
        orderIngredient2.setIngredientDishOrderCost(ingredient2.getCost());
        orderIngredient2.setOrderDish(orderDish);
        orderIngredient2.setQuantity(1);
        orderIngredient2.setId(2L);

        Set<OrderIngredient> orderIngredients=new HashSet<>();
        orderIngredients.add(orderIngredient1);
        orderIngredients.add(orderIngredient2);

        orderDish.setOrderIngredients(orderIngredients);

        Set<OrderDish> orderDishes=new HashSet<>();
        orderDishes.add(orderDish);

        Dish dish2 = new Dish();
        dish2.setId(1L);
        dish2.setName("schabowy");
        dish2.setCost(new BigDecimal("20.00"));

        OrderDish orderDish1=new OrderDish();
        orderDish1.setQuantity(1);
        orderDish1.setSingleDishCost(dish2.getCost());
        orderDishes.add(orderDish1);

        order.setOrderDishes(orderDishes);

        return order;
    }
    @Test
    void calculatePrice() {
       orderService=new OrderService(preparingShoppingCart(), orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository, jpaAuditingConfig);

        BigDecimal returnedPrice=orderService.calculateTotalPrice();

        assertEquals("64.80",returnedPrice.toString());
    }

    @Test
    void addOrderToDatabase(){

        Order orderToReturn=preparingShoppingCart();
        orderToReturn.setId(1L);
        orderService=new OrderService(preparingShoppingCart(), orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository, jpaAuditingConfig);

        PaymentKind paymentKind=new PaymentKind();
        paymentKind.setId(1L);
        paymentKind.setName("Card");
        given(paymentKindRepository.findById(1L)).willReturn(Optional.of(paymentKind));

        Status status=new Status();
        status.setName("started");
        status.setId(1L);
        given(statusRepository.findById(1L)).willReturn(Optional.of(status));
        AuditorAware<String> auditorAware=new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                return Optional.of("nowy user");
            }
        };

        given(jpaAuditingConfig.auditorAwareBean()).willReturn(auditorAware);
        given(orderRepository.save(houseNrCaptor.capture())).willReturn(orderToReturn);

        Long id=1L,orderIngredientId=1L;
        for (OrderDish orderDish : orderToReturn.getOrderDishes()) {
            orderDish.setId(++id);
            for(OrderIngredient orderIngredient: orderDish.getOrderIngredients()){
                orderIngredient.setId(++orderIngredientId);
            }
        }

        given(orderDishRepository.saveAll(any(Set.class))).willReturn(new ArrayList<>(orderToReturn.getOrderDishes()));
        given(orderIngredientRepository.saveAll(any(Set.class))).willReturn(    new ArrayList<>(orderToReturn.getOrderDishes().iterator().next().getOrderIngredients()),
                new ArrayList<>(orderToReturn.getOrderDishes().iterator().next().getOrderIngredients()));



        OrderCommand orderAsArgument=new OrderCommand();
        orderAsArgument.setCity("Poznan");
        orderAsArgument.setStreet("Piotrowo");
        orderAsArgument.setHouseNr(HOUSE_NR);
        orderAsArgument.setApartment(44);

        PaymentKindCommand paymentKindCommand=new PaymentKindCommand();
        paymentKindCommand.setId(1L);
        orderAsArgument.setPaymentKind(paymentKindCommand);
        Order returnedOrder=orderService.addOrderToDatabase(orderAsArgument);

        then(paymentKindRepository).should().findById(1L);
        then(orderRepository).should().save(any(Order.class));
        then(orderDishRepository).should().saveAll(any(Set.class));
        then(orderIngredientRepository).should(times(2)).saveAll(any(Set.class));
        then(statusRepository).should().findById(1L);

        assertEquals(Integer.valueOf(HOUSE_NR),houseNrCaptor.getValue().getHouseNr());
        assertThat(orderToReturn.getOrderDishes()).hasSize(2);
        assertEquals(Long.valueOf(1L),returnedOrder.getStatus().getId());

    }
}