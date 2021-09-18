package com.aspire.mirror.misc.dispatcher.impl;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aspire.mirror.misc.dao.MiscDao;
import com.aspire.mirror.misc.dispatcher.SnmpUtilSendTrap;
import com.aspire.mirror.misc.model.SnmpEvent;
import com.aspire.mirror.misc.model.SnmpEventHis;

@Service("xinjiang")
public class XinJiangSendTrap implements SnmpUtilSendTrap{

	@Autowired
	private MiscDao miscDao;

	@Value("${mydata.factorName}")
	private String factorName;
	@Value("${mydata.provinceName}")
	private String provinceName;
	@Value("${mydata.cityName}")
	private String cityName;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XinJiangSendTrap.class);

	@Override
	@Async("miscAsync")
	public void sendPDU(SnmpEvent snmpEvent,Map<String,String> nodeMap,String ipPort) {

		String logString = "告警内容------"+"告警唯一id"+snmpEvent.getAlertId()+"------告警开始时间="+snmpEvent.getAlertStartTime();
		
		try {
			
			//设定目标对象CommunityTarget
			CommunityTarget target = new CommunityTarget();
			//CommunityTarget:设定目标的ip地址
			//Address address = GenericAddress.parse("udp:192.168.172.101/161");
			//Address address = GenericAddress.parse("udp:192.168.172.101/162");
			Address address = GenericAddress.parse(ipPort);
			target.setAddress(address);
			//CommunityTarget:设定snmp共同体Community
			target.setCommunity(new OctetString("public"));
			//CommunityTarget:设定超时重试次数
			target.setRetries(3);
			//CommunityTarget:设定超时时间
			target.setTimeout(5*60);
			//CommunityTarget:设定snmp的版本
			target.setVersion(SnmpConstants.version2c);
					
					
					
					
			//创建消息对象PDU
			PDU pdu = new PDU();
			
			TimeTicks sysUpTime = new TimeTicks(
		      System.currentTimeMillis() / 1000L);
		    pdu.add(new VariableBinding(SnmpConstants.sysUpTime, sysUpTime));

		    /*
		    pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OctetString(
		      "1.3.6.1.4.1.14915.2")));

		     */
		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("ALERT_ID")), 
		      new OctetString(snmpEvent.getAlertId())));
		    logString = logString+"------"+nodeMap.get("ALERT_ID")+"="+snmpEvent.getAlertId();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("NE_CODE")), 
		      new OctetString(snmpEvent.getNeCode() == null ? "" : 
		      changCharert(snmpEvent.getNeCode()))));
			logString = logString+"------"+nodeMap.get("NE_CODE")+"="+snmpEvent.getNeCode();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("BUSINESS_SYSTEM")), 
		      new OctetString(snmpEvent.getBussinessSystem() == null ? "" : 
		      changCharert(snmpEvent.getBussinessSystem()))));
			logString = logString+"------"+nodeMap.get("BUSINESS_SYSTEM")+"="+snmpEvent.getBussinessSystem();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("MONI_OBJECT")), 
		      new OctetString(snmpEvent.getDevice() == null ? "" : 
		      changCharert(snmpEvent.getDevice()))));
			logString = logString+"------"+nodeMap.get("MONI_OBJECT")+"="+snmpEvent.getDevice();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("MONI_INDEX")), 
		      new OctetString(changCharert(snmpEvent.getMoniIndex() == null ? "" : 
		      changCharert(snmpEvent.getMoniIndex())))));
			logString = logString+"------"+nodeMap.get("MONI_INDEX")+"="+snmpEvent.getMoniIndex();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("ALERT_LEVEL")), 
		      new OctetString(changCharert(snmpEvent.getAlertLevel() == null ? "" : 
		      changCharert(snmpEvent.getAlertLevel())))));
			logString = logString+"------"+nodeMap.get("ALERT_LEVEL")+"="+snmpEvent.getAlertLevel();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("MONI_TYPE")), 
		      new OctetString(changCharert(snmpEvent.getMoniType() == null ? "" : 
		      changCharert(snmpEvent.getMoniType())))));
			logString = logString+"------"+nodeMap.get("MONI_TYPE")+"="+snmpEvent.getMoniType();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("ALERT_START_TIME")), 
		      new OctetString(DateToStr(snmpEvent
		      .getAlertStartTime()))));
			logString = logString+"------"+nodeMap.get("ALERT_START_TIME")+"="+DateToStr(snmpEvent.getAlertStartTime());


		    pdu.add(new VariableBinding(
		      new OID((String)nodeMap.get("REMOVED_DATE")), 
		      new OctetString(DateToStr(snmpEvent.getRemoveDate()))));
			logString = logString+"------"+nodeMap.get("REMOVED_DATE")+"="+DateToStr(snmpEvent.getRemoveDate());


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("EVENT_STATUS")), 
		      new OctetString(String.valueOf(snmpEvent.getEventStatus()))));
			logString = logString+"------"+nodeMap.get("EVENT_STATUS")+"="+String.valueOf(snmpEvent.getEventStatus());


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("TITLE")), 
		      new OctetString(changCharert(snmpEvent.getMoniIndex() == null ? "" : 
		      changCharert(snmpEvent.getMoniIndex())))));
			logString = logString+"------"+nodeMap.get("TITLE")+"="+snmpEvent.getMoniIndex();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("ALERT_DESC")), 
		      new OctetString(changCharert(snmpEvent.getAlertDesc() == null ? "" : 
		      changCharert(snmpEvent.getAlertDesc())))));
			logString = logString+"------"+nodeMap.get("ALERT_DESC")+"="+snmpEvent.getAlertDesc();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("DEVICE")), 
		      new OctetString(snmpEvent.getDevice() == null ? "" : 
		      changCharert(snmpEvent.getDevice()))));
			logString = logString+"------"+nodeMap.get("DEVICE")+"="+snmpEvent.getDevice();


		    pdu.add(new VariableBinding(new OID((String)nodeMap.get("CUR_MONI_VALUE")), 
		      new OctetString(changCharert(snmpEvent.getCurMoniValue() == null ? "" : 
		      changCharert(snmpEvent.getCurMoniValue())))));
			logString = logString+"------"+nodeMap.get("CUR_MONI_VALUE")+"="+snmpEvent.getCurMoniValue();



			/**
			 * 以下三个新增字段内容从application.yml文件中读取
			 * 并非从数据库的告警表
			 */
			//新增字段“省份”
			pdu.add(new VariableBinding(new OID((String)nodeMap.get("PROVINCE_NAME")),
					new OctetString(changCharert(provinceName == null ? "" :
							changCharert(provinceName)))));
			logString = logString+"------"+nodeMap.get("PROVINCE_NAME")+"="+provinceName;


			//新增字段“市”
			pdu.add(new VariableBinding(new OID((String)nodeMap.get("CITY_NAME")),
					new OctetString(changCharert(cityName == null ? "" :
							changCharert(cityName)))));
			logString = logString+"------"+nodeMap.get("CITY_NAME")+"="+cityName;


			//新增字段“厂家”
			pdu.add(new VariableBinding(new OID((String)nodeMap.get("FACTOR_NAME")),
					new OctetString(changCharert(factorName == null ? "" :
							changCharert(factorName)))));
			logString = logString+"------"+nodeMap.get("FACTOR_NAME")+"="+factorName;



			/**
			 * 以下新增内容从jar包同级文件yn-mirror.properties中获取
			 */
			try {

				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(System.getProperty("user.dir")+"/yn-mirror.properties"),"utf-8"));
				String s = null;
				HashMap<String,String> map = new HashMap<String,String>();
				while((s = reader.readLine()) != null){
					String[] split = s.split("=");
					map.put(split[0],split[1]);
				}

				for(String key:map.keySet()){
					if(snmpEvent.getMoniIndex().equals(key)){
						pdu.add(new VariableBinding(new OID((String)nodeMap.get("NEW1")),
								new OctetString(changCharert(map.get(key) == null ? "" :
										changCharert(map.get(key))))));
						logString = logString+"------"+nodeMap.get("NEW1")+"="+map.get(key);
					}
				}
			}catch (Exception e){
				e.printStackTrace();
				LOGGER.error("没有读取到程序外的自定义文件");
			}




			/* 以下方法，在key中有空格，回导致读取key不准确，弃用(只使用key中没有空格的情况)
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(System.getProperty("user.dir")+"/yn-mirror.properties"),"utf-8"));
			Properties properties = new Properties();
			properties.load(reader);
			for(String key:properties.stringPropertyNames()){
				if(snmpEvent.getMoniIndex().equals(key)){
					pdu.add(new VariableBinding(new OID((String)nodeMap.get("NEW1")),
							new OctetString(changCharert(properties.getProperty(key) == null ? "" :
									changCharert(properties.getProperty(key))))));
					logString = logString+"------"+nodeMap.get("NEW1")+"="+properties.getProperty(key);
				}
			}

			 */


			LOGGER.info(logString);//写入日志

			//pdu.add(new VariableBinding(new OID("1.2.3"),new OctetString("zhujiahao")));
			//pdu.add(new VariableBinding(new OID("1.2.3.4"),new OctetString("55667788")));	
			//设定pdu的类型(参考上面的 snmpde的5种操作种类总结)
			pdu.setType(PDU.TRAP);
			
			//设定采取的协议TransportMapping(我这里使用默认的udp)
			DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
			transport.listen();
			
			
			//创建snmp对象，发送pdu
			Snmp snmp = new Snmp(transport);
			ResponseEvent response = snmp.send(pdu, target);
			
			
			transport.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("snmp发送失败.告警id为:"+ new OctetString(snmpEvent.getAlertId())+".错误详情:"+e.toString());
			//如果异常或报错,则跳出,不会执行下面的插入动作
			return;
		}
		
		
		//发送成功的数据,插入"上传成功表"中
		this.insertSnmpEventHis(snmpEvent);
		
	}
	
	
	
	public void insertSnmpEventHis(SnmpEvent snmpEvent) {
		
		SnmpEventHis snmpEventInfo = new SnmpEventHis();
		snmpEventInfo.setAlertId(snmpEvent.getAlertId());
		snmpEventInfo.setCreateTime(snmpEvent.getCreateTime());
		snmpEventInfo.setAlertStatus(snmpEvent.getAlertStatus());
		
		
		/*
		发送成功后，要把数据  插入/更新  到"上传成功表"中
		 判断"上传成功表"中是否有该条数据：
			没有：插入"上传成功表"。
			有：更新"上传成功表"中对应的那条数据。
		*/
		SnmpEventHis info = miscDao.findSnmpEventSucInfo(snmpEvent);
		if (info == null) {
			miscDao.insertSnmpEventSusInfo(snmpEventInfo);
		} else {
			miscDao.updateSnmpEventInfo(snmpEventInfo);
		}
		
	}
	
	
	
	public String changCharert(String str)
			throws UnsupportedEncodingException
	{
		//return new String(str.getBytes("GBK"), "GBK");
		return new String(str.getBytes("utf-8"), "utf-8");
	}
	  
	public String DateToStr(Date date) {
		String str ="";
	   if(date != null){
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   str = format.format(date);
	   }
	   return str;
	} 
}
