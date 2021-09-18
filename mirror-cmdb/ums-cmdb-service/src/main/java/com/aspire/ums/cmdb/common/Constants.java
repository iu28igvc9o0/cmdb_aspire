package com.aspire.ums.cmdb.common;

import java.util.Map;

/**
 * 公共静态变量
 * 
 * @author lupeng
 * 
 */
public class Constants {

	// 表单类型
	public static final String MODULE_FORM_TYPE_SINGLEROWTEXT = "singleRowText"; // 单行文本

	public static final String MODULE_FORM_TYPE_MULTIROWTEXT = "multiRowText"; // 多行文本

	public static final String MODULE_FORM_TYPE_RICHTEXT = "richText"; // 富本文

	public static final String MODULE_FORM_TYPE_LISTSEL = "listSel"; // 下拉菜单

	public static final String MODULE_FORM_TYPE_SINGLESEL = "singleSel"; // 单选

	public static final String MODULE_FORM_TYPE_MULTISEL = "multiSel"; // 多选

	public static final String MODULE_FORM_TYPE_DOUBLE = "double"; // 小数

	public static final String MODULE_FORM_TYPE_INT = "int"; // 整数

	public static final String MODULE_FORM_TYPE_IMAGE = "image"; // 图片

	public static final String MODULE_FORM_TYPE_FILE = "file"; // 附件

	public static final String MODULE_FORM_TYPE_DATETIME = "dateTime"; // 时间

	public static final String MODULE_FORM_TYPE_TABLE = "table"; // 表格

	public static final String MODULE_FORM_TYPE_CASCADER = "cascader"; // 级联

	public static final String MODULE_FORM_TYPE_GROUPLINE = "groupLine"; // 属性分组

	// 参数类型
	public static final String MODULE_FORM_PARAM_MINLENGTH = "minLength"; // 最小长度

	public static final String MODULE_FORM_PARAM_MAXLENGTH = "maxLength"; // 最大长度

	public static final String MODULE_FORM_PARAM_VALIDATION = "validation"; // 格式

	public static final String MODULE_FORM_PARAM_MIN = "min"; // 最小值

	public static final String MODULE_FORM_PARAM_MAX = "max"; // 最大值

	public static final String MODULE_FORM_PARAM_PRECISION = "precision"; // 小数位数

	public static final String MODULE_FORM_PARAM_FORMATDATE = "formatDate"; // 日期格式化

	public static Map<String, String> relationMap;

	static {
		relationMap.put("OneToOne", "一对一");
		relationMap.put("OneToMany", "一对多");
		relationMap.put("ManyToOne", "多对一");
		relationMap.put("ManyToMany", "多对多");

		relationMap.put("一对一", "OneToOne");
		relationMap.put("一对多", "OneToMany");
		relationMap.put("多对一", "ManyToOne");
		relationMap.put("多对多", "ManyToMany");
	}

	public static final String MODULE_RELATION_SOURCE = "source_module";
	
	public static final String MODULE_RELATION_TARGET = "target_module";
	
	public static final String MODULE_RELATION_ONETOONE = "OneToOne";
	
	public static final String MODULE_RELATION_ONETOMANY = "OneToMany";
	
	public static final String MODULE_RELATION_MANYTOONE = "ManyToOne";
	
	public static final String MODULE_RELATION_MANYTOMANY = "ManyToMany";
	
    public static final String LOG_ACTION_TYPE_ADDINSTANCE_RELATION_NAME = "建立关系";
    
    public static final String LOG_ACTION_TYPE_DELINSTANCE_RELATION_NAME = "删除关系";
    
    public static final String LOG_ACTION_TYPE_ADDINSTANCE_NAME = "新建配置";
    
    public static final String LOG_ACTION_TYPE_DELINSTANCE_NAME = "删除配置";
    
    public static final String LOG_ACTION_TYPE_UPDATEINSTANCE_NAME = "修改配置";
    
    public static final String LOG_ACTION_TYPE_HANDUPINSTANCE_NAME = "移交配置";
    
    public static final String MODOULE_RELATION_DIRECT_UP = "UP";
    
    public static final String MODOULE_RELATION_DIRECT_DOWN = "DOWN";
	
}
