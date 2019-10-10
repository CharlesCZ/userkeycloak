package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.service.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DishController {



    private final DishService dishService;

    public DishController( DishService dishService) {

        this.dishService = dishService;
    }

    @GetMapping("/orders/dishes/new")
    public String getAllDishes(Model model){


        model.addAttribute("dishes",dishService.getAllDishesWithIngredients());


                return "orders/dishes";
    }

    @GetMapping("/orders/dishes/{id}/new")
public String dishDetails(@PathVariable Long id,Model model){
        model.addAttribute("dish",dishService.getDishById(id));

        return "orders/dishAndIngredientsForm";
}

    @PostMapping("/orders/dishes/{id}/new")
    public String postChosenDish(@PathVariable Long id, DishCommand dishCommand){

        dishService.addToCart(dishCommand);

        return "redirect:/orders/summary";
    }

}
