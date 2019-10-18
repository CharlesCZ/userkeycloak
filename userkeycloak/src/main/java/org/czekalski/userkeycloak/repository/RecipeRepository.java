package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.Dish;
import org.czekalski.userkeycloak.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    @Transactional
    @Modifying
    @Query("Delete from Recipe r where r.dish.id=:id")
    void deleteByDishId(Long id);

    List<Recipe> findAllByDish(Dish dish);
}
