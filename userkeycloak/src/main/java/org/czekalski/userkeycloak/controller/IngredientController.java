package org.czekalski.userkeycloak.controller;

import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

private final IngredientRepository ingredientRepository;


    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    @GetMapping({"", "/", "/index"})
    public String getAllIngredients(Model model){
            model.addAttribute("ingredients",ingredientRepository.findAll());

        return "ingredients/list";
    }



}
