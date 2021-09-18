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
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 类名: OpsHostWhitelist
 * <p/>
 *
 * 类功能描述: 主机白名单对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年3月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class OpsWhitelistHost {

	@JsonProperty("whitelist_type")
	private final WhitelistTypeEnum				whitelistType			= WhitelistTypeEnum.host;

	@JsonProperty("id")
	private String								id;

	@JsonProperty("pool_id")
	private String								poolId;					// 资源池id
	
	@JsonProperty("pool_name")
	private String								poolName;				// 资源池name

	@JsonProperty("department1")
	private String								department1;			// 一级部门id
	
	@JsonProperty("department1_name")
	private String								department1Name;		// 一级部门name

	@JsonProperty("department2")
	private String								department2;			// 二级部门id
	
	@JsonProperty("department2_name")
	private String								department2Name;		// 二级部门name

	@JsonProperty("biz_system")
	private String								bizSystem;				// 业务系统id

	@JsonProperty("biz_system_name")
	private String								bizSystemName;			// 业务系统id
	
	@JsonProperty("host_ip")
	private String								hostIp;
	
	@JsonProperty("status")
	private String 								status 	= "ON"; 				// ON: 启用  	OFF: 禁用
	
	@JsonProperty("creater")
	private String								creater;
	
	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date								createTime;
	
	@JsonProperty("updater")
	private String								updater;
	
	@JsonProperty("last_update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date								lastUpdateTime;

	@JsonProperty("script_constraint_list")
	private final Set<OpsWhitelistConstraint>	scriptConstraintList	= new HashSet<>();	// 关联的脚本约束

	@JsonProperty("pipeline_constraint_list")
	private final Set<OpsWhitelistConstraint>	pipelineConstraintList	= new HashSet<>();	// 关联的作业约束
	
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
//		keys.add(department1 == null ? "" : department1);
//		keys.add(department2 == null ? "" : department2);
//		keys.add(bizSystem == null ? "" : bizSystem);
		keys.add(hostIp == null ? "" : hostIp);
		this.id = Md5Util.getMD5String(StringUtils.join(keys, "|"));
		getScriptConstraintList().forEach(c -> {
			c.setWhitelistType(this.getWhitelistType());
			c.setWhitelistId(id);
		});
		getPipelineConstraintList().forEach(c -> {
			c.setWhitelistType(this.getWhitelistType());
			c.setWhitelistId(id);
		});
		return this.id;
	}
	
	@JsonIgnore
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
		if (StringUtils.isNotBlank(poolId) && StringUtils.isNotBlank(hostIp)) {
			return Pair.of(true, null);
		}
		return Pair.of(false, "必须至少选择一个主机白名单属性");
	}
	
	public String getStatus() {
		return status != null ? status : "ON";
	}
	
	/** 
	 *
	 * 项目名称: ops-api 
	 * <p/>
	 * 
	 * 类名: OpsWhitelistHostQueryParam
	 * <p/>
	 *
	 * 类功能描述: 主机白名单分页查询参数对象
	 * <p/>
	 *
	 * @author	pengguihua
	 *
	 * @date	2021年3月6日  
	 *
	 * @version	V1.0 
	 * <br/>
	 *
	 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
	 *
	 */
	@Setter
	@Getter
	public static class OpsWhitelistHostQueryParam extends AbsPageQueryParamsAware {
		
		@JsonProperty("pool_id")
		private String	poolId;

		@JsonProperty("pool_name")
		private String	poolName;

		@JsonProperty("department1")
		private String	department1;

		@JsonProperty("department2")
		private String	department2;

		@JsonProperty("biz_system")
		private String	bizSystem;

		@JsonProperty("host_ip")
		private String	hostIp;

		@JsonProperty("status")
		private String	status;			// ON: 启用 OFF: 禁用
		
		@JsonProperty("remark")
		private String	remark;		

		@JsonProperty("create_time_start")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	createTimeStart;

		@JsonProperty("create_time_end")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	createTimeEnd;
	}
}
