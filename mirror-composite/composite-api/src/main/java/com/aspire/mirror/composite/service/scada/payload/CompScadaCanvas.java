package com.aspire.mirror.composite.service.scada.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * monitor_canvas新增对象类
 * <p>
 * 项目名称: mirror平台
 * 包:     com.aspire.mirror.scada.entity
 * 类名称:     ScadaCanvasRes
 * 类描述:     monitor_canvas返回对象
 * 创建时间:     2019-06-11 11:32:23
 * @author JinSu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompScadaCanvas implements Serializable {

	/**
	 * 视图ID
	 */
	private String id;

	/**
	 * 视图标题
	 */
	private String name;

	/**
	 * 图片类型 0普通背景图  1拓扑图背景图
	 */
	private Integer pictureType;

	/**
	 * 监控对象id
	 */
//	private String precinctId;

	/**
	 * 页面类型 0普通页面  1模板
	 */
	private Integer pageType;

	/**
	 * 视图内容
	 */
	private String content;


	private String idc;

	private String pod;

	private String bizSystem;



	private String isDefault;

	private Date createTime;
	private Date updateTime;

	private String projectName;

	private String bindObj;
}
