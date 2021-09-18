import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.net.util.Base64;
import org.junit.Test;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
 
/**
 * 本类用于向管理进程发送Trap信息
 * 
 * @author zhanjia
 *
 */
public class SnmpUtilSendTrap {
 
	private Snmp snmp = null;
 
	private Address targetAddress = null;
 
	public void initComm() throws IOException {
 
		// 设置管理进程的IP和端口
		targetAddress = GenericAddress.parse("udp:127.0.0.1/8163");
		TransportMapping transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		transport.listen();
 
	}
 
	/**
	 * 向管理进程发送Trap报文
	 * 
	 * @throws IOException
	 */
	public void sendPDU() throws IOException {
 
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
	   
		pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.3.0"),
				new OctetString(getBase64("2019-03-13 20:29:20"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.6.3.1.1.4.1.0"),
				new OctetString(getBase64("1.3.6.1.4.1.8596.37945.1.1.552707.600010009"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.1"),
				new OctetString(getBase64("CPC-RP-55"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.2"),
				new OctetString(getBase64("907141547"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.3"),
				new OctetString(getBase64("机柜"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.3"),
				new OctetString(getBase64("ALARM_02_01"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.5"),
				new OctetString(getBase64("报警报警"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.6"),
				new OctetString(getBase64("设备厂家：中兴; 硬件设备电源告警,浪潮设备电源异常告警,设备厂家：中兴; 硬件设备电源告警,浪潮设备电源异常告警."))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.7"),
				new OctetString(getBase64("3"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.8"),
				new OctetString(getBase64("1552459867"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.9"),
				new OctetString(getBase64("CPC-D-55-VPN-SINFOR-00020371"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.11"),
				new OctetString(getBase64("PhysicalSwitch"))));
		// 1-清除，0-新增
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.12"),
				new OctetString(getBase64("0"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.13"),
				new OctetString(getBase64("93891411"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.14"),
				new OctetString(getBase64("BFJD-ISC3-I-S5852T-46"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.15"),
				new OctetString(getBase64(""))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.16"),
				new OctetString(getBase64("192.168.149.219"))));
		pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.49022.2.21.4.17"),
				new OctetString(getBase64("192.168.149.219"))));
		pdu.setType(PDU.TRAP);
 
		// 向Agent发送PDU，并接收Response
		ResponseEvent respEvnt = snmp.send(pdu, target);
 
		// 解析Response
		/*if (respEvnt != null && respEvnt.getResponse() != null) {
			Vector<VariableBinding> recVBs = respEvnt.getResponse().getVariableBindings();
			for (int i = 0; i < recVBs.size(); i++) {
				VariableBinding recVB = recVBs.elementAt(i);
				System.out.println(recVB.getOid() + " : " + recVB.getVariable());
			}
		}*/
		System.out.println("成功!!!");
	}

	@Test
	public  void testSnmpTrap(){
		try {
			SnmpUtilSendTrap util = new SnmpUtilSendTrap();
			util.initComm();
			util.sendPDU();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	public static String getBase64(String msg){
		 Base64 base64 = new Base64();
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

}
