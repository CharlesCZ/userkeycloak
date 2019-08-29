package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
}
