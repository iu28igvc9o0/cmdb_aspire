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
 * 类名称:     ScadaCanvasReq
 * 类描述:     monitor_canvas请求对象
 * 创建时间:     2019-06-11 11:32:23
 * @author JinSu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScadaCanvasReq implements Serializable {

	/**
	 * 视图ID
	 */
	private String id;

	/**
	 * 视图标题
	 */
	private String name;


	/**
	 * 1:物理拓扑，2：逻辑拓扑，3：租户拓扑
	 */
	private Integer pictureType;

//	/**
//	 * 监控对象id
//	 */
//	private String precinctId;

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

//	/**
//	 * 绑定对象名称，是precinct_id的中文描述
//	 */
//	private String precinctName;
	private String idc;

	private String pod;

	private String bizSystem;

	private String bindName;

	private String isDefault;

	private String projectName;

	private String bindObj;
}
