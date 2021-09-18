package com.aspire.mirror.ops.api.domain.whitelist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.ops.api.domain.AbsPageQueryParamsAware;
import com.aspire.mirror.ops.api.domain.whitelist.WhitelistConst.WhitelistTypeEnum;
import com.aspire.mirror.ops.api.util.JsonUtil;
import com.aspire.mirror.ops.api.util.Md5Util;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsWhitelistCruiseCheck
 * <p/>
 *
 * 类功能描述: 巡检白名单对象
 * <p/>
 *
 * @author	xuxixuan
 *
 * @date	2021年3月9日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class OpsWhitelistCruiseCheck {
	
	

	
	@JsonProperty("id")
	private String	id;

	@JsonProperty("whitelist_type")
	private final WhitelistTypeEnum				whitelistType			= WhitelistTypeEnum.cruisecheck; //白名单类型，值固定为  cruisecheck
	
	
	
	@JsonProperty("script_id")
	private String	scriptId;         	//脚本id
	
	@JsonProperty("script_name")
	private String	scriptName;			//脚本名称

	@JsonProperty("script_content_type")
	private String	scriptContentType;	//脚本内容类型
	
	@JsonProperty("script_group_name")
	private String	scriptGroupName;    //分组信息
	
	@JsonProperty("item_id")
	private String	itemId;  			//巡检项id
	
	@JsonProperty("pool_id")
	private String	poolId;  			//资源池id
	
	@JsonProperty("pool_name")
	private String	poolName;  			//资源池名称
	
	@JsonProperty("item_name")
	private String	itemName;			//巡检项名称
	

	@JsonProperty("template_id")
	private String	templateId;			//巡检模板id

	@JsonProperty("template_name")
	private String	templateName;		//巡检模板名称

	
	@JsonProperty("status")
	private String 	status; 			// ON: 启用  	OFF: 禁用
	
	@JsonProperty("attachment")
	private String 	attachment;  		//附件备注
	
	@JsonProperty("creater")
	private String	creater;     		//创建人
	
	@JsonProperty("create_time")		
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	createTime;			//创建时间
	
	@JsonProperty("updater")
	private String	updater;			//更新者
	
	@JsonProperty("last_update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	lastUpdateTime;		//最后更新时间

	@JsonProperty("host_constraint_list")
	private final Set<OpsWhitelistConstraint>	hostConstraintList	= new HashSet<>();	// 关联的脚本约束

	@JsonProperty("os_type_constraint_list")
	private final Set<OpsWhitelistConstraint>	osTypeConstraintList	= new HashSet<>();	// 关联的作业约束
	
	@JsonProperty("remark")
	private String 								remark; 									// 备注
	
	@JsonProperty("attachment_list")
	private final List<String> 					attachmentList = new ArrayList<>(); 		// 附件备注(SFTP路径)
	

	public String getId() {
		if (StringUtils.isNotBlank(id)) {
			return id;
		}
		return refreshIdByKeys();
	}
	
	/** 
	 * 功能描述: 根据关键信息刷新id  
	 * <p>
	 * @return
	 */
	public String refreshIdByKeys() {
		List<String> keys = new ArrayList<>();
		keys.add(poolId == null ? "" : poolId);
		keys.add(itemId == null ? "" : itemId);
		

		this.id = Md5Util.getMD5String(StringUtils.join(keys, "|"));
		getHostConstraintList().forEach(c -> {
			c.setWhitelistType(this.getWhitelistType());
			c.setWhitelistId(id);
		});
		getOsTypeConstraintList().forEach(c -> {
			c.setWhitelistType(this.getWhitelistType());
			c.setWhitelistId(id);
		});
		return this.id;
	}
	
	@JsonProperty("attachmentJson")
	public String getAttachmentJson() {
		return JsonUtil.toJacksonJson(attachmentList);
	}
	
	public void setAttachmentJson(String attachmentJson) {
		attachmentList.clear();
		if (StringUtils.isNotBlank(attachmentJson)) {
			TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
			attachmentList.addAll(JsonUtil.jacksonConvert(attachmentJson, typeRef));
		}
	}
	
	/** 
	 * 功能描述: 自检  
	 * <p>
	 * @return
	 */
	public Pair<Boolean, String> selfCheck() {
		if (StringUtils.isNotBlank(itemId)){
			return Pair.of(true, null);
		}
		return Pair.of(false, "必须至少选择一个主机白名单属性(itemId)");
	}
	
	/** 
	 *
	 * 项目名称: ops-api 
	 * <p/>
	 * 
	 * 类名: OpsWhitelistCruiseCheckQueryParam
	 * <p/>
	 *
	 * 类功能描述: 巡检白名单分页查询参数对象
	 * <p/>
	 *
	 * @author	xuxixuan
	 *
	 * @date	2021年3月9日  
	 *
	 * @version	V1.0 
	 * <br/>
	 *
	 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
	 *
	 */
	@Setter
	@Getter
	public static class OpsWhitelistCruiseCheckQueryParam extends AbsPageQueryParamsAware {
		
		
		
		@JsonProperty("script_id")
		private String	scriptId;         	//脚本id
		
		@JsonProperty("script_name")
		private String	scriptName;			//脚本名称

		@JsonProperty("script_content_type")
		private String	scriptContentType;	//脚本内容类型
		
		@JsonProperty("script_group_name")
		private String	scriptGroupName;    //分组信息
		
		@JsonProperty("item_id")
		private String	itemId;  			//巡检项id
		
		@JsonProperty("pool_id")
		private String	poolId;  			//资源池id 
		
		@JsonProperty("pool_name")
		private String	poolName;  			//资源池名称
		
		@JsonProperty("item_name")
		private String	itemName;			//巡检项名称
		

		@JsonProperty("template_id")
		private String	templateId;			//巡检模板id

		@JsonProperty("template_name")
		private String	templateName;		//巡检模板名称

		@JsonProperty("attachment")
		private String 	attachment;  		//附件备注
		
		@JsonProperty("creater")
		private String	creater;     		//创建人

		@JsonProperty("status")
		private String	status;			// ON: 启用 OFF: 禁用

		@JsonProperty("updater")
		private String	updater;			//更新者
		
		@JsonProperty("last_update_time")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	lastUpdateTime;		//最后更新时间

		
		@JsonProperty("create_time")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	createTime;

		
	}
}
