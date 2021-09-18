package com.aspire.mirror.alert.server.task; /**
 * @author xuewenlong@cmss.chinamobile.com
 * @updated 2016年10月20日
 */


/**
 * @author xuewenlong@cmss.chinamobile.com
 * @updated 2016年10月20日
 */

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Vector;


/** 
 * 本类用于向管理进程发送Trap信息 
 *  
 * @author zhanjia 
 * 
 */  
public class TrapSender {  
	
	

	private Snmp snmp = null;  

	private Address targetAddress = null; 
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void initComm() throws IOException {  

		// 设置管理进程的IP和端口  
		//targetAddress = GenericAddress.parse("udp:10.144.91.61/30125");
		//targetAddress = GenericAddress.parse("udp:127.0.0.1/162");
		targetAddress = GenericAddress.parse("udp:127.0.0.1/8362");
		//targetAddress = GenericAddress.parse("udp:127.0.0.1/1163");
		TransportMapping transport = new DefaultUdpTransportMapping();  
		snmp = new Snmp(transport);  
		transport.listen();  

	}  

	/** 
	 * 向管理进程发送Trap报文 
	 *  
	 * @throws IOException 
	 * @throws ParseException 
	 */  
	public void sendPDU(int n) throws IOException  {  

		// 设置 target  
		CommunityTarget target = new CommunityTarget();  
		target.setAddress(targetAddress);  

		// 通信不成功时的重试次数  
		target.setRetries(2);  
		// 超时时间  
		target.setTimeout(1500);  
		// snmp版本  
		target.setVersion(SnmpConstants.version2c);  

		// 创建 PDU  
		PDU pdu = new PDU(); 

		String ResourcePoolID = new String("1.3.6.1.4.1.49022.2.21.4.1");
		String SerialNumber = new String("1.3.6.1.4.1.49022.2.21.4.2");
		String AlarmType = new String("1.3.6.1.4.1.49022.2.21.4.3");
		String AlarmTitleID = new String("1.3.6.1.4.1.49022.2.21.4.4");
		String AlarmTitle = new String("1.3.6.1.4.1.49022.2.21.4.5");
		String AlarmContent = new String("1.3.6.1.4.1.49022.2.21.4.6");
		String AlarmLevel = new String("1.3.6.1.4.1.49022.2.21.4.7");
		String AlarmTime = new String("1.3.6.1.4.1.49022.2.21.4.8");
		String ObjectId = new String("1.3.6.1.4.1.49022.2.21.4.9");
		String LocationInfo = new String("1.3.6.1.4.1.49022.2.21.4.10");
		String DeviceType = new String("1.3.6.1.4.1.49022.2.21.4.11");
		String NotifyType = new String("1.3.6.1.4.1.49022.2.21.4.12");
		String AlarmId = new String("1.3.6.1.4.1.49022.2.21.4.13");
		String SystemName = new String("1.3.6.1.4.1.49022.2.21.4.14");
		String CurrentIp = new String("1.3.6.1.4.1.49022.2.21.4.16");
		String SourceIP = new String("1.3.6.1.4.1.49022.2.21.4.17");
		String ALARMIMPACT_OID = new String("1.3.6.1.4.1.49022.2.21.4.15");
		String PARENTID_OID =  new String("1.3.6.1.4.1.49022.2.21.4.20");
		String PARENTDEVICETYPE_OID =  new String("1.3.6.1.4.1.49022.2.21.4.21");
		String PARENTNAME_OID =  new String("1.3.6.1.4.1.49022.2.21.4.22");

		String time = dateFormat.format(new Date());

		String pool="CPC-RP-NM-02";
		pdu.add(
				new VariableBinding(SnmpConstants.sysUpTime,   //sysUpTime
						new OctetString(getBase64(time))));

		pdu.add(
				new VariableBinding(SnmpConstants.snmpTrapOID,    //snmpTrapID
						new OctetString(getBase64 ("1.3.6.1.4.1.2011.5.25.41.3.3"))));

		pdu.add(
				new VariableBinding(new OID(ResourcePoolID), 
						new OctetString(getBase64(pool))));

		pdu.add(
				new VariableBinding(new OID(SerialNumber), 
						new OctetString(getBase64(new Integer32(111).toString()))));

		pdu.add(
				new VariableBinding(new OID(AlarmType), 
						new OctetString(getBase64("网络"))));

		pdu.add(
				new VariableBinding(new OID(AlarmTitleID), 
						new OctetString(getBase64("ALARM_01_6"))));

		pdu.add(
				new VariableBinding(new OID(AlarmTitle), 
						new OctetString(getBase64("网络安全设备故障"+n))));

		pdu.add(
				new VariableBinding(new OID(AlarmContent), 
						new OctetString(getBase64("CRITIAL - sde : Error:,{instance_service:bc_ec}"))));

		pdu.add(
				new VariableBinding(new OID(AlarmLevel), 
						new OctetString(getBase64(new Integer32(1).toString()))));
		Date date = new Date();

		long times = date.getTime()/1000; 
		pdu.add(
				new VariableBinding(new OID(AlarmTime), 
						new OctetString( getBase64(Long.toString(times)))));

		pdu.add(
				new VariableBinding(new OID(ObjectId), 
						new OctetString(getBase64("CPC-RP-NM-01-SVI-SRV-00000517"+n))));


		pdu.add(
				new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.10"), 
						new OctetString(getBase64("sanhaojigui"))));



