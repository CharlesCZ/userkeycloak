package org.czekalski.userkeycloak.bootstrap;
import org.czekalski.userkeycloak.model.Orders;
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

        Orders order1=new Orders();
        order1.setNazwa("nowe zamowienie");
        order1.setId(orderRepository.save(order1).getId());
        orderRepository.save(order1);


    }
}
