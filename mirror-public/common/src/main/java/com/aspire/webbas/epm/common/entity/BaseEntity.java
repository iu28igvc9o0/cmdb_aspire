package com.aspire.webbas.epm.common.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称: [webbas-component-base]
 * 包名: [com.aspire.voms.base.entity]
 * 类名称: [BaseEntity]
 * 类描述: [基础领域类，可扩展一些公共字段]
 * 创建人: [王磊]
 * 创建时间: [2014年9月23日 下午5:46:01]
 */
public class BaseEntity {

	/**
	 * 动态字段. 在ibatis文件中可用“dynamicFields.xxx”方式读取动态字段值
	 */
	private Map dynamicFields = new HashMap();

	/** 排序字段 orderName order */
	private String orderName;
	private String order;

	/**
	 * 动态字段获取
	 * @return 字段map对象
	 */
	public Map getDynamicFields() {
		return dynamicFields;
	}

	/**
	 * @param dynamicFields 动态字段对象
	 */
	public void setDynamicFields(Map dynamicFields) {
		this.dynamicFields = dynamicFields;
	}

	/**
	 * 设置动态字段值.
	 * @param fieldName
	 *            字段名称
	 * @param value
	 *            字段值
	 */
	public void setField(String fieldName, Object value) {
		dynamicFields.put(fieldName, value);
	}

	/**
	 * 删除动态字段
	 * @param fieldName 字段key
	 */
	public void removeField(String fieldName) {
		dynamicFields.remove(fieldName);
	}

	/**
	 * 返回动态字段值.
	 * @param fieldName 字段名称
	 * @return 对象
	 */
	public Object getField(String fieldName) {
		if (dynamicFields == null) {
			return null;
		}
		return getDynamicFields().get(fieldName);
	}

	/**
	 * @return orderName
	 */
	public String getOrderName() {
		return orderName;
	}

	/**
	 * @param orderName 排序参数
	 */
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	/**
	 * @return order 排序类型
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order 排序类型
	 */
	public void setOrder(String order) {
		this.order = order;
	}
}
