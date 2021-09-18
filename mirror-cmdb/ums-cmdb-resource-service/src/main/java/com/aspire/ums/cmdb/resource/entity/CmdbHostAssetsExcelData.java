package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class CmdbHostAssetsExcelData implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String suiteID;
	private String ID;
	private String device_class_id;
	private String device_type_id;
	private String device_model_id;
	private String device_os_type_id;
	private String device_maintenance_id; //维保信息ID
	private String businessId;//业务ID

	private String device_ip; //主IP地址
	private String other_ip; //其它IP地址
	private String device_log_name; //逻辑名
	private String business_level1; //一级业务
	private String business_level2; //二级业务
	private String host_backup; //主备关系
	private String idc; //所属位置
	private String idc_label; //所属位置
	private String idc_check; //所属位置校验标识符
	private String idc_location; //机房位置
	private String device_cell; //机柜号
	private String box_num; //刀箱号
	private String slot_num; //槽位号
	private String box_mgd_ip; //刀箱管理IP
	private String exsi_ip; //所在宿主机IP
	private String vm_name; //承载虚拟机名称
	private String vm_ip; //承载虚拟机IP
	private String device_class; //设备分类
	private String device_type; //设备类型
	private String device_model; //设备型号
	private String device_os_type; //设备系统类型
	private String device_status; //状态
	private String device_standard; //设备规格
	private String device_sn; //序列号
	private String b_card_sn; //板卡序列号
	private String asset_number; //资产标签号

	private String block_size; //块存储(GB)
	private String dis_storage; //分布式存储（GB）
	private String dis_st_dir; //分布式存储挂载目录
	private String maintenance_time; //维保时间
	private String online_time; //上线时间

	private String dis_st_type; //分布式存储类型
	private String resource_plan; //资源计划性
	private String blong_to; //项目归属

	private String console_ip; //console IP
	private String console_vlan; //console vlan
	private String console_mask; //console 掩码
	private String console_gw; //console 网关
	private String console_user; //console 账号
	private String console_password; //console 密码
	private String business_vlan; //业务 vlan
	private String local_disk; //本地磁盘大小
	private String mount_disk; //初始外挂磁盘
	private String network_area; //网络区域
	private String device_maintenance; //维保信息
	private String device_maintenance_vender; //维保厂家
	private String managed_by_ansiblertable; //是否ansible管理
	private String for_check; //是否迎检
	private String mgd_by_pool; //是否是资源池管理设备
	private String remark; //备注

	private String is_assign;//是否分配

	private String business_department;

	/*20180704  建立4个字段  转资成本,单价,按比例分摊日期,使用年限*/
	private String trans_cost;
	private String unit_price;
	private String prorate_date;
	private String service_life;

	//资源建设详情id
	private String build_detail_id;


}