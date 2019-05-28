package com.founder.econdaily.common.entity;

import java.io.Serializable;

public class BaseResult implements Serializable {

	private static final long serialVersionUID = -3170017099972798963L;
	
	/**
	  *  响应状态码
	 */
	protected int status;
	
	/**
	  *  响应消息
	 */
	protected String message="";

	public BaseResult(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public long getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
