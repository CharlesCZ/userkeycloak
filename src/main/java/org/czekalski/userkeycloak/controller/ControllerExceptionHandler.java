package org.czekalski.userkeycloak.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView handleNumberFormatException(Exception exception){

        log.error("Handling Number Format Exception");
        log.error(exception.getMessage());
        ModelAndView modelAndView=new ModelAndView("400view");
        modelAndView.addObject("exception",exception);

        return modelAndView;
    }

}
