package com.aspire.mirror.indexproxy.indexprocess;

public class Consts {
	public static final String	MONITOR_EVENT_TOPIC							= "topic_monitorEvent";
	// 中央机房接受指标值的topic
	public static final String	CENTER_ROOM_MONITOR_VALUE_TOPIC				= "topic_centerRoom_indexValue";
	// 中央机房接受指标值的topic的分区个数
	public static final int		CENTER_ROOM_MONITOR_VALUE_TOPIC_PARTIONS	= 3;
	
	
	public enum EVENT_SOURCE {
		TRIGGERS, DISCOVERY, REGISTRATION;
	}
	
	public enum EVENT_VALUE {
		NORMAL("0"), EXCEPTION("1"); // 
		
		private String code;
		private EVENT_VALUE(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return this.code;
		}
	}
	
	public enum EVENT_ACKNOWLEDGE_STATUS {
		NO_ACKNOWLEDGE("0"), ACKNOWLEDGE("1");
		
		private String statusCode;
		private EVENT_ACKNOWLEDGE_STATUS(String code) {
			this.statusCode = code;
		}
		
		public String getStatusCode() {
			return statusCode;
		}
	}
}