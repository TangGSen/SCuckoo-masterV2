package com.ourcompany.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     :2018/6/1 上午11:42
 * Des    :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckIsLogin {
}
