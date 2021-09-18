package com.aspire.mirror.alert.server.biz.monthReport;

import com.aspire.mirror.alert.server.vo.monthReport.AlertMonthReportVo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AlertMonthDayBiz {

	 //每天查询业务系统均峰值：老方案
    void syncBizSytemDay(AlertMonthReportVo monthReportRequest)throws ParseException;
  //每天查询业务系统均峰值：老方案:分钟
    void syncBizSytemDayByMinite(AlertMonthReportVo monthReportRequest)throws  Exception;

    //暂时不用：统计每台设备每天的均峰值
    void syncIdcTypeIpDay(AlertMonthReportVo monthReportRequest)throws Exception;
    
    
    //每天查询业务系统均峰值
    List<Map<String, Object>> syncBizSytemDayNew(AlertMonthReportVo monthReportRequest)throws ParseException;
    //暂时不用
    void syncIdcTypeIpDayNew(AlertMonthReportVo monthReportRequest)throws Exception;
    //统计当月资源池均峰值和比值
	void IdcTypeMonthData(String month) throws Exception;
	//统计当月业务系统均峰值和比值
	void bizSystemMonthData() throws Exception;
	//统计当月net数据
	void netMonthData(String month) throws Exception;
	//每天查询资源池均峰值
	void syncIdcTypeDayNew(AlertMonthReportVo monthReportRequest) throws ParseException;
	
	void exportBizSytemDayExcel(String day,int hisFlag) throws Exception;
	
	void exportIdcTypeIpDayExcel(String day)throws Exception;
    
	//查询信息港、南基的数据
	 List<Map<String, Object>> getData(String month,String idcType,int hisFlag)  throws Exception;
	//查询资源池性能数据：1已分配物理服务器资源利用率2已上线裸金属（含GPU）3已分配裸金属（含GPU）4宿主机资源利用率5管理节点
	void IdcTypeMonthData2(int type,String month) throws Exception;
	
	//二级租户资源利用率1中移信息公司2除1
	void syncDepartment2Data(int type,String month) throws Exception;
	
	//云租户资源利用率3
	void syncBizSystem2Data(int type,String month) throws Exception;
	
	//运营月报表2-1  指标情况
	void phyMonthData(int type,String month) throws Exception;
	
	//单个设备性能缺失告警
	void scanDeviceMonitorData(String idcType,String idcTypeName,String date);
	
	//资源池的设备性能分布(按天)
	void scanIdcTypePerformanceData(String idcType, String idcTypeName, String date) throws Exception;


	void exportBizSystemMonthExcel(String month,String idcType,int hisFlag) throws Exception;
}
