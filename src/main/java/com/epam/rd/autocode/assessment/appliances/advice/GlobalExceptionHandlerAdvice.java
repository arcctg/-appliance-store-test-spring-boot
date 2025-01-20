package com.epam.rd.autocode.assessment.appliances.advice;

import com.epam.rd.autocode.assessment.appliances.exception.NotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice(basePackages = "com.epam.rd.autocode.assessment.appliances.controller.web")
public class GlobalExceptionHandlerAdvice {
    private static final String ERROR_MESSAGE = "An unexpected error occurred: ";
    private static final String MESSAGE = "message";
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
    public String handleNotFoundException(RuntimeException ex, Model model) {
        String message = ex.getMessage();

        model.addAttribute(MESSAGE, message);
        logger.error(message);

        return "error/generalError";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleNotValidException(MethodArgumentNotValidException ex, Model model) {
        String message = getErrorMessage(ex);

        model.addAttribute(MESSAGE, message);
        logger.warn(message);

        return "error/notValidError";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        String message = ERROR_MESSAGE + ex.getMessage();

        model.addAttribute(MESSAGE, message);
        logger.error(message, ex);

        return "error/generalError";
    }

    private String getErrorMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
