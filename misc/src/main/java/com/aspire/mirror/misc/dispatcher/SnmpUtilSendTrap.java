package com.aspire.mirror.misc.dispatcher;

import java.util.Map;

import com.aspire.mirror.misc.model.SnmpEvent;


public interface SnmpUtilSendTrap {

	void sendPDU(SnmpEvent snmpEvent,Map<String,String> nodeMap,String ipPort);
}
