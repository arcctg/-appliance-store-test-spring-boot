package com.epam.rd.autocode.assessment.appliances.advice;

import com.epam.rd.autocode.assessment.appliances.exception.ClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
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

    @ExceptionHandler(ClientNotFoundException.class)
    public void handleUserNotFoundException(ClientNotFoundException ex) {
        logger.error(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleUsernameNotFoundException(UsernameNotFoundException ex) {
        logger.info(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        model.addAttribute("message", "An unexpected error occurred: " + ex.getMessage());

        return "error/generalError";
    }
}
