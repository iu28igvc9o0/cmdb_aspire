package com.aspire.mirror.indexadapt.exception;

public class IndexAdaptException extends RuntimeException {
	private static final long serialVersionUID = -5852434000742099565L;

	public IndexAdaptException(String msg) {
        super(msg);
    }
    
    public IndexAdaptException(Throwable cause) {
        super(cause); 
    }
    
    public IndexAdaptException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
