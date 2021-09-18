package com.aspire.mirror.scada.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 持久对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.scada.entity   
 * 类名称:     Canvas
 * 类描述:    持久类，定义与表字段对应的属性
 * 创建时间:     2019-06-11 11:32:23
 * @author JinSu
 */
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScadaCanvas implements Serializable{
	/**
	 * 视图ID
	 */
	private String id;

	/**
	 * 视图标题
	 */
	private String name;

//	/**
//	 * X轴坐标
//	 */
//	private String xCoordinate;
//
//	/**
//	 * X轴坐标
//	 */
//	private String yCoordinate;
//
//	/**
//	 * 宽度
//	 */
//	private String width;
//
//	/**
//	 * 高度
//	 */
//	private String height;
//
//	/**
//	 * 背景色
//	 */
//	private String backgroundColor;
//
//	/**
//	 * 背景图片
//	 */
//	private String backgroundPictureUrl;

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

	private List<String> bizSystemList;

	private String isDefault;

	private Date createTime;
	private Date updateTime;

	private String projectName;

	private String bindObj;
}
