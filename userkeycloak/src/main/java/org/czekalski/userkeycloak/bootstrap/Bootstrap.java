package org.czekalski.userkeycloak.bootstrap;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class Bootstrap implements CommandLineRunner {



private final OrderRepository orderRepository;
    public Bootstrap(OrderRepository orderRepository) {

        this.orderRepository = orderRepository;

    }


    @Override
    public void run(String... args) throws Exception {

        Order order1=new Order();
        order1.setDescription("nowe zamowienie");
        order1.setId(orderRepository.save(order1).getId());
        orderRepository.save(order1);


    }
}
