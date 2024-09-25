package ru.discomfortdeliverer.lesson_five.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogExecutionTimeAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogExecutionTimeAspect.class);

    @Around("@within(LogExecutionTime) || @annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        logger.info("Вызван мето {} класса {}. Время выполнения: {} mc", methodName, className, executionTime);

        return proceed;
    }
}
