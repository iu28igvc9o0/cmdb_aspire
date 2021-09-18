package com.aspire.mirror.scada.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class ScadaCanvasRes implements Serializable {

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
	private String precinctId;

	/**
	 * 页面类型 0普通页面  1模板
	 */
	private Integer pageType;

	/**
	 * 视图内容
	 */
	private String content;

	/**
	 * 视图类型名称，是picture_type的中文描述
	 */
	private String pictureName;

	private String idc;

	private String pod;

	private String bizSystem;

	private String bindName;

	private String isDefault;

	private String projectName;
}
