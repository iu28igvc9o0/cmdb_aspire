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
 * 类名: OpsWhitelistBaseline
 * <p/>
 *
 * 类功能描述: 基线白名单对象
 * <p/>
 *
 * @author	xuxixuan
 *
 * @date	2021年3月17日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class OpsWhitelistBaseline {
	
	@JsonProperty("id")
	private String								id;

	@JsonProperty("whitelist_type")
	private final WhitelistTypeEnum				whitelistType			= WhitelistTypeEnum.baseline;

	@JsonProperty("pool_id")
	private String	poolId;						//资源池
	
	@JsonProperty("pool_name")
	private String	poolName;						//资源池名称
	
	@JsonProperty("baseline_type")
	private String	baselineType;					//基线类型
	
	@JsonProperty("baseline_id")
	private String	baselineId;			        //基线指标
	
	
	@JsonProperty("attachment")
	private String	attachment;						//附件信息

	
	@JsonProperty("status")
	private String 								status; 				// ON: 启用  	OFF: 禁用
	
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

	@JsonProperty("host_constraint_list")
	private final Set<OpsWhitelistConstraint>	hostConstraintList	= new HashSet<>();	// 关联的主机约束

	@JsonProperty("os_type_constraint_list")
	private final Set<OpsWhitelistConstraint>	osTypeConstraintList	= new HashSet<>();	// 关联的系统类型约束
	
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
		keys.add(poolId == null ? "":poolId);
		keys.add(baselineId == null ? "" : baselineId);
		
		
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
		if (StringUtils.isNotBlank(baselineId)){
			return Pair.of(true, null);
		}
		return Pair.of(false, "必须至少选择一个漏洞白名单属性");
	}
	
	/** 
	 *
	 * 项目名称: ops-api 
	 * <p/>
	 * 
	 * 类名: OpsWhitelistBaselineQueryParam
	 * <p/>
	 *
	 * 类功能描述: 漏洞白名单分页查询参数对象
	 * <p/>
	 *
	 * @author	xuxixuan
	 *
	 * @date	2021年3月17日  
	 *
	 * @version	V1.0 
	 * <br/>
	 *
	 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
	 *
	 */
	@Setter
	@Getter
	public static class OpsWhitelistBaselineQueryParam extends AbsPageQueryParamsAware {
		
//		资源池、基线类型、基线指标 时间点、黑名单状态、
		
		
		@JsonProperty("pool_id")
		private String	poolId;  //资源池
		
		@JsonProperty("pool_name")
		private String	poolName;						//资源池名称
		
		@JsonProperty("baseline_type")
		private String	baselineType; //基线类型
		
		@JsonProperty("baseline_id")
		private String	baselineId; //基线指标
		
	
		@JsonProperty("status")
		private String	status;			// ON: 启用 OFF: 禁用

		@JsonProperty("create_time")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	createTime;
		
		@JsonProperty("last_update_time")
		@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date	lastUpdateTime;

		
	}
}
