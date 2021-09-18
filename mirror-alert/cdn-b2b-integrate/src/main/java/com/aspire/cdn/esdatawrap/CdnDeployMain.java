package com.aspire.cdn.esdatawrap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CdnDeployMain {

	public static void main(String[] args) {
//		prepareIdcType4Province();
		prepareAlertProxyIdc();
	}
	
	private static void prepareIdcType4Province() {
		String join_provs = "安徽,河南,山东,广西,河北,四川,辽宁,福建,广东,山西,贵州,重庆,云南,黑龙江,陕西,江西,湖南,内蒙古,湖北,上海,甘肃,吉林,北京,天津,江苏,浙江,宁夏,海南,新疆,西藏,青海";
		String[] provinceArr = join_provs.split(",");
		String sqlTemp = "INSERT INTO cmdb_idc_manager (`id`, `idc_code`, `idc_name`, `zabbix_server_ip`, `sort_index`, `is_delete`) VALUES ('%s', '%s', '%s', '127.0.0.1', '%s', '0');";

		List<String> sqlList = new ArrayList<>();
		int baseStep = 10;
		for (int i = 0; i < provinceArr.length; i++) {
			String uuid = UUID.randomUUID().toString();
			sqlList.add(String.format(sqlTemp, uuid, provinceArr[i], provinceArr[i], baseStep++));
		}
		
		sqlList.stream().forEach(sql -> System.out.println(sql));
	}
	
	
	private static void prepareAlertProxyIdc() {
		String join_provs = "安徽,河南,山东,广西,河北,四川,辽宁,福建,广东,山西,贵州,重庆,云南,黑龙江,陕西,江西,湖南,内蒙古,湖北,上海,甘肃,吉林,北京,天津,江苏,浙江,宁夏,海南,新疆,西藏,青海";
		String[] provinceArr = join_provs.split(",");
		String sqlTemp = "INSERT INTO alert_proxy_idc (`id`, `proxy_name`, `idc`, `remark`) VALUES ('%s', '%s', '%s', '%s');";

		List<String> sqlList = new ArrayList<>();
		int baseStep = 50;
		for (int i = 0; i < provinceArr.length; i++) {
			sqlList.add(String.format(sqlTemp, baseStep + i, provinceArr[i], provinceArr[i], provinceArr[i]));
		}
		sqlList.stream().forEach(sql -> System.out.println(sql));
	}
}
