package com.hua.common.aspect;

import com.alibaba.fastjson.JSON;
import com.hua.common.annotation.OptLog;
import com.hua.mapper.OperationLogMapper;
import com.hua.pojo.entity.OperationLog;
import com.hua.util.IpUtils;
import com.hua.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 操作日志切面处理
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/15 22:51
 */
@Aspect
@Component
public class OptLogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 设置操作日志切入点，记录操作日志，在注解的位置切入代码
     */
    @Pointcut("@annotation(com.hua.common.annotation.OptLog)")
    public void optLogPointCut() {}

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行，如果连接点抛出异常，则不会执行
     * @param joinPoint 切入点
     * @param key 响应结果
     */
    @AfterReturning(value = "optLogPointCut()", returning = "key")
    public void saveOptLog(JoinPoint joinPoint, Object key) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request =
                (HttpServletRequest) Objects.requireNonNull(requestAttributes)
                        .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取织入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        // 操作模块
        operationLog.setOptModule(api.tags()[0]);
        // 操作类型
        operationLog.setOptType(optLog.optType());
        // 操作描述
        operationLog.setOptDesc(apiOperation.value());
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方式
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        // 请求方法
        operationLog.setOptMethod(methodName);
        // 请求参数
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
        // 响应结果
        operationLog.setResponseData(JSON.toJSONString(key));
        // 请求用户id
        operationLog.setUserId(UserUtils.getLoginUser().getId());
        // 请求用户昵称
        operationLog.setNickname(UserUtils.getLoginUser().getNickname());
        // 请求Ip
        String ipAddr = IpUtils.getIpAddr(request);
        operationLog.setIpAddr(ipAddr);
        operationLog.setIpSource(IpUtils.getIpSource(ipAddr));
        // 请求url
        operationLog.setOptUrl(request.getRequestURI());
        operationLogMapper.insert(operationLog);
    }

}