		pdu.add(
				new VariableBinding(new OID(DeviceType), 
						new OctetString(getBase64("PhysicalServer"))));

			// 0-告警，1-消警
		pdu.add(
				new VariableBinding(new OID(NotifyType), 
						new OctetString(getBase64(new Integer32(1).toString()))));

		pdu.add(
				new VariableBinding(new OID(AlarmId), 
						new OctetString(getBase64(new Integer32(n).toString()))));


		pdu.add(
				new VariableBinding(new OID(SystemName), 
						new OctetString(getBase64("compute-03-01-108.local"))));


		pdu.add(
				new VariableBinding(new OID(ALARMIMPACT_OID), 
						new OctetString(getBase64("告警影响描述信息。。。。。。。"))));



		pdu.add(
				new VariableBinding(new OID(CurrentIp), 
						new OctetString(getBase64(new IpAddress("127.0.0.1").toString()))));



		pdu.add(
				new VariableBinding(new OID(SourceIP), 
						new OctetString(getBase64(new IpAddress("127.0.0.34").toString()))));
		pdu.add(
				new VariableBinding(new OID(PARENTID_OID),
						new OctetString(getBase64("Parent-CPC-RP"))));
		pdu.add(
				new VariableBinding(new OID(PARENTDEVICETYPE_OID),
						new OctetString(getBase64("Physical"))));
		pdu.add(
				new VariableBinding(new OID(PARENTNAME_OID),
						new OctetString(getBase64("xue的机器"))));





		pdu.setType(PDU.TRAP);  
		System.out.println(pdu.toString());

		// 向Agent发送PDU，并接收Response  
		ResponseEvent respEvnt = snmp.send(pdu, target);  

		// 解析Response  
		if (respEvnt != null && respEvnt.getResponse() != null) {  
			Vector<VariableBinding> recVBs =(Vector<VariableBinding>) respEvnt.getResponse()  
					.getVariableBindings();  
			for (int i = 0; i < recVBs.size(); i++) {  
				VariableBinding recVB = recVBs.elementAt(i);  
				System.out.println(recVB.getOid() + " : " + recVB.getVariable());  
			}  
		}  
	}

	public static String getBase64(String msg){
		org.apache.commons.net.util.Base64 base64 = new org.apache.commons.net.util.Base64();
		byte[] textByte = null;
		try {
			textByte = msg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//编码
		return base64.encodeToString(textByte);
	}

	public static void main(String[] args) throws InterruptedException {  
		try {  
			TrapSender util = new TrapSender();  
			util.initComm(); 
		
			
			for(int i=0;i<1;i++) {
			    //int x=(int)(Math.random()*7000);
				Thread.sleep(100);
			    util.sendPDU(10);
			}
			/* while(true) {
                int x=(int)(Math.random()*30);
                util.sendPDU(x); 
                Thread.sleep(60000);
            }*/

		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}  
}
