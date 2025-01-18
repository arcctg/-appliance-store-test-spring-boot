package com.epam.rd.autocode.assessment.appliances.advice;

import com.epam.rd.autocode.assessment.appliances.exception.ClientNotFoundException;
import com.epam.rd.autocode.assessment.appliances.exception.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice(basePackages = "com.epam.rd.autocode.assessment.appliances.controller.web")
public class GlobalExceptionHandlerAdvice {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException() {
        return "error/403";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleResourceNotFoundException() {
        return "error/404";
    }

    @ExceptionHandler(InternalError.class)
    public String handleInternalError() {
        return "error/500";
    }

    @ExceptionHandler({UsernameNotFoundException.class, NotFoundException.class})
    public void handleNotFoundException(RuntimeException ex) {
        logger.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleNotValidException(MethodArgumentNotValidException ex, Model model) {
        String message = getErrorMessage(ex);

        model.addAttribute("message", message);
        logger.warn(message);

        return "error/notValidError";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        String message = "An unexpected error occurred: " + ex.getMessage();

        model.addAttribute("message", message);
        logger.error(message);
        ex.printStackTrace();

        return "error/generalError";
    }

    private String getErrorMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
    }
}
