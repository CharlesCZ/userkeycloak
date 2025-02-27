package org.czekalski.userkeycloak.controller;


import lombok.extern.slf4j.Slf4j;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.exceptions.NotFoundIngredientException;
import org.czekalski.userkeycloak.model.Ingredient;
import org.czekalski.userkeycloak.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/ingredients")
public class IngredientController {

private  final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @GetMapping({"", "/", "/index"})
    public String getAllIngredients(Model model){
            model.addAttribute("ingredients",ingredientService.findAll());

        return "ingredients/list";
    }

    @GetMapping("/{id}/show")
public String showIngredient(@PathVariable Long id, Model model){
        model.addAttribute("ingredient",ingredientService.findById(id));

        return "ingredients/show";
}

    @GetMapping("/{id}/delete")
    public String deleteIngredient(@PathVariable Long id){

        ingredientService.deleteById(id);

        return "redirect:/ingredients";
    }

@GetMapping("/new")
public String newIngredientForm(Model model){


    Ingredient ingredient=new Ingredient();

    model.addAttribute("ingredient",ingredient);

        return "ingredients/ingredientForm";
    }

    @PostMapping("/new")
    public String SaveOrUpdateIngredient(IngredientCommand ingredientCommand){
  IngredientCommand savedIngredient=  ingredientService.save( ingredientCommand);

  return "redirect:/ingredients/"+savedIngredient.getId()+"/show";

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundIngredientException.class)
    public String handleNotFoundIngredient(Exception ex,Model model){

        model.addAttribute("ex",ex);

        return "ingredients/404view";
    }
}
