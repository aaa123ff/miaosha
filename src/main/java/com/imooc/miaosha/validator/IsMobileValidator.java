package com.imooc.miaosha.validator;

import com.imooc.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


// 定义校验类，实现ConstraintValidator接口，接口使用了泛型
// 需要指定两个参数，第一个是自定义注解，第二个是需要校验的数据类型
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	// 是否强制校验
	private boolean required = false;

	// initialize方法主要做一些初始化操作，
	// 它的参数是我们使用到的注解，可以获取到运行时的注解信息
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtil.isMobile(value);
		}else {
			if(StringUtils.isEmpty(value)) {
				return true;
			}else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
