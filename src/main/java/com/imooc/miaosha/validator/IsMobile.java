package com.imooc.miaosha.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// @Target注解的作用目标： 接口，字段，方法，参数等
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
// @Retention注解的保留位置，
// RUNTIME:这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用。
@Retention(RUNTIME)
// @Document：说明该注解将被包含在javadoc中
@Documented
// @Constraint将此注解的验证逻辑交给IsMobileValidator来处理（见后面）
@Constraint(validatedBy = {IsMobileValidator.class })
// @interface不是接口是注解类
public @interface  IsMobile {

	// 是否强制校验
	boolean required() default true;
	// 校验不通过时的报错信息
	String message() default "手机号码格式错误";

	// 下面这两个类是必须要加的
	// 将validator进行分类，不同的类group中会执行不同的validator操作
	Class<?>[] groups() default { };
	// 主要是针对bean，很少使用
	Class<? extends Payload>[] payload() default { };
}
