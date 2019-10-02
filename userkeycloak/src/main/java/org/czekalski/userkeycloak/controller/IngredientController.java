package org.czekalski.userkeycloak.controller;


import org.czekalski.userkeycloak.service.IngredientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

private  final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @GetMapping({""})
    public String getAllIngredients(Model model){
            model.addAttribute("ingredients",ingredientService.findAll());

        return "ingredients/list";
    }



}
