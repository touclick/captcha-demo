package com.touclick.captcha.model;

public class Result {
	
	private int code;
	
	private String message;
	
	private String sign;
	
	private String ckCode;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String getCkCode() {
		return ckCode;
	}

	public void setCkCode(String ckCode) {
		this.ckCode = ckCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	
	@Override
	public String toString() {
		return this.code + ":" + this.message + ":" + this.sign ;
	}
}
