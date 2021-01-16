package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.OrderIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderIngredientRepository extends JpaRepository<OrderIngredient,Long> {
}
