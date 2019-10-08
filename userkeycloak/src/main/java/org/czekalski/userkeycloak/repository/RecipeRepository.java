package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
}
