package com.marcus.core.aspect;

import com.marcus.base.bean.SecuritySubject;
import com.marcus.entity.BaseEntity;
import com.marcus.security.SecurityService;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 16:27
 **/
@Aspect
@Component
public class MybatisAspect {
    @Autowired(required = false)
    private SecurityService securityService;

    @Pointcut("execution(* com.marcus..mapper..insert(..))")
    public void insertEntity() { }

    @Pointcut("execution(* com.marcus..mapper..update(..))")
    public void updateEntity() { }

    @Pointcut("execution(* com.marcus..mapper..updateById(..))")
    public void updateEntityById() { }

    @Pointcut("insertEntity() || updateEntity() || updateEntityById()")
    public void saveOrUpdate(){ }

    @Around("saveOrUpdate() && target(tar) && args(entity)")
    public Object aroundSave(ProceedingJoinPoint point, Object tar, Object entity) throws Throwable {
        if ((entity instanceof BaseEntity)) {
            updateBaseInfo(entity);
        } else if (entity instanceof Iterable){
            //批量保存记录
            Iterable iterable = (Iterable) entity;
            for (Iterator iter = iterable.iterator(); iter.hasNext();) {
                Object obj = iter.next();
                if (obj instanceof BaseEntity) {
                    updateBaseInfo(obj);
                }
            }
        }
        return point.proceed();
    }

    private void updateBaseInfo(Object entity) throws IllegalAccessException, InvocationTargetException {
        if(securityService!=null) {
            String sessionId = getCurrentSessionId();
            if(sessionId!=null) {
                SecuritySubject securitySubject = securityService.getLoginSubject(getCurrentSessionId());
                if (securitySubject!=null) {
                    BeanUtils.setProperty(entity, "operatorId", securitySubject.getUserId());
                    BeanUtils.setProperty(entity, "operatorName", securitySubject.getLoginName());
                }
            }
        }
        BeanUtils.setProperty(entity, "createTime", new Date());
        BeanUtils.setProperty(entity, "updateTime", new Date());
    }

    /**
     * 获取上下文会话id
     * @return
     */
    public static String getCurrentSessionId(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest().getSession().getId();
    }
}
