package com.touclick.captcha.model;

public class Status {

    public static final int STATUS_OK = 0; //验证成功
    public static final int STATUS_TOKEN_EXPIRED = 1;//该验证已过期
    public static final int STATUS_NO_PUBKEY_ERROR = 2;//公钥不可为空
    public static final int STATUS_TOKEN_ERROR = 3;//一次验证返回的token不可为空
    public static final int STATUS_PUBKEY_ERROR = 4;//公钥不正确或者checkCode与一次验证不符
    public static final int CHECKCODE_ERROR = 5;//CheckCode有误,请确认CheckCode是否和一次验证传递一致
    public static final int STATUS_PARAM_ERROR = 6;//sign加密错误,请检查参数是否正确
    public static final int STATUS_VERIFY_ERROR = 7;//一次验证错误
    public static final int STATUS_SERVER_ERROR = 8;//点触服务器异常
    public static final int STATUS_HTTP_ERROR = 9;//http请求异常
    public static final int STATUS_JSON_TRANS_ERROR = 10;//json转换异常,可能是请求地址有误,请检查请求地址(http://sverify.touclick.com/sverify.touclick?参数)
    public static final int CHECKADDRESS_ERROR = 11;//二次验证地址不合法
    public static final int SIGN_ERROR = 12;//签名校验失败,数据可能被篡改
    
    
    private int code;
    
    private String checkCode;

    private String message;
    
    public Status() {
    }

    public Status(int code, String checkCode ,String message) {
        this.code = code;
        this.checkCode = checkCode;
        this.message = message;
    }

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

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
	
    public static String getCause(int statusCode) {
        String cause = null;
        switch (statusCode) {
            case STATUS_OK:
                break;
            case STATUS_TOKEN_EXPIRED:
                cause = "该验证已过期";
                break;
            case STATUS_NO_PUBKEY_ERROR:
                cause = "公钥不可为空";
                break;
            case STATUS_TOKEN_ERROR:
                cause = "一次验证返回的token为必需参数,不可为空";
                break;
            case STATUS_PUBKEY_ERROR:
                cause = "公钥不正确";
                break;
            case CHECKCODE_ERROR:
                cause = "CheckCode有误,请确认CheckCode是否和一次验证传递一致";
                break;
            case STATUS_PARAM_ERROR:
                cause = "sign加密错误,请检查参数是否正确";
                break;
            case STATUS_VERIFY_ERROR:
                cause = "一次验证错误";
                break;
            case STATUS_SERVER_ERROR:
                cause = "点触服务器异常";
                break;
            case STATUS_HTTP_ERROR:
                cause = "http请求异常";
                break;
            case STATUS_JSON_TRANS_ERROR:
                cause = "json转换异常,可能是请求地址有误,请检查请求地址(http://[checkAddress].touclick.com/sverify.touclick?参数)";
                break;
            case CHECKADDRESS_ERROR:
                cause = "二次验证地址不合法";
                break;
            case SIGN_ERROR:
                cause = "签名校验失败,数据可能被篡改";
                break;
            default:
                cause = "";
        }
        return cause;
    }

}
