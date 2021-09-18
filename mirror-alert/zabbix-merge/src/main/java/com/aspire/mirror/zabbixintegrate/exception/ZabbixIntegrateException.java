package com.aspire.mirror.zabbixintegrate.exception;

public class ZabbixIntegrateException extends RuntimeException {
	private static final long serialVersionUID = -5852434000742099565L;

	public ZabbixIntegrateException(String msg) {
        super(msg);
    }
    
    public ZabbixIntegrateException(Throwable cause) {
        super(cause); 
    }
    
    public ZabbixIntegrateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
