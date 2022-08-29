package com.imooc.miaosha.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

	// java正则表达式通过java.util.regex包下的Pattern类与Matcher类实现
	// 括号里面的是规则
	// 然后把规则编译成模式对象
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		// Matcher类提供了对正则表达式的分组支持,以及对正则表达式的多次匹配支持，要想得到更丰富的正则匹配操作,那就需要将Pattern与Matcher联合使用。
		// 这个地方不用纠结，下面这两句和Pattern mobile_pattern = Pattern.compile("1\\d{10}");
		// 构成一个整体，直接理解为字符串匹配就行，详见https://www.cnblogs.com/dgwblog/p/10073256.html
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
	
//	public static void main(String[] args) {
//			System.out.println(isMobile("18912341234"));
//			System.out.println(isMobile("1891234123"));
//	}
}
