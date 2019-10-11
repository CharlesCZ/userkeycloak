package org.czekalski.userkeycloak.repository;

import org.czekalski.userkeycloak.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long > {

    @Query("SELECT DISTINCT  d FROM Dish d INNER  JOIN  Recipe r ON d.id = r.dish")
    List<Dish> findAll();

    @Query("SELECT DISTINCT  d FROM Dish d INNER  JOIN  Recipe r ON d.id = r.dish WHERE  d.id=:aLong")
    Optional<Dish> findById(Long aLong);
}
