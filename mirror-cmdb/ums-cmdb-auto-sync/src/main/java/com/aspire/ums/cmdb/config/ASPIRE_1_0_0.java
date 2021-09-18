package com.aspire.ums.cmdb.config;

import java.lang.annotation.*;

	/**
	 * 注解控制是否暴露在swagger中
	 */
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface ASPIRE_1_0_0 {
	}