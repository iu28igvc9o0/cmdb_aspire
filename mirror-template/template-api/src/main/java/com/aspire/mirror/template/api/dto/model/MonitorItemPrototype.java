package com.aspire.mirror.template.api.dto.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: template-api 
 * <p/>
 * 
 * 类名: MonitorItemPrototype
 * <p/>
 *
 * 类功能描述: 巡检指标原型对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年1月27日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class MonitorItemPrototype {
	// 系统巡检导出excel时需要剔除的列
	public static final String[] EXECL_EXCLUDE_CRUISE_SYSTEM 	= {"检查配置项", "基线标准", "资源池"};  
	// 基线巡检导出excel时需要剔除的列
	public static final String[] EXECL_EXCLUDE_CRUISE_JW  		= {"检查配置项", "基线标准", "是否基线"};  
	// 交维巡检导出excel时需要剔除的列
	public static final String[] EXECL_EXCLUDE_CRUISE_BASELINE	= {"资源池", "检查指标分类", "是否基线"};  
	
	public static final String PROTOTYPE_LABEL_CRUISE_SYSTEM	= "cruise_system";
	public static final String PROTOTYPE_LABEL_CRUISE_JW		= "cruise_jw";
	public static final String PROTOTYPE_LABEL_CRUISE_BASELINE	= "cruise_baseline";
	
	@JsonProperty("id")
	private Long	id;
	
	@JsonProperty("pool_id")
	private String	poolId;					// 资源池id
	
	@Excel(name = "资源池", width = 20)
	@JsonProperty("pool_name")
	private String	poolName;				// 资源池name
	
//	@Excel(name = "巡检指标分类", width = 20)
//	@JsonProperty("cruise_classify")
//	private String	cruiseClassify;			// 巡检指标分类
	
	@Excel(name = "检查指标分类", width = 20)
	@JsonProperty("check_classify")
	private String	checkClassify;			// 检查指标分类
	
	@Excel(name = "检查配置项", width = 20)
	@JsonProperty("check_config_params")
	private String	checkConfigParams;		// 检查配置项
	
	@Excel(name = "检查指标分组", width = 20)
	@JsonProperty("check_group")
	private String	checkGroup;				// 检查指标分组
	
	@Excel(name = "基线标准", width = 20, replace = {"两部委_twoParties"})
	@JsonProperty("baseline_standard")
	private String	baselineStandard;		// 基线标准		twoParties: 两部委
	
	@Excel(name = "是否基线", width = 20, replace = {"是_Y", "否_N"})
	@JsonProperty("baseline_flag")
	private String	baselineFlag;			// 是否基线
	
	@Excel(name = "是否多行处理", width = 20, replace = {"是_Y", "否_N"})
	@JsonProperty("multiline_flag")
	private String	multilineFlag;			// 是否多行处理
	
	@JsonProperty("prototype_label")
	private String	prototypeLabel;			// 原型标签：  cruise_system 系统巡检     cruise_baseline 基线巡检     cruise_jw 交维巡检
	
	@JsonProperty("item_type")
	private String	itemType;				// 指标项类型    SCRIPT-脚本（现有） INDEX-监控指标（现有）
	
	@Excel(name = "巡检指标", width = 20)
	@JsonProperty("item_name")
	private String	itemName;				// 指标名
	
	@Excel(name = "优先级", width = 20, replace = {"低_1", "中_2", "高_3"})
	@JsonProperty("priority")
	private String	priority;				// 优先级
	
	@JsonProperty("script_id")
	private String	scriptId;				// 引用的脚本id
	
	@Excel(name = "对应脚本", width = 20)
	@JsonProperty("script_name")
	private String	scriptName;
	
	@JsonProperty("script_content_type")
	private Integer	scriptContentType;		// 脚本内容类型, 与自动化脚本类型相同      1 sh 2 bat 3 python  
	
	@JsonProperty("script_group_name")
	private String	scriptGroupName;		// 脚本所在的group名称
	
	@JsonProperty("item_ext")
	private ItemExt	itemExt;				// 参数定义
	
	@Excel(name = "指标项运算符", width = 20)
	@JsonProperty("logic_sign")
	private String	logicSign;				// 指标运算符
	
	@JsonProperty("expression")
	private String	expression;				// 阈值条件表达式
	
	@JsonProperty("default_threshold_val")
	private String	defaultThresholdVal;	// 默认阈值
	
	@Excel(name = "描述", width = 25)
	private String	description;
	
	@Excel(name = "状态", width = 20, replace = {"启用_ON", "禁用_OFF"})
	@JsonProperty("status")
	private String	status;					// 状态开关   ON 启用    OFF 禁用
	
	@JsonProperty("create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date 	createTime;
	
	@JsonProperty("update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	updateTime;
	
	public ItemExt getItemExt() {
		return itemExt == null ? new ItemExt() : itemExt;
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class MonitorItemPrototypeQueryModel extends AbsPageQueryParamsAware {
		
		@JsonProperty("prototype_label")
		private String	prototypeLabel;	
		
		@JsonProperty("status")
		private String status;		// 状态开关   ON 启用    OFF 禁用
		
		@JsonProperty("item_name_like")
		private String	itemNameLike;	
		
		@JsonProperty("description_like")
		private String	descriptionLike;
	}
}
