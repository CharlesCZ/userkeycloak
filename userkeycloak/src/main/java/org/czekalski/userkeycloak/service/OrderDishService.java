package org.czekalski.userkeycloak.service;


import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.czekalski.userkeycloak.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderDishService {

    private final Order shoppingCart;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public OrderDishService(Order shoppingCart, RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.shoppingCart = shoppingCart;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }




    public List<OrderDish> getAllPossibilities() {


        List<Recipe> recipes=recipeRepository.findAll();
        List<Dish> distinctDishes=new ArrayList<>();
        //filtering to distinct dishes
        distinctDishesFilter(recipes, distinctDishes);

        //after I've got distinct dishes in distinctDishes
        List<OrderDish> orderDishes=new ArrayList<>();

        distinctDishes.forEach(dish -> {
            OrderDish orderDish = new OrderDish();
            orderDish.setDish(dish);
            orderDishes.add(orderDish);

        });

        //populating orderIngredients of orderDish
     recipes.forEach(recipe -> {

         orderDishes.forEach(orderDish -> {
             if(orderDish.getDish().getId().equals(recipe.getDish().getId())){
                 OrderIngredient orderIngredient=new OrderIngredient();
                 orderIngredient.setIngredient(recipe.getIngredient());
                 orderIngredient.setIngredientDishOrderCost(recipe.getIngredient().getCost());
                 orderIngredient.setQuantity(recipe.getIngredientQuantity());
                 orderIngredient.setOrderDish(orderDish);
                 orderDish.getOrderIngredients().add(orderIngredient);
             }

         });

     });

     //populating with additional ingredients
     //duplication is inside
        ingredientRepository.findAll().forEach(ingredient -> {
            orderDishes.forEach(orderDish -> {
                OrderIngredient orderIngredient=new OrderIngredient();
                orderIngredient.setIngredient(ingredient);
                orderIngredient.setIngredientDishOrderCost(ingredient.getCost());
                orderIngredient.setQuantity(0);
                orderIngredient.setOrderDish(orderDish);
                orderDish.getOrderIngredients().add(orderIngredient);
            });
        });


        //removing duplication
        orderDishes.forEach(orderDish -> {
            HashSet<Long> ingredientId=new HashSet<>();
            //to not miss anything
            orderDish.getOrderIngredients().forEach(orderIngredient -> {
                if(!orderIngredient.getQuantity().equals(0)){
                    ingredientId.add(orderIngredient.getIngredient().getId());
                }

            });

            orderDish.getOrderIngredients().removeIf(i-> !ingredientId.add(i.getIngredient().getId()) && i.getQuantity().equals(0));
        });



        return orderDishes;
    }



    private void distinctDishesFilter(List<Recipe> recipes, List<Dish> distinctDishes) {
        int dirty=0;
        for (Recipe recipe : recipes) {
            for (Dish distinctDish : distinctDishes) {
                if (distinctDish.getId().equals(recipe.getDish().getId())) {
                    dirty = 1;
                }
            }
            //after moving from innerloop
            if (dirty == 0) {
                distinctDishes.add(recipe.getDish());
            } else {
                //reseting dirty
                dirty = 0;
            }

        }

    }

    public Order addToCart() {
        return null;
    }
}
