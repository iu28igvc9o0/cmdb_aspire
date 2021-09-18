package com.aspire.mirror.common.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilHelp {
	/**
	 * slf4j
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilHelp.class);

	/**
	 * 实体对象属性非空检查
	 * 
	 * @param obj
	 *            实体对象
	 * @return boolean 对象是的字段是否全部为空
	 * @throws IllegalAccessException
	 */
	public static boolean checkObjFieldIsNull(Object obj) {
		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.get(obj) == null || "".equals(f.get(obj))) {
					return true;
				}
			} catch (IllegalAccessException e) {
				LOGGER.warn("UtilHelp.checkObjFieldIsNull(obj)异常", e);
			}
		}
		return false;
	}
}
