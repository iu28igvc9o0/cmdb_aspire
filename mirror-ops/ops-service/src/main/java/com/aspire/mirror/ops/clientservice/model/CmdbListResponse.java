/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  CmdbListResponse.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月7日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.clientservice.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.ops.api.domain.OpsSpectreHostExt;
import com.aspire.mirror.ops.domain.OpsAuditInfo;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Data;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: CmdbListResponse
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class CmdbListResponse {
	
	@JsonProperty("totalSize")
	private Integer				totalSize;

	@JsonProperty("data")
	private List<CmdbInstance>	dataList;
	
	
	/** 
	 *
	 * 项目名称: ops-service 
	 * <p/>
	 * 
	 * 类名: CmdbInstance
	 * <p/>
	 *
	 * 类功能描述: TODO
	 * <p/>
	 *
	 * @author	pengguihua
	 *
	 * @date	2020年4月7日  
	 *
	 * @version	V1.0 
	 * <br/>
	 *
	 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
	 *
	 */
	@Data
	public static class CmdbInstance {

		@JsonProperty
		private String idcType;
		
		@JsonProperty
		private String ip;
		
		@JsonProperty("is_install_agent")
		private String isInstallAgent;			// 是|否
		
		@JsonIgnore
		protected final Map<String, Object>	extraAttrs = new HashMap<String, Object>();	// 其它属性

        public static OpsSpectreHostExt generateHostExtFromCMDBIntance(CmdbInstance cmdbInstance) {
			OpsSpectreHostExt opsSpectreHostExt = new OpsSpectreHostExt();
			opsSpectreHostExt.setAgentIp(cmdbInstance.getIp());
			opsSpectreHostExt.setPool(cmdbInstance.getIdcType());
			opsSpectreHostExt.setPoolName((String)cmdbInstance.getExtendAttrs().get("idcType_idc_name_name"));
			opsSpectreHostExt.setDepartment1((String)cmdbInstance.getExtendAttrs().get("department1"));
			opsSpectreHostExt.setDepartment2((String)cmdbInstance.getExtendAttrs().get("department2"));
			opsSpectreHostExt.setBizSystem((String)cmdbInstance.getExtendAttrs().get("bizSystem"));
			opsSpectreHostExt.setOsType((String)cmdbInstance.getExtendAttrs().get("device_os_type"));
			opsSpectreHostExt.setOsStatus((String)cmdbInstance.getExtendAttrs().get("pay_mainten_status"));
			opsSpectreHostExt.setOsStatusName((String)cmdbInstance.getExtendAttrs().get("pay_mainten_status_dict_note_name"));
			opsSpectreHostExt.setDepartment1Name((String)cmdbInstance.getExtendAttrs().get("department1_orgName_name"));
			opsSpectreHostExt.setDepartment2Name((String)cmdbInstance.getExtendAttrs().get("department2_orgName_name"));
			opsSpectreHostExt.setBizSystemName((String)cmdbInstance.getExtendAttrs().get("bizSystem_bizSystem_name"));
			opsSpectreHostExt.setOsTypeName((String)cmdbInstance.getExtendAttrs().get("device_os_type_dict_note_name"));
			opsSpectreHostExt.setDeviceClass((String)cmdbInstance.getExtendAttrs().get("device_class"));
			opsSpectreHostExt.setDeviceClassName((String)cmdbInstance.getExtendAttrs().get("device_class_dict_note_name"));
			opsSpectreHostExt.setDeviceType((String)cmdbInstance.getExtendAttrs().get("device_type"));
			opsSpectreHostExt.setDeviceTypeName((String)cmdbInstance.getExtendAttrs().get("device_type_dict_note_name"));
			opsSpectreHostExt.setDeviceName((String)cmdbInstance.getExtendAttrs().get("device_name"));
			opsSpectreHostExt.setRoomId((String)cmdbInstance.getExtendAttrs().get("roomId"));
			opsSpectreHostExt.setRoomName((String)cmdbInstance.getExtendAttrs().get("roomId_room_name_name"));
			opsSpectreHostExt.setBizEmployee((String)cmdbInstance.getExtendAttrs().get("biz_employee"));
			return opsSpectreHostExt;
        }

        @JsonAnyGetter
		public Map<String, Object> getExtendAttrs() {
			return extraAttrs;
		}
		
		@JsonAnySetter
		public void addExtendAttr(String key, Object value) {
			this.extraAttrs.put(key, value);
		}
		
		public Map<String, Object> toMap() {
			TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String,Object>>() {};
			return JsonUtil.jacksonConvert(this, typeRef);
		}
	}
}
