package org.czekalski.userkeycloak.repository;



import org.czekalski.userkeycloak.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {


    @Query("SELECT DISTINCT o FROM  Order o LEFT OUTER JOIN OrderDish od ON o.id= od.order LEFT OUTER JOIN OrderIngredient io ON od.id = io.orderDish WHERE o.id=:orderId ")
   Optional<Order> getOrderDetailsById(Long orderId);
}
