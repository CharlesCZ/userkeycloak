package org.czekalski.userkeycloak.service;

import org.czekalski.userkeycloak.commadPattern.command.OrderDishCommand;
import org.czekalski.userkeycloak.commadPattern.command.OrderIngredientCommand;
import org.czekalski.userkeycloak.commadPattern.mapper.OrderIngredientMapper;
import org.czekalski.userkeycloak.model.OrderDish;
import org.czekalski.userkeycloak.model.OrderIngredient;
import org.czekalski.userkeycloak.repository.OrderIngredientRepository;
import org.springframework.stereotype.Service;


@Service
public class OrderIngredientServiceImpl implements OrderIngredientService {

    private final OrderIngredientRepository orderIngredientRepository;
    private final OrderIngredientMapper orderIngredientMapper;

    public OrderIngredientServiceImpl(OrderIngredientRepository orderIngredientRepository, OrderIngredientMapper orderIngredientMapper) {
        this.orderIngredientRepository = orderIngredientRepository;
        this.orderIngredientMapper = orderIngredientMapper;
    }

    @Override
    public void deleteById(Long orderIngredientId) {

        orderIngredientRepository.deleteById(orderIngredientId);
    }

    @Override
    public OrderIngredientCommand updateOrderIngredientById(Long orderIngredientId) {

        return  orderIngredientRepository.findById(orderIngredientId)
                .map(orderIngredientMapper::orderIngredientToOrderIngredientCommand).orElseThrow(RuntimeException::new);

    }

    @Override
    public OrderIngredient updateOrderIngredient(OrderIngredientCommand orderIngredient) {
       OrderIngredient returnedOrderIngredient= orderIngredientRepository.findById(orderIngredient.getId()).orElseThrow(RuntimeException::new);
       returnedOrderIngredient.setQuantity(orderIngredient.getQuantity());
        returnedOrderIngredient.setIngredientDishOrderCost(orderIngredient.getIngredientDishOrderCost());
        return orderIngredientRepository.save(returnedOrderIngredient);
    }
}
