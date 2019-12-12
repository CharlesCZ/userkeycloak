package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderMapper;
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
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
   orderService=new OrderServiceImpl(new Order(), orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);

    }

    private Order preparingFullOrder() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setSize(new BigDecimal(1));
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
        dish2.setId(2L);
        dish2.setName("schabowy");
        dish2.setSize(new BigDecimal(1));
        dish2.setCost(new BigDecimal("20.00"));

        OrderDish orderDish1=new OrderDish();
        orderDish1.setQuantity(1);
        orderDish1.setSingleDishCost(dish2.getCost());
        orderDish1.setDish(dish2);
        orderDishes.add(orderDish1);

        order.setOrderDishes(orderDishes);

        return order;
    }
    @Test
    void calculatePrice() {
       orderService=new OrderServiceImpl(preparingFullOrder(), orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);

        BigDecimal returnedPrice=orderService.calculateTotalPrice();

        assertEquals("64.80",returnedPrice.toString());
    }

    @Test
    void addOrderToDatabase(){

        Order orderToReturn= preparingFullOrder();
        orderToReturn.setId(1L);
        orderService=new OrderServiceImpl(preparingFullOrder(), orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);

        PaymentKind paymentKind=new PaymentKind();
        paymentKind.setId(1L);
        paymentKind.setName("Card");
        given(paymentKindRepository.findById(1L)).willReturn(Optional.of(paymentKind));

        Status status=new Status();
        status.setName("started");
        status.setId(1L);
        given(statusRepository.findById(1L)).willReturn(Optional.of(status));



        given(orderRepository.save(houseNrCaptor.capture())).willReturn(orderToReturn);

        Long id=1L,orderIngredientId=1L;
        for (OrderDish orderDish : orderToReturn.getOrderDishes()) {
            orderDish.setId(++id);
            for(OrderIngredient orderIngredient: orderDish.getOrderIngredients()){
                orderIngredient.setId(++orderIngredientId);
            }
        }

        OrderCommand orderAsArgument=new OrderCommand();
        orderAsArgument.setCity("Poznan");
        orderAsArgument.setStreet("Piotrowo");
        orderAsArgument.setHouseNr(HOUSE_NR);
        orderAsArgument.setApartment(44);

        PaymentKindCommand paymentKindCommand=new PaymentKindCommand();
        paymentKindCommand.setId(1L);
        orderAsArgument.setPaymentKind(paymentKindCommand);
        Principal principal=new Principal() {
            @Override
            public String getName() {
                return "123";
            }
        };
        Order returnedOrder=orderService.addOrderToDatabase(orderAsArgument, principal);

        then(paymentKindRepository).should().findById(1L);
        then(orderRepository).should().save(any(Order.class));
        then(statusRepository).should().findById(1L);

        assertEquals(Integer.valueOf(HOUSE_NR),houseNrCaptor.getValue().getHouseNr());
        assertThat(orderToReturn.getOrderDishes()).hasSize(2);
        assertEquals(Long.valueOf(1L),returnedOrder.getStatus().getId());

    }



    @Test
    void getAllOrders(){
        PaymentKind paymentKind=new PaymentKind();
        paymentKind.setId(1L);
        paymentKind.setName("Card");

        Status status=new Status();
        status.setName("started");
        status.setId(1L);

        Order orderToReturn=new Order();
        orderToReturn.setId(1L);
        orderToReturn.setPayed(false);
        orderToReturn.setStatus(status);
        orderToReturn.setPaymentKind(paymentKind);
        orderToReturn.setCreatedBy("user");
        Timestamp createdDate=new  Timestamp(System.currentTimeMillis());
        orderToReturn.setCreatedDate(createdDate);
        orderToReturn.setLastModifiedBy("user2");
        Timestamp lastModifiedDate=new  Timestamp(System.currentTimeMillis());
        orderToReturn.setLastModifiedDate(lastModifiedDate);

        given(orderRepository.findAll()).willReturn(Arrays.asList(orderToReturn));


        List<OrderCommand> returnedOrder=orderService.getAllOrders();


        then(orderRepository).should().findAll();
        assertEquals(Long.valueOf(1L),returnedOrder.get(0).getId());
        assertEquals(Long.valueOf(1L),returnedOrder.get(0).getStatus().getId());
        assertEquals("user",returnedOrder.get(0).getCreatedBy());
        assertEquals("user2",returnedOrder.get(0).getLastModifiedBy());
        assertEquals(lastModifiedDate,returnedOrder.get(0).getLastModifiedDate());
        assertEquals(createdDate,returnedOrder.get(0).getCreatedDate());
    }



    @Test
    void getOrderDetailsById() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);

        Order order=new Order();
        order.setId(1L);
        order.setPayed(false);

        OrderDish orderDish=new OrderDish();
        orderDish.setId(1L);
        orderDish.setQuantity(4);
        orderDish.setDish(dish);
        orderDish.setSingleDishCost(new BigDecimal("2.0"));
        orderDish.setOrder(order);

        OrderIngredient orderIngredient1=new OrderIngredient();
        orderIngredient1.setIngredient(ingredient1);
        orderIngredient1.setOrderDish(orderDish);
        orderIngredient1.setQuantity(2);
        orderIngredient1.setId(1L);
        orderIngredient1.setIngredientDishOrderCost(new BigDecimal("4.0"));
        Set<OrderIngredient> orderIngredients=new HashSet<>();
        orderIngredients.add(orderIngredient1);

        orderDish.setOrderIngredients(orderIngredients);
        Set<OrderDish> orderDishes=new HashSet<>();
        orderDishes.add(orderDish);
        order.setOrderDishes(orderDishes);

        given(orderRepository.getOrderDetailsById(1L)).willReturn(Optional.of(order));



        OrderCommand orderCommand=orderService.getOrderDetailsById(1L);



        assertEquals(false,orderCommand.getPayed());
        assertThat(orderCommand.getOrderDishes()).hasSize(1);
        assertEquals(INGREDIENT_NAME1,
                orderCommand.getOrderDishes().iterator().next().getOrderIngredients().iterator().next().getIngredient().getName());
    }


    //  given(orderService.updateOrder(any(OrderCommand.class))).willReturn(orderCommand);
    @Test
    void totalPriceForOrderDishCommand(){
        OrderDishCommand orderDishCommand=new OrderDishCommand();
        orderDishCommand.setSingleDishCost(new BigDecimal("20.0"));
        orderDishCommand.setQuantity(2);
        OrderIngredientCommand orderIngredient1=new OrderIngredientCommand();
        orderIngredient1.setQuantity(2);
        orderIngredient1.setIngredientDishOrderCost(new BigDecimal("2.0"));
        orderDishCommand.setOrderIngredients(new HashSet<>(Collections.singletonList(orderIngredient1)));


       BigDecimal returnedPrice=orderService.totalPriceForOrderDishCommand(orderDishCommand);


       assertEquals(new BigDecimal("48.0"),returnedPrice);
    }



    @Test
    void  getTotalPriceForOrderCommand(){
        OrderDishCommand orderDishCommand=new OrderDishCommand();
        orderDishCommand.setSingleDishCost(new BigDecimal("20.0"));
        orderDishCommand.setQuantity(2);
        OrderIngredientCommand orderIngredient1=new OrderIngredientCommand();
        orderIngredient1.setQuantity(2);
        orderIngredient1.setIngredientDishOrderCost(new BigDecimal("2.0"));
        orderDishCommand.setOrderIngredients(new HashSet<>(Collections.singletonList(orderIngredient1)));
        OrderCommand orderCommand=new OrderCommand();
        orderCommand.setOrderDishes(new HashSet<>(Collections.singletonList(orderDishCommand)));

        orderService.getTotalPriceForOrderCommand(orderCommand);

        assertEquals(new BigDecimal("48.0"),orderCommand.getTotalPrice());
    }

    @Test
    void mergeTheSameDishes(){
        Order order=preparingFullOrder();
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("10.00"));
        dish.setSize(new BigDecimal(1));


        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.50"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.20"));


        OrderDish orderDish=new OrderDish();
        orderDish.setId(null);
        orderDish.setSingleDishCost(dish.getCost());
        orderDish.setQuantity(1);
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
order.getOrderDishes().add(orderDish);
        orderService=new OrderServiceImpl(order, orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);
