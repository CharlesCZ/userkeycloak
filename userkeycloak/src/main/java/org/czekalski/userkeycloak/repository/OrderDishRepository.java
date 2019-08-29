package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDishRepository extends JpaRepository<OrderDish, Long > {
}
