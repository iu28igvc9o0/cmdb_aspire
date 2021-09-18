package com.aspire.mirror.misc.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.aspire.mirror.misc.model.SnmpEvent;
import com.aspire.mirror.misc.model.SnmpEventHis;
import com.aspire.mirror.misc.model.SnmpEventNode;

@Mapper
public interface MiscDao {

	//查询出待上传的告警数据的总数
	//Integer findSnmpEventSucInfoCount();
	
	//根据"告警表" 和 "告警历史表" 筛选出需要上传的告警数据集合
	List<SnmpEvent> ListAllSnmpEvent();
	
	//查询出"上传成功表"中是否有对应的记录
	SnmpEventHis findSnmpEventSucInfo(SnmpEvent snmpEvent);
	
	//查询出"OID描述表"中的数据集合
	List<SnmpEventNode> listSnmpEventNode();
	
	//判断每一条"消警"数据,是否有对应的"告警"数据，有则返回字符"1",没有则返回null
	String findSnmpEventAlert(SnmpEvent snmpEvent);
	
	//插入"上传成功表"
	void insertSnmpEventSusInfo(SnmpEventHis snmpEventHis);
	
	//更新"上传成功表"
	void updateSnmpEventInfo(SnmpEventHis snmpEventHis);
}
