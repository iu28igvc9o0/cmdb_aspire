/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsAutoRepairScheme.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月19日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain.autorepair;

import java.util.Date;
import java.util.List;

import com.aspire.mirror.ops.api.domain.AbsPageQueryParamsAware;
import com.aspire.mirror.ops.api.domain.OpsGroupObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsAutoRepairSchemeDTO
 * <p/>
 *
 * 类功能描述: 自愈方案
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class OpsAutoRepairSchemeDTO extends OpsGroupObject {
	
	@JsonProperty("scheme_id")
	protected Long schemeId;

	@JsonProperty("scheme_name")
	protected String schemeName;
	
	@JsonProperty("multi_items_apply_time")
	protected Integer multiItemsApplyTime;
	
	@JsonProperty("refer_pipeline_count")
	protected Integer referPipelineCount;

	@JsonProperty("refer_pipeline_list")
	protected List<OpsApSchemePipelineDTO> referApPipelineList;
	
	@JsonProperty("remove_pipeline_list")
	protected List<Long> removePipelineList;
	
	@JsonProperty("refer_apitem_list")
	protected List<OpsApSchemeItemDTO> referApItemList;
	
	@JsonProperty("remove_apitem_list")
	protected List<Long> removeApItemList;
	
	@JsonProperty("description")
	protected String description;

	@JsonProperty("creater")
	protected String	creater;

	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date		createTime;

	@JsonProperty("updater")
	protected String	updater;

	@JsonProperty("update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date		updateTime;
	
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class OpsAutoRepairSchemeQueryModel extends AbsPageQueryParamsAware {
		
		@JsonProperty("scheme_name_like")
		private String	schemeNameLike;
		
		@JsonProperty("group_id_list")
	    private List<Long> groupIdList;
		
		@JsonProperty("create_time_start")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private String	createTimeStart;
		
		@JsonProperty("create_time_end")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private String	createTimeEnd;
	}
}
