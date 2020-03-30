package com.mozzartbet.gameservice.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogExecutedMethodAspect {

	@Around("execution(* com.mozzartbet.gameservice..*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

		Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());

		Object proceed = joinPoint.proceed();

		logger.info("Executed method: "+joinPoint.getSignature());
		return proceed;
	}
}
