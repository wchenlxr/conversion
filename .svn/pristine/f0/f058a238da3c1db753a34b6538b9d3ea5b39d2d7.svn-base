package com.adtech.cn.aspect;

import com.adtech.cn.aspect.support.IgnoreMethodEnum;
import com.adtech.cn.aspect.support.RestResult;
import com.adtech.cn.exception.ApplicationException;
import com.adtech.cn.exception.SystemError;
import com.adtech.cn.exception.support.IBaseError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面对controller层异常统一处理
 * Created by Administrator on 2018/3/22.
 */
@Component
@Aspect
@Order(1)
public class ThrowableAspect {

    private static long visitsNum = 0;

    private static long errVisitsNum = 0;

    private static final Logger logger = LoggerFactory.getLogger(ThrowableAspect.class);

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    @Pointcut("execution(* com.adtech.cn..*Controller.*(..))")
    public void throwable() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void annotation() {
    }

    @Around("throwable() && annotation()")
    public Object around(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        long currentMillis = System.currentTimeMillis();
        Object retVal;
        try {
            retVal = joinPoint.proceed();
            long time = System.currentTimeMillis() - currentMillis;
            visitsNum++;
            if (logger.isDebugEnabled()) {
                logger.debug("{} call {} service performed {} method, {} milliseconds.",
                        request.getRemoteHost(),
                        request.getRequestURI(),
                        joinPoint.getSignature().toShortString(),
                        System.currentTimeMillis() - currentMillis);
            }
            if (time > 500) {
                if (logger.isWarnEnabled()) {
                    logger.warn("{} call {} service performed {} method, {} milliseconds.",
                            request.getRemoteHost(),
                            request.getRequestURI(),
                            joinPoint.getSignature().toShortString(),
                            System.currentTimeMillis() - currentMillis);
                }
            }

        } catch (Throwable throwable) {
            errVisitsNum++;
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String name = method.getName();
            IBaseError error;
            if (!IgnoreMethodEnum.exist(name)) {
                if (throwable instanceof ApplicationException) {
                    ApplicationException ae = (ApplicationException) throwable;
                    error = ae.getError();
                } else {
                    error = SystemError.SYSTEM_ERROR;
                }
                retVal = RestResult.Fail(error, throwable.getStackTrace());
                if (logger.isErrorEnabled()) {
                    logger.error("{} call {} service performed {} method, {} milliseconds. Exception: {}",
                            request.getRemoteHost(),
                            request.getRequestURI(),
                            joinPoint.getSignature().toShortString(),
                            System.currentTimeMillis() - currentMillis,
                            throwable.getMessage());
                }
                if (logger.isDebugEnabled()) {
                    throwable.printStackTrace(System.out);
                }
            } else {
                error = SystemError.SYSTEM_OTHER_ERROR;
                logger.error(error.getCode() + ":" + error.getText() + " ; {}", throwable.getLocalizedMessage());
                retVal = null;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("正确请求数：{}，错误请求数：{}，总计请求数：{}", visitsNum, errVisitsNum, visitsNum + errVisitsNum);
        }
        if (retVal instanceof RestResult) {
            Map<String, String> map = new HashMap<>();
            map.put("code", ((RestResult) retVal).getCode());
            map.put("message", ((RestResult) retVal).getMessage());
            return gson.toJson(map);
        }
        return retVal;
    }
}
