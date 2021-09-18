package com.aspire.mirror.ops.exception;

public class OpsServiceException extends RuntimeException {
	private static final long serialVersionUID = -62348076367001113L;

    public OpsServiceException(String msg) {
        super(msg);
    }
    
    public OpsServiceException(Throwable t) {
    	super(t);
    }

    public OpsServiceException(String msg, Throwable t) {
        super(msg, t);
        this.setStackTrace(t.getStackTrace());
    }

}
