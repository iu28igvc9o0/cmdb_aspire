package com.aspire.mirror.indexproxy.exception;

public class IndexProxyException extends RuntimeException {
	private static final long serialVersionUID = -5852434000742099565L;

	public IndexProxyException(String msg) {
        super(msg);
    }
    
    public IndexProxyException(Throwable cause) {
        super(cause); 
    }
    
    public IndexProxyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
