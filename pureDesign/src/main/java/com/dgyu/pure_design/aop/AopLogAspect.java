package com.dgyu.pure_design.aop;


import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.UserAgent;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @ClassName AopLogAspect
 * @Description 使用 aop 切面记录请求日志信息
 * @Author dgyu
 * @Date 2022/4/13
 */
@Aspect
@Component
@Slf4j
public class AopLogAspect {

    private static final String START_TIME = "request-start";
    private static final String URL_NAME = "urlName";
    private static final String IP_NAME = "IpName";
    private static final String CLASS_NAME = "className";
    private static final String METHOD_NAME = "methodName";
    private static final String PARAM_STR = "paramStr";

    // 参数可以临时存储在threadLocal
    public static final ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal();

    public void setThreadLocal(Map<String, String> map) {
        threadLocal.set(map);
    }


    public Map<String, String> getThreadLocal() {
        Map<String, String> map = threadLocal.get();
        threadLocal.remove();
        return map;
    }


    /**
     * 切入点
     */
    @Pointcut("execution(public * com.dgyu..*.*Controller.*(..))")
    public void log() {

    }


    /**
     * 前置操作
     *
     * @param point 切入点
     */
    @Before("log()")
    public void beforeLog(JoinPoint point) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // 获取参数值
        Object[] args = point.getArgs();
        // 获取当前类的对象
        String classType = point.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();
        String methodName = point.getSignature().getName(); // 获取方法名称
        // 获取参数名称和值
        Map<String, Object> nameAndArgs = this.getFieldsName(this.getClass(), clazzName, methodName, args);
        // 根据获取到的值所属的不同类型通过两种不同的方法获取参数
        boolean flag = false;
        if (nameAndArgs != null && nameAndArgs.size() > 0) {
            for (Map.Entry<String, Object> entry : nameAndArgs.entrySet()) {
                log.info(">>>>>>>>>>>>>>类型" + entry.getValue().getClass().toString());
                if (entry.getValue() instanceof String) {
                    flag = true;
                    break; // 跳出循环
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        if (flag) {
            // 从Map中获取
            sb.append(JSONObject.toJSONString(nameAndArgs)).append(",");
        } else {
            if (args != null) {
                for (Object object : args) {
                    if (object != null) {
                        if (object instanceof MultipartFile || object instanceof ServletRequest || object instanceof ServletResponse) {
                            continue;
                        }
                        sb.append(JSONObject.toJSONString(object)).append(",");
                    }
                }
            }
        }
        String arg = "";
        if (StringUtils.isEmpty(sb.toString())) {
            arg = null;
        } else {
            arg = sb.toString().substring(0, sb.toString().length() - 1);
        }

        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        log.info("【AOP 请求 URL】：{}", request.getRequestURL());
        log.info("【AOP 请求 IP】：{}", request.getRemoteAddr());
        log.info("【AOP 请求类名】：{}，", point.getSignature().getDeclaringTypeName());
        log.info("【AOP 请求方法名】：{}", point.getSignature().getName());
        log.info("【token】：{}", request.getHeader("token"));
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("【AOP 请求参数】：{}", arg);
        Long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);

        request.setAttribute(URL_NAME, request.getRequestURL());
        request.setAttribute(IP_NAME, request.getRemoteAddr());
        request.setAttribute(CLASS_NAME, point.getSignature().getDeclaringTypeName());
        request.setAttribute(METHOD_NAME, point.getSignature().getName());
        request.setAttribute(PARAM_STR, JSONObject.toJSONString(parameterMap));

    }


    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {

        Object result = point.proceed();
        log.info("【返回值】：{}", JSONObject.toJSONString(result));
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        Long start = (Long) request.getAttribute(START_TIME);

        Long end = System.currentTimeMillis();
        log.info("【请求耗时】：{}毫秒", end - start);

        return result;
    }

    /**
     * 后置操作
     */
    @AfterReturning("log()")
    public void afterReturning() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        log.info("【浏览器类型】：{}，【操作系统】：{}，【原始User-Agent】：{}", userAgent.getBrowser().toString(), userAgent.getOperatingSystem().toString(), header);
    }


    /**
     * @return Map<String, Object>
     * @Description 获取字段名和字段值
     */
    private Map<String, Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);
        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);// paramNames即参数名
        }
        return map;
    }

}