OrderCommand returnedOrderComand=orderService.convertedShoppingCar();


assertThat(returnedOrderComand.getOrderDishes()).hasSize(2);
assertEquals(Integer.valueOf(5),returnedOrderComand.getOrderDishes().stream()
                           .filter(orderDishCommand -> orderDishCommand.getDish().getName().equals(DISH_NAME_1)).findFirst()
                            .get().getQuantity());

    }

    @Test
    void mergeTheSameDishes_differentDishes(){
        orderService=new OrderServiceImpl(preparingFullOrder(), orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);

        OrderCommand returnedOrderComand=orderService.convertedShoppingCar();


        assertThat(returnedOrderComand.getOrderDishes()).hasSize(2);


    }


    @Test
    void mergeTheSameDishes_DifferentIngredients(){
        Order order=preparingFullOrder();
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("10.00"));
        dish.setSize(new BigDecimal(1));


        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.50"));




        OrderDish orderDish=new OrderDish();
        orderDish.setId(null);
        orderDish.setSingleDishCost(dish.getCost());
        orderDish.setQuantity(1);
        orderDish.setDish(dish);
        orderDish.setOrder(order);


        OrderIngredient orderIngredient1=new OrderIngredient();
        orderIngredient1.setIngredient(ingredient1);
        orderIngredient1.setIngredientDishOrderCost(ingredient1.getCost());
        orderIngredient1.setOrderDish(orderDish);
        orderIngredient1.setQuantity(2);
        orderIngredient1.setId(1L);



        Set<OrderIngredient> orderIngredients=new HashSet<>();
        orderIngredients.add(orderIngredient1);


        orderDish.setOrderIngredients(orderIngredients);
        order.getOrderDishes().add(orderDish);
        orderService=new OrderServiceImpl(order, orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);
        OrderCommand returnedOrderComand=orderService.convertedShoppingCar();


        assertThat(returnedOrderComand.getOrderDishes()).hasSize(3);


    }


    @Test
    void mergeTheSameDishes_differentSizes(){
        Order order=preparingFullOrder();
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName(DISH_NAME_1);
        dish.setCost(new BigDecimal("10.00"));
        dish.setSize(new BigDecimal(1.2));


        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName(INGREDIENT_NAME1);
        ingredient1.setCost(new BigDecimal("0.50"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName(INGREDIENT_NAME2);
        ingredient2.setCost(new BigDecimal("0.20"));


        OrderDish orderDish=new OrderDish();
        orderDish.setId(null);
        orderDish.setSingleDishCost(dish.getCost());
        orderDish.setQuantity(1);
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
        order.getOrderDishes().add(orderDish);
        orderService=new OrderServiceImpl(order, orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);
        OrderCommand returnedOrderComand=orderService.convertedShoppingCar();


        assertThat(returnedOrderComand.getOrderDishes()).hasSize(3);


    }

@Test
    void cleanShoppingCart(){
    Order order=preparingFullOrder();
   order.setTelephone("123123123");
    orderService=new OrderServiceImpl(order, orderRepository, orderMapper, paymentKindRepository, orderDishRepository, orderIngredientRepository, statusRepository);
  orderService.cleanShoppingCart();

  assertThat(orderService.getShoppingCart().getOrderDishes()).isEmpty();
    assertThat(orderService.getShoppingCart().getTelephone()).isNull();



}


@Test
    void  save(){
OrderCommand orderCommand=new OrderCommand();
orderCommand.setUser("newUser");

Order order=new Order();
order.setUser("newUser");
order.setId(1L);

given(orderRepository.save(any(Order.class))).willReturn(order);

OrderCommand returnedUser=orderService.save(orderCommand);

    assertEquals("newUser",returnedUser.getUser());
    assertEquals(Long.valueOf(1L),returnedUser.getId());


}
}