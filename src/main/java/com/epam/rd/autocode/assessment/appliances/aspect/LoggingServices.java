package com.epam.rd.autocode.assessment.appliances.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingServices {
    private static final Logger logger = LoggerFactory.getLogger(LoggingServices.class);

    @Before("@annotation(Loggable)")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Method {} called with arguments: {}", methodName, args);
    }

    @AfterReturning(pointcut = "@annotation(Loggable)", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method {} returned with result: {}", methodName, result);
    }
}
