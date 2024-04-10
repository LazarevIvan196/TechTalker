package com.example.techtalker.logs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(public * com.example.techtalker.services.*.*(..))")
    public void logBeforeServiceMethods(JoinPoint joinPoint) {
        logger.info("Выполнение: {}", joinPoint.getSignature().toShortString());
    }

    @After("execution(public * com.example.techtalker.services.*.*(..))")
    public void logAfterServiceMethods(JoinPoint joinPoint) {
        logger.info("Завершено {}: ", joinPoint.getSignature().toShortString());
    }
}
