package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.PaymentKindCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderMapper;
import org.czekalski.userkeycloak.config.audit.JpaAuditingConfig;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private  Order shoppingCart;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentKindRepository paymentKindRepository;
    private final OrderDishRepository orderDishRepository;
    private final OrderIngredientRepository orderIngredientRepository;
    private final StatusRepository statusRepository;



    public OrderServiceImpl(Order shoppingCart, OrderRepository orderRepository, OrderMapper orderMapper, PaymentKindRepository paymentKindRepository, OrderDishRepository orderDishRepository, OrderIngredientRepository orderIngredientRepository, StatusRepository statusRepository) {
        this.shoppingCart = shoppingCart;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.paymentKindRepository = paymentKindRepository;
        this.orderDishRepository = orderDishRepository;
        this.orderIngredientRepository = orderIngredientRepository;
        this.statusRepository = statusRepository;


    }


    @Override
    public Order getShoppingCart(){
        return shoppingCart;
    }

    @Override
public BigDecimal calculateTotalPrice(){
BigDecimal price=new BigDecimal(0);

    for (OrderDish orderDish : shoppingCart.getOrderDishes())
     {
        price=  price.add(    orderDish.getSingleDishCost().multiply( new BigDecimal(orderDish.getQuantity()) )    );

         for (OrderIngredient orderIngredient : orderDish.getOrderIngredients()) {
                price=  price.add(    orderIngredient.getIngredient().getCost().multiply(new BigDecimal(orderIngredient.getQuantity())).multiply(new BigDecimal(orderDish.getQuantity()))   );
            }


        }

//shoppingCart.setTotalPrice(price);
        return price;
}
    @Override
    public BigDecimal totalPriceForOrderDishCommand(OrderDishCommand orderDishCommand) {
        BigDecimal price = new BigDecimal(0);

        if (orderDishCommand.getQuantity() == null || orderDishCommand.getQuantity() == 0) {
            throw new RuntimeException("orderDishQuantity:" + orderDishCommand.getQuantity());
        } else {
            price = price.add(orderDishCommand.getSingleDishCost().multiply(new BigDecimal(orderDishCommand.getQuantity())));

            for (OrderIngredientCommand orderIngredient : orderDishCommand.getOrderIngredients()) {
                price = price.add(orderIngredient.getIngredientDishOrderCost().multiply(new BigDecimal(orderIngredient.getQuantity())).multiply(new BigDecimal(orderDishCommand.getQuantity())));
            }


            return price;
        }
    }

    /*private Order mergeTheSameDishes(Order order){
        for(Iterator<OrderDish> it=order.getOrderDishes().iterator();it.hasNext();){
         OrderDish od1 =  it.next();
            for(Iterator<OrderDish> it2=order.getOrderDishes().iterator();it2.hasNext();) {
                OrderDish od2 = it.next();

                if (compareOrderDishes(od1, od2)) {

                    od1.getOrderIngredients().forEach(i1 -> {
                        od2.getOrderIngredients().forEach(i2 -> {
                            if (i1.getId().equals(i2.getId())) {
                                i1.setQuantity(i1.getQuantity() + i2.getQuantity());
                            }

                        });

                    });

                    od1.setQuantity(od1.getQuantity() + od2.getQuantity());


                }

            }

        }



    }*/





    private Order mergeTheSameDishes(Order order){
if(order.getOrderDishes().size()>1) {
    List<OrderDish> orderDishes = new ArrayList<>(order.getOrderDishes());
    for (int i = 0; i < orderDishes.size(); ++i)
        for (int j = i + 1; j < orderDishes.size(); ++j) {
            OrderDish od1 = orderDishes.get(i);
            OrderDish od2 = orderDishes.get(j);

            if (compareOrderDishes(od1, od2)) {


                orderDishes.remove(od2);
                od1.setQuantity(od1.getQuantity() + od2.getQuantity());
            }



        }

    order.setOrderDishes(new HashSet<>(orderDishes));
}
        return order;
    }













    private Boolean compareOrderDishes(OrderDish od1, OrderDish od2) {

       if( !od1.getDish().getId().equals(od2.getDish().getId()) || !od1.getDish().getSize().equals(od2.getDish().getSize()))
           return false;
       else{
           if(   od1.getOrderIngredients().size() !=od2.getOrderIngredients().size())
           return false;
            else{
               return od1.getOrderIngredients().containsAll(od2.getOrderIngredients());

           }



       }


    }

    @Override
