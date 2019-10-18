package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {


    @Query("Delete from Recipe r where r.dish.id=:id")
    void deleteByDishId(Long id);
}
