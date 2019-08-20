package org.czekalski.userkeycloak.repository;



import org.czekalski.userkeycloak.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Long> {
}
