package org.czekalski.userkeycloak.controller;

import org.bouncycastle.math.raw.Mod;
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

    @GetMapping("/dish/new")
    public String getNewDish(Model model){

        model.addAttribute("dish",new DishCommand());

        return "dishes/dishForm";
    }
    @PostMapping("/dish/new")
    public String  saveOrUpdateDish(DishCommand dishCommand){

   DishCommand returnedDish= dishService.saveDishCommand(dishCommand);

       return  "redirect:/dish/"+returnedDish.getId()+"/show";
    }

    @GetMapping("/dish/{id}/show")
    public String getDishShow(@PathVariable  Long id, Model model){
        model.addAttribute("dish",dishService.findDishCommandById(id));

        return "dishes/show";
    }

    @GetMapping("/dish/{id}/update")
    public String getUpdateDish(@PathVariable  Long id,Model model){
        model.addAttribute("dish",dishService.findDishCommandById(id));

        return "dishes/dishForm";
    }

    @GetMapping("/dish/{id}/delete")
    public String getDeleteDish(@PathVariable  Long id){
        dishService.deleteById(id);

        return "redirect:/orders/dishes/new";
    }




}
