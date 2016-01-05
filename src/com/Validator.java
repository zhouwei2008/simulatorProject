package com;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {
	String defaultregx() default "";//"[^$&=~]+";
	String regx() default "";
	int maxsize() default 0;
	int length() default 0;
	boolean nullable() default true;
	boolean list() default true; //显示规则
	boolean throwable() default true; //违反规则是否抛异常
	String message() default "非法字符";
}


