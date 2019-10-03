package org.czekalski.userkeycloak.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundIngredientException extends RuntimeException {

    public NotFoundIngredientException() {
    }

    public NotFoundIngredientException(String message) {
        super(message);
    }

    public NotFoundIngredientException(String message, Throwable cause) {
        super(message, cause);
    }
}
