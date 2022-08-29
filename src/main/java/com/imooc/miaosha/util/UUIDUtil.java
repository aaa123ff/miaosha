package com.imooc.miaosha.util;

import java.util.UUID;

public class UUIDUtil {
	public static String uuid() {
		// UUID 通用唯一识别码，UUID 的目的是让分布式系统中的所有元素都能有唯一的识别信息。
		// UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法
		return UUID.randomUUID().toString().replace("-", "");
	}
}
