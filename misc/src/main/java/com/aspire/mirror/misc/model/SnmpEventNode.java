package com.aspire.mirror.misc.model;

import lombok.Data;

@Data
public class SnmpEventNode {

	//OID的值
	private String nodeMiscId;
	
	//OID对应的描述
	private String nodeMiscName;
	
	//这个暂时没有用
	private String snmpStatus;
		
}
