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


    public Order getShoppingCart(){
        return shoppingCart;
    }

public Boolean deleteFromCart(Long id){



        return  shoppingCart.getOrderDishes().removeIf(orderDish -> orderDish.getId().equals(id));
}



    public Order addToCart() {
        return null;
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
     //       dish.getOrderDishes().add(orderDish);
            orderDish.setDish(dish);
            orderDishes.add(orderDish);

        });

        //populating orderIngredients of orderDish
        populatingWithIngredients(recipes, orderDishes);

        //populating with additional ingredients
        //duplication is inside
         populatingWithIngredientsWithDuplicates( ingredientRepository.findAll(),orderDishes);

        //removing duplication
        removingDuplication(orderDishes);


        return orderDishes;
    }

    private void removingDuplication(List<OrderDish> orderDishes) {
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
    }


    private void populatingWithIngredientsWithDuplicates(List<Ingredient> AllFromIngredientRepository, List<OrderDish> orderDishes) {
        AllFromIngredientRepository.forEach(ingredient -> {
            orderDishes.forEach(orderDish -> {
                OrderIngredient orderIngredient=new OrderIngredient();
      //          ingredient.getOrderIngredients().add(orderIngredient);
                orderIngredient.setIngredient(ingredient);
                orderIngredient.setIngredientDishOrderCost(ingredient.getCost());
                orderIngredient.setQuantity(0);
                orderIngredient.setOrderDish(orderDish);
                orderDish.getOrderIngredients().add(orderIngredient);
            });
        });
    }
    private void populatingWithIngredients(List<Recipe> recipes, List<OrderDish> orderDishes) {
        recipes.forEach(recipe -> {

            orderDishes.forEach(orderDish -> {
                if(orderDish.getDish().getId().equals(recipe.getDish().getId())){
                    OrderIngredient orderIngredient=new OrderIngredient();
      //              recipe.getIngredient().getOrderIngredients().add(orderIngredient);
                    orderIngredient.setIngredient(recipe.getIngredient());
                    orderIngredient.setIngredientDishOrderCost(recipe.getIngredient().getCost());
                    orderIngredient.setQuantity(recipe.getQuantity());
                    orderIngredient.setOrderDish(orderDish);
                    orderDish.getOrderIngredients().add(orderIngredient);
                }

            });

        });
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


}
