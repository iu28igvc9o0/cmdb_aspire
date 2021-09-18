/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  CdnDeviceServerDTO.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.ums.cdn.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.aspire.ums.cdn.annotation.CmdbTableColumn;
import com.aspire.ums.cdn.clientservice.payload.CmdbCode;
import com.aspire.ums.cdn.model.CmdbUpdateInstance.CmdbInstanceTableColumn;

import lombok.Data;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: CdnDeviceServerDTO
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class CdnDeviceServerDTO {
	@CmdbTableColumn(tag="main", colName="roomId")
	private String machineRoomName;		// 机房名
	
	@CmdbTableColumn(tag="sub", colName="isp_name")
	private String ispName;				// 服务提供商
	
	@CmdbTableColumn(tag="sub", colName="flat_name")
	private String flatName;			// 平面  如  华为 腾讯 等
	
	@CmdbTableColumn(tag="sub", colName="server_group_name")
	private String serverGroupName;		// ?
	
	@CmdbTableColumn(tag="sub", colName="server_logic_fun_name")
	private String serverLogicFunName;	// ?
	
	@CmdbTableColumn(tag="main", colName="device_type")
	private String deviceTypeName;		// 设备类型 ?
	
	@CmdbTableColumn(tag="sub", colName="server_type_name")
	private String serverTypeName;		// 服务类型名
	
	@CmdbTableColumn(tag="sub", colName="server_fun_name")
	private String serverFunName;		// 服务器功能   如： 边缘服务节点子系统
	
	@CmdbTableColumn(tag="main", colName="device_class")
	private String deviceClass = "服务器";	// 设备分类
	
	@CmdbTableColumn(tag="main", colName="device_mfrs")
	private String deviceVendorName;	// 设备供应商
	
	@CmdbTableColumn(tag="main", colName="device_model")
	private String deviceModelName;		// 
	
	@CmdbTableColumn(tag="main", colName="idcType")
	private String deviceOwnerName;		// 资源归属     如    西藏  广西
	
	@CmdbTableColumn(tag="main", colName="device_os_type")
	private String operationSystemName;	// 
	
	@CmdbTableColumn(tag="main", colName="device_name")
	private String hostname;		// 主机标签
	
	@CmdbTableColumn(tag="main", colName="HOST_NAME")
	private String realHostname;	// 真实主机名
	
	@CmdbTableColumn(tag="main", colName="ip")
	@CmdbTableColumn(tag="sub", colName="m_card_ip")
	private String mCardIP;			// 业务IP ?
	
//	@InstanceField(tag="sub", colName="salt_status_code")
	private String saltStatusCode;	// salt状态
	
	@CmdbTableColumn(tag="sub", colName="salt_status_label")
	private String saltStatusLabel;	
	
//	@InstanceField(tag="main", colName="device_status_code")
	private String deviceStatusCode; // 服务器物理状态
	
	@CmdbTableColumn(tag="main", colName="device_status")
	private String deviceStatusLabel;	
	
//	@InstanceField(tag="sub", colName="soft_status_code")
	private String softStatusCode;	// 服务器软件状态
	
	@CmdbTableColumn(tag="sub", colName="soft_status_label")
	private String softStatusLabel;	
	
	@CmdbTableColumn(tag="sub", colName="remark")
	private String remark;
	
	@CmdbTableColumn(tag="sub", colName="ssh_ip")
	private String sshIP;	// SSH连接IP
	
	@CmdbTableColumn(tag="sub", colName="ssh_port")
	private String sshPort;	// SSH连接端口
	
	public void setSaltStatusCode(String saltStatusCode) {
		this.saltStatusCode = saltStatusCode;
		this.saltStatusLabel = saltStatusCode == null ? null : SALT_STATUS.getLabelByCode(saltStatusCode);
	}
	
	public void setDeviceStatusLabel(String deviceStatusCode) {
		this.deviceStatusCode = deviceStatusCode;
		this.deviceStatusLabel = deviceStatusCode == null ? null : DEVICES_STATUS.getLabelByCode(deviceStatusCode);
	}
	
	public void setSoftStatusLabel(String softStatusCode) {
		this.softStatusCode = softStatusCode; 
		this.softStatusLabel = softStatusCode == null ? null : SOFT_STATUS.getLabelByCode(softStatusCode);
	}
	
	public void refreshStatusLabels() {
		setSaltStatusCode(this.saltStatusCode);
		setDeviceStatusLabel(this.deviceStatusCode);
		setSoftStatusLabel(this.softStatusCode);
	}
	
	public CmdbUpdateInstance resolveCmdbUpdateInstance(CmdbCode ipCode, String instanceId) {
		CmdbUpdateInstance updateModel = new CmdbUpdateInstance();
		updateModel.setInstanceTableList(resolveInstanceTableColumnList("main"));
		updateModel.setOtherTableList(resolveInstanceTableColumnList("sub"));
		if (ipCode != null) {
			CmdbInstanceIpManager ipModel = new CmdbInstanceIpManager();
			ipModel.setCodeId(ipCode.getCodeId());
			ipModel.setIp(this.getSshIP());
			if (instanceId != null) {
				ipModel.setInstanceId(instanceId);
			}
			updateModel.setIpManagerList(Collections.singletonList(ipModel));
		}
		return updateModel;
	}
	
	private List<CmdbInstanceTableColumn> resolveInstanceTableColumnList(String tag) {
		final List<CmdbInstanceTableColumn> result = new ArrayList<>();
		final CdnDeviceServerDTO dto = this;
		
		ReflectionUtils.doWithFields(this.getClass(), new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				field.setAccessible(true);
				CmdbTableColumn[] annos = field.getDeclaredAnnotationsByType(CmdbTableColumn.class);
				if (annos == null || annos.length == 0) {
					return;
				}
				for (CmdbTableColumn anno : annos) {
					if (!tag.equals(anno.tag())) {
						continue;
					}
					Object fieldVal = ReflectionUtils.getField(field, dto);
					if (fieldVal != null) {
						result.add(new CmdbInstanceTableColumn(anno.colName(), fieldVal));
					}
				}
			}
		});
		return result;
	}
	
	public static enum SALT_STATUS {
//		UNCONNECT("unconnect", "未连接"), INIT("init", "初始化"), WAIT("wait", "等待初始化结果"), 
//		FAILED("failed", "失败"), CONNECT("connect", "已连接"), DISCONNECT("disconnect", "连接已断开");
		
		UNCONNECT("3", "未连接"), INIT("0", "初始化"), WAIT("1", "等待初始化结果"), 
		FAILED("2", "失败"), CONNECT("4", "已连接"), DISCONNECT("5", "连接已断开");
		
		private String code;
		private String label;
		
		private SALT_STATUS(String code, String label) {
			this.code = code;
			this.label = label;
		}
		
		public static String getLabelByCode(String code) {
			for (SALT_STATUS item : SALT_STATUS.values()) {
				if (item.code.equals(code)) {
					return item.label;
				}
			}
			return null;
		}
	}
	
	public static enum DEVICES_STATUS {
//		SHELVES("shelves", "上架"), ONLINE("online", "上线"), OFFLINE("offline", "下线");
		SHELVES("6", "上架"), ONLINE("5", "上线"), OFFLINE("0", "下线");
		
		private String status;
		private String label;
		
		private DEVICES_STATUS(String status, String label) {
			this.status = status;
			this.label = label;
		}
		
		public static String getLabelByCode(String statusCode) {
			for (DEVICES_STATUS item : DEVICES_STATUS.values()) {
				if (item.status.equals(statusCode)) {
					return item.label;
				}
			}
			return null;
		}
	}
	
	public static enum SOFT_STATUS {
//		SHELVES("shelves", "未初始化"), INIT("init", "初始化"), WAIT("wait", "等待初始化结果"), SUCCESS("success", "初始化成功"),
//		FAILED("failed", "初始化失败"), ISSUING("issuing", "配置下发中");
		
		SHELVES("0", "未初始化"), INIT("init", "初始化"), WAIT("2", "等待初始化结果"), SUCCESS("3", "初始化成功"),
		FAILED("4", "初始化失败"), ISSUING("1", "配置下发中");
		
		private String status;
		private String label;
		
		private SOFT_STATUS(String status, String label) {
			this.status = status;
			this.label = label;
		}
		
		public static String getLabelByCode(String statusCode) {
			for (SOFT_STATUS item : SOFT_STATUS.values()) {
				if (item.status.equals(statusCode)) {
					return item.label;
				}
			}
			return null;
		}
	}
}
