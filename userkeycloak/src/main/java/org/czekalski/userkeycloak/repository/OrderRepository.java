package org.czekalski.userkeycloak.repository;



import org.czekalski.userkeycloak.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
