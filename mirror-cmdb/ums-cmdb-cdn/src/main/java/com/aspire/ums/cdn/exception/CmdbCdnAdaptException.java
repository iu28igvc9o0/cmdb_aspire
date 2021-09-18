package com.aspire.ums.cdn.exception;

public class CmdbCdnAdaptException extends RuntimeException {
	private static final long serialVersionUID = 9133783943764623679L;

	public CmdbCdnAdaptException(String message) {
        super(message);
    }

    public CmdbCdnAdaptException(String message, Throwable cause) {
        super(message, cause);
    }

    public CmdbCdnAdaptException(Throwable cause) {
        super(cause);
    }
}
