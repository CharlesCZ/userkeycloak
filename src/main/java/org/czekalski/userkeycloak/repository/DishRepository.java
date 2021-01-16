package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long > {

    @Query("SELECT DISTINCT  d FROM Dish d INNER  JOIN  Recipe r ON d.id = r.dish")
    List<Dish> InnerJoinRecipeAll();

    @Query("SELECT DISTINCT  d FROM Dish d LEFT  JOIN  Recipe r ON d.id = r.dish WHERE  d.id=:aLong")
    Optional<Dish> LeftJoinRecipe(Long aLong);

    @Query("SELECT DISTINCT  d FROM Dish d LEFT OUTER  JOIN  Recipe r ON d.id = r.dish WHERE  d.id=:aLong")
    Optional<Dish> findByIdLeftJoin(Long aLong);

    @Query("SELECT DISTINCT  d FROM Dish d WHERE d.type.id=:i")
    List<Dish> findAllWhereTypeId(Long i);
}
