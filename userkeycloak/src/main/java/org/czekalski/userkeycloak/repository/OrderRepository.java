package org.czekalski.userkeycloak.repository;


import org.czekalski.userkeycloak.commadPattern.command.OrderCommand;
import org.czekalski.userkeycloak.model.Order;
import org.czekalski.userkeycloak.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {


    @Query("SELECT DISTINCT o FROM  Order o LEFT OUTER JOIN OrderDish od ON o.id= od.order LEFT OUTER JOIN OrderIngredient io ON od.id = io.orderDish WHERE o.id=:orderId ")
   Optional<Order> getOrderDetailsById(Long orderId);

    List<Order> findByStatus(Status status);

    @Query("SELECT  o FROM  Order  o WHERE o.user=:name ")
    List<Order> findAllByUserId(String name);
}
