package org.czekalski.userkeycloak.service;


import org.czekalski.userkeycloak.commadPattern.command.DishCommand;
import org.czekalski.userkeycloak.commadPattern.command.IngredientCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.IngredientMapper;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderDishMapper;
import org.czekalski.userkeycloak.model.*;
import org.czekalski.userkeycloak.repository.IngredientRepository;
import org.czekalski.userkeycloak.repository.OrderDishRepository;
import org.czekalski.userkeycloak.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDishServiceImpl implements  OrderDishService{

    private final Order shoppingCart;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final OrderDishRepository orderDishRepository;
    private final OrderDishMapper orderDishMapper;
    private final IngredientMapper ingredientMapper;

    public OrderDishServiceImpl(Order shoppingCart, RecipeRepository recipeRepository, IngredientRepository ingredientRepository, OrderDishRepository orderDishRepository, OrderDishMapper orderDishMapper, IngredientMapper ingredientMapper) {
        this.shoppingCart = shoppingCart;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.orderDishRepository = orderDishRepository;
        this.orderDishMapper = orderDishMapper;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public Order getShoppingCart(){
        return shoppingCart;
    }


    @Override
public Boolean deleteFromCart(Long id){



        return  shoppingCart.getOrderDishes().removeIf(orderDish -> orderDish.getId().equals(id));
}


    @Override
    public Order addToCart() {
        return null;
    }



























    @Override
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

    @Override
    public void deleteOrderDishById(Long orderDishId) {
        orderDishRepository.deleteById(orderDishId);
    }

    @Override
    public OrderDishCommand getOrderDishCartById(long orderDishId) {

     /* return   shoppingCart.getOrderDishes().stream()
              .filter(orderDish -> orderDish.getId().equals(orderDishId))
              .map(orderDishMapper::orderDishToOrderDishCommand).findFirst().orElseThrow(RuntimeException::new);*/

     OrderDishCommand orderDishCommand=   shoppingCart.getOrderDishes().stream()
                .filter(orderDish -> orderDish.getId().equals(orderDishId))
                .map(orderDishMapper::orderDishToOrderDishCommand).findFirst().orElseThrow(RuntimeException::new);

orderDishCommand.getDish().setQuantity(orderDishCommand.getQuantity());

orderDishCommand.getOrderIngredients().forEach(orderIngredientCommand -> {
    orderIngredientCommand.getIngredient().setQuantity( orderIngredientCommand.getQuantity());
    orderDishCommand.getDish().getIngredientCommands().add(orderIngredientCommand.getIngredient());
});

        //addition ingredients with duplicates
        addAllIngredientsToDishCommand(orderDishCommand.getDish());


        //removing duplicates
        removingDuplicatesFromDishCommand(orderDishCommand.getDish());

//ingredient price depends on dish size
    /*    orderDishCommand.getDish().getIngredientCommands().stream()
                .map(ingredientCommand ->{
                    ingredientCommand.setCost(ingredientCommand.getCost().multiply(orderDishCommand.getDish().getSize()).stripTrailingZeros());
                    return  ingredientCommand;
                }).collect(Collectors.toList());

*/

return orderDishCommand;
    }

    private void addAllIngredientsToDishCommand(DishCommand dishCommand) {
        List<Ingredient> allIngredients= ingredientRepository.findAll();
        allIngredients.forEach(ingredient -> {

            IngredientCommand ingredientCommand=ingredientMapper.ingredientToIngredientCommand(ingredient);
            ingredientCommand.setQuantity(0);
            //ingredient price depends on dish size
            ingredientCommand.setCost(ingredientCommand.getCost().multiply(dishCommand.getSize()).stripTrailingZeros());
            dishCommand.getIngredientCommands().add(ingredientCommand);

        });
    }

    private void removingDuplicatesFromDishCommand(DishCommand dishCommand) {
        HashSet<Long> ingredientCommandId=new HashSet<>();

        dishCommand.getIngredientCommands().forEach(ingredientCommand -> {
            if(!ingredientCommand.getQuantity().equals(0)){
                ingredientCommandId.add(ingredientCommand.getId());
            }
        });

        dishCommand.getIngredientCommands().removeIf(i -> !ingredientCommandId.add(i.getId()) && i.getQuantity().equals(0));
    }






    @Override
    public Order updateOrderDishCart(OrderDishCommand orderDishFromTemplate) {
       // System.out.println(shoppingCart.getOrderDishes().size());
      //  System.out.println(shoppingCart);
    shoppingCart.getOrderDishes().removeIf(orderDish1 -> orderDish1.getId().equals(orderDishFromTemplate.getId()));
     //   System.out.println(shoppingCart.getOrderDishes().size());
     //   System.out.println(shoppingCart);
        orderDishFromTemplate.setQuantity(orderDishFromTemplate.getDish().getQuantity());
        orderDishFromTemplate.setSingleDishCost(orderDishFromTemplate.getDish().getCost());
        OrderDish orderDish=orderDishMapper.orderDishCommandToOrderDish(orderDishFromTemplate);

        orderDishFromTemplate.getDish().getIngredientCommands().forEach(ingredientCommand -> {

            if(!ingredientCommand.getQuantity().equals(0)) {
                OrderIngredient orderIngredient = new OrderIngredient();
                orderIngredient.setQuantity(ingredientCommand.getQuantity());
                orderIngredient.setOrderDish(orderDish);
                orderIngredient.setIngredientDishOrderCost(ingredientCommand.getCost());
                orderIngredient.setIngredient(ingredientMapper.ingredientCommandToIngredient(ingredientCommand));
                orderDish.getOrderIngredients().add(orderIngredient);
            }
        });


        //adding temporary id to orderDish of Shopping Cart

        orderDish.setId(orderDishFromTemplate.getId());


        shoppingCart.getOrderDishes().add(orderDish);


        return shoppingCart;

    }
}
