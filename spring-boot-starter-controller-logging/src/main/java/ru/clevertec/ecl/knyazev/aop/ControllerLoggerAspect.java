package ru.clevertec.ecl.knyazev.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@Aspect
@Slf4j
public class ControllerLoggerAspect {

    private final static String REQUEST_LOG_MESSAGE = "Request: ";
    private final static String RESPONSE_LOG_MESSAGE = "Response: ";

    private static final String MESSAGE_BRACES = "{}";

	private static final String UNKNOWN_ERROR = "Unknown error";

    @Pointcut(value = "execution(org.springframework.http.ResponseEntity ru.clevertec.ecl.knyazev.controller.*Controller.*(..))")
    public void allControllerMethods() {
    }

    @Around(value = "allControllerMethods()")
    public ResponseEntity<?> logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        String controllerName = joinPoint.getTarget().getClass().getName();

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();


        log.info(String.format("%s - %s %s",
                controllerName,
                REQUEST_LOG_MESSAGE,
                MESSAGE_BRACES), methodName + Arrays.toString(args));

        try {
            Object objectResult = joinPoint.proceed();

            ResponseEntity<?> responseEntity = objectResult != null
                    ? (ResponseEntity<?>) objectResult
                    : null;

            log.info(String.format("%s - %s%s",
                    controllerName,
                    RESPONSE_LOG_MESSAGE,
                    MESSAGE_BRACES), responseEntity);

            return responseEntity;

        } catch (Throwable t) {
            log.error(String.format("%s - %s%s",
                            controllerName,
                            RESPONSE_LOG_MESSAGE,
                            MESSAGE_BRACES),
                    (t.getMessage() != null ? t.getMessage() : UNKNOWN_ERROR), t);
            throw t;
        }
    }
}