public OrderCommand convertedShoppingCar(){
mergeTheSameDishes(shoppingCart);
        OrderCommand orderCommand=orderMapper.orderToOrderCommand(shoppingCart);
        orderCommand.getOrderDishes().forEach(orderDishCommand -> {
            orderDishCommand.setTotalPrice( totalPriceForOrderDishCommand(orderDishCommand) );
                });

    orderCommand.setPaymentKind(new PaymentKindCommand());
        return orderCommand;

}

    private  Order convertFromProxy(Order shoppingCart,Principal principal){
//TODO better solution
        Order shoppingCartToSave=new Order();
        shoppingCartToSave.setStatus(shoppingCart.getStatus());
        shoppingCartToSave.setPaymentKind(shoppingCart.getPaymentKind());
        shoppingCartToSave.setCity(shoppingCart.getCity());
        shoppingCartToSave.setStreet(shoppingCart.getStreet());
        shoppingCartToSave.setHouseNr(shoppingCart.getHouseNr());
        shoppingCartToSave.setApartment(shoppingCart.getApartment());
        shoppingCartToSave.setOrderDishes(shoppingCart.getOrderDishes());
        shoppingCartToSave.setCreatedDate(shoppingCart.getCreatedDate());
        shoppingCartToSave.setCreatedBy(shoppingCart.getCreatedBy());
        shoppingCartToSave.setFinishedTime(shoppingCart.getFinishedTime());
        shoppingCartToSave.setDescription(shoppingCart.getDescription());
        shoppingCartToSave.setId(shoppingCart.getId());

        shoppingCartToSave.setPayed(shoppingCart.getPayed());
        shoppingCartToSave.setDescription(shoppingCart.getDescription());
        shoppingCartToSave.setTelephone(shoppingCart.getTelephone());
        if(principal!=null && principal.getName()!=null) {
            shoppingCartToSave.setUser(principal.getName());
        }else
            shoppingCartToSave.setUser("user unknown");
     //   jpaAuditingConfig.auditorAwareBean().getCurrentAuditor().ifPresent(shoppingCartToSave::setUser);

        return shoppingCartToSave;

    }

    @Override
    @Transactional
    public Order addOrderToDatabase(OrderCommand orderCommand, Principal principal) {

        shoppingCart.setApartment(orderCommand.getApartment());
        shoppingCart.setHouseNr(orderCommand.getHouseNr());
        shoppingCart.setStreet(orderCommand.getStreet());
        shoppingCart.setCity(orderCommand.getCity());
        shoppingCart.setDescription(orderCommand.getDescription());
        shoppingCart.setTelephone(orderCommand.getTelephone());
        paymentKindRepository.findById(orderCommand.getPaymentKind().getId()).ifPresent(shoppingCart::setPaymentKind);
        shoppingCart.setPayed(false);
        statusRepository.findById(1L).ifPresent( shoppingCart::setStatus);


        Order shoppingCartToSave =convertFromProxy(shoppingCart,principal);

        for(Iterator<OrderDish> it = shoppingCart.getOrderDishes().iterator(); it.hasNext();){
            OrderDish orderDish=it.next();
            //removing temporary id
            orderDish.setId(null);
            orderDish.setOrder(shoppingCartToSave);
        }

        orderRepository.save(shoppingCartToSave);

        return shoppingCart;
    }

    @Override
    public void cleanShoppingCart(){


        shoppingCart.setStatus(null);
        shoppingCart.setPaymentKind(null);
        shoppingCart.setCity(null);
        shoppingCart.setStreet(null);
        shoppingCart.setHouseNr(null);
        shoppingCart.setApartment(null);
        shoppingCart.setOrderDishes(new HashSet<>());
        shoppingCart.setCreatedDate(null);
        shoppingCart.setCreatedBy(null);
        shoppingCart.setFinishedTime(null);
        shoppingCart.setDescription(null);
        shoppingCart.setId(null);
        shoppingCart.setPayed(null);
        shoppingCart.setDescription(null);
        shoppingCart.setTelephone(null);
        shoppingCart.setUser(null);
     //   jpaAuditingConfig.auditorAwareBean().getCurrentAuditor().ifPresent(shoppingCart::setUser);
    }

    @Override
    public List<OrderCommand> getAllOrders(){

       return orderRepository.findAll().stream()
               .map(orderMapper::orderToOrderCommand).collect(Collectors.toList());
    }

    @Override
    public void getTotalPriceForOrderCommand(OrderCommand orderCommand){

BigDecimal fullPrice=new BigDecimal(0);
       for(Iterator<OrderDishCommand> it= orderCommand.getOrderDishes().iterator();it.hasNext();){
           OrderDishCommand odc=it.next();
           BigDecimal tempPrice=  totalPriceForOrderDishCommand(odc);
           System.out.println(tempPrice);
           odc.setTotalPrice(tempPrice);
           fullPrice=fullPrice.add(tempPrice);
       }


       orderCommand.setTotalPrice(fullPrice);

    }

    @Override
    public OrderCommand getOrderDetailsById(long id) {
        Optional<OrderCommand> orderCommand =orderRepository.getOrderDetailsById(id)
                .map(orderMapper::orderToOrderCommand);

        if(orderCommand.isPresent()){
            getTotalPriceForOrderCommand(orderCommand.get());

        return orderCommand.get();
        }
        else return null;
    }
    @Override
    public OrderCommand save(OrderCommand orderCommand){

        return orderMapper.orderToOrderCommand(
                orderRepository.save(
                        orderMapper.orderCommandToOrder(orderCommand)));
    }

    @Override
    public List<OrderCommand> newOrderList() {

        Status status = statusRepository.findByName("Created").orElseThrow(() -> new RuntimeException("Not Found status with this name"));
        return orderRepository.findByStatus(status).stream()
                .map(order -> orderMapper.orderToOrderCommand(order)).collect(Collectors.toList());
    }

    @Override
    public List<OrderCommand> newOrderList(String id) {
        Status status = statusRepository.findByName("Created").orElseThrow(() -> new RuntimeException("Not Found status with this name"));
        return orderRepository.findByStatus(status).stream()
                .map(order -> orderMapper.orderToOrderCommand(order)).filter(orderCommand -> orderCommand.getUser().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<OrderCommand> getAllUserOrders(String name) {
        return orderRepository.findAllByUserId(name).stream()
                .map(orderMapper::orderToOrderCommand).collect(Collectors.toList());
    }
}
