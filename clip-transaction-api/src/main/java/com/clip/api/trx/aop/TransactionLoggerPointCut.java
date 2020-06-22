package com.clip.api.trx.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class TransactionLoggerPointCut {

	private static final Logger LOGGER = LoggerFactory.getLogger("TransactionLogger");			
	
	@Before("execution(* com.clip.api.trx..*(..))")
	public void logControllerMethodAccess(JoinPoint jp) {
		LOGGER.info("Executing Method : {} ", jp.getSignature().toShortString());
	}
	
	@AfterReturning(pointcut="execution(* com.clip.api.trx.controller..*(..))", returning = "retVal")
	public void logControllerMethodReturn(JoinPoint jp, Object retVal) {
		if (null != retVal) {
			LOGGER.info("Controller Returning : {} ", retVal.toString());
		}
	}
	
}
