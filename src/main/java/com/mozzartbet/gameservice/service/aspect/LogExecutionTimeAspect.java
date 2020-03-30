package com.mozzartbet.gameservice.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogExecutionTimeAspect {
	
	@Pointcut("execution(public * *(..))")
	public void publicMethod() {
		
	}

	@Pointcut("within(@com.mozzartbet.gameservice.annotation.LogExecutionTime *)")
	public void annotatedClass() {
			
	}
	
	@Pointcut("@annotation(com.mozzartbet.gameservice.annotation.LogExecutionTime)")
	public void annotatedMethod() {
		
	}

	@Around("(annotatedClass() && publicMethod()) || annotatedMethod()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

		Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());

		long start = System.currentTimeMillis();

		Object proceed = joinPoint.proceed();

		long executionTime = System.currentTimeMillis() - start;

		logger.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");
		return proceed;
	}

}
