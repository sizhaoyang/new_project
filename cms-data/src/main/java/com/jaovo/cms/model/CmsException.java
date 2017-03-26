package com.jaovo.cms.model;

public class CmsException extends RuntimeException {
	public CmsException() {

	}

	public CmsException(String message) {
		super(message);
	}

	public CmsException(Throwable cause) {
		super(cause);
	}

	// ------------ 只要是我们项目的异常,不是别的原因的一样,通常都是这种方式
	public CmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CmsException(String message, Throwable cause,
			boolean enableSupperssion, boolean writableStackTrace) {
		super(message, cause, enableSupperssion, writableStackTrace);
	}
}
