package org.javaboy.vhr.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.javaboy.vhr.model.WebLog;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;


@Component
@Aspect
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * org.javaboy.vhr.controller.WsController.*(..))")
    public void excludeWebSocket(){

    }

    //..代表当前包和子包，如果用。代表该包下，而controller里还有其他包，所以只在登陆是记录了，其他没记录
    @Pointcut("execution(public * org.javaboy.vhr.controller..*.*(..))&&!excludeWebSocket()")
    public void WebLog(){
    }


    @Around("WebLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Date startTime=new Date();
        ServletRequestAttributes servletRequestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Date endTime=new Date();
        //request.getRequestURL()返回StringBuilder，toString转成String类型
        String url = request.getRequestURL().toString();
        String baseUrl=StrUtil.removeSuffix(url, URLUtil.url(url).getPath());
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        WebLog webLog=new WebLog();
        if (method.isAnnotationPresent(ApiOperation.class)){
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            webLog.setDescription(apiOperation.value());
        }
        Object proceed = joinPoint.proceed();
        webLog.setBasePath(baseUrl);
        webLog.setMethod(request.getMethod());
        webLog.setStartTime(startTime);
        webLog.setEndTime(endTime);
        webLog.setIp(request.getRemoteUser());
        webLog.setParameter(Arrays.toString(joinPoint.getArgs()));
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(url);
        //Idea自带的Jackson将对象转为json
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("{}",objectMapper.writeValueAsString(webLog));
        return proceed;
    }
}

