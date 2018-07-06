package com.ourcompany.aop.aspectj;


import com.ourcompany.R;
import com.ourcompany.aop.annotation.CheckIsLogin;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.ToastUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 上午11:25
 * Des    : 检查是否登录
 */
@Aspect
public class CheckIsLoginTrace {

    /**
     * 找到处理的切点
     * 这个 * * *(..) 可以处理CheckLogin这个注解的任意类 任意方法 的任意参数
     */
    @Pointcut("execution(@com.ourcompany.aop.annotation.CheckIsLogin * *(..))")
    public void executionCheckIsLogin() {
    }

    /**
     * 处理切面编程
     */
    @Around("executionCheckIsLogin()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        LogUtils.e("sen","checkLogin******");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckIsLogin checkLogin = signature.getMethod().getAnnotation(CheckIsLogin.class);
        if (checkLogin != null) {
            if (MServiceManager.getInstance().isUserLogin()) {
                return joinPoint.proceed();
            } else {
                ToastUtils.showSimpleToast(ResourceUtils.getString(R.string.str_user_not_login));
                return null;
            }
        }
        return null;
    }


}

