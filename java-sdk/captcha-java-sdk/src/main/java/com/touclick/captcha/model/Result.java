package com.touclick.captcha.model;

public class Result {
	
	private int code;
	
	private String message;
	
	private long timestamp;
	
	private String sign;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	
	@Override
	public String toString() {
		return this.code + ":" + this.message + ":" + this.sign + ":" + this.timestamp;
	}
}
