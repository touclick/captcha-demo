package com.touclick.captcha.service;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.touclick.captcha.exception.TouclickException;
import com.touclick.captcha.http.HttpClient;
import com.touclick.captcha.http.Response;
import com.touclick.captcha.model.Parameter;
import com.touclick.captcha.model.Result;
import com.touclick.captcha.model.Status;
import com.touclick.captcha.util.TouclickUtil;

/**
* @ClassName: TouClick
* @Description: 请求二次验证, 服务端验证
* @author zhanwei
* @date 2016年5月17日 下午4:37:06
* @version 1.0
* 
* 说明：
* 	请求点触服务器进行二次验证
 */
public class TouClick implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(TouClick.class);

    private static final long serialVersionUID = -176092625883595547L;
    private static final String HTTP = "http://";
    private static final String CHECK_POSTFIX = ".touclick.com/sverify.touclick2";
    private static final String CALLBACK_POSTFIX = ".touclick.com/callback";

    private HttpClient client = new HttpClient();

    /**
     * 请求二次验证, 服务端验证
     *
     * @param checkAddress  二次验证地址，二级域名
     * @param sid session id
     * @param token     二次验证口令，单次有效
     * @param pubKey    公钥
     * @param priKey    私钥
     * @return Status   返回类型
     * @throws TouclickException
     */
    public Status check(String checkAddress, String sid,String token,String pubKey,String priKey) throws TouclickException {
        return this.check(checkAddress,sid, token,pubKey, priKey, "", "");
    }

    /**
     * 请求二次验证, 服务端验证
     *
     * @param checkAddress  二次验证地址，二级域名
     * @param token     二次验证口令，单次有效
     * @param sid session id
     * @param pubKey    公钥
     * @param priKey    私钥
     * @param userName  请求用户名 用于统计分析
     * @param userId    请求用户id 用于统计分析
     * @return Status    返回类型
     * @throws TouclickException
     */
    public Status check(String checkAddress, String sid,String token,String pubKey,String priKey, String userName, String userId) throws TouclickException {
        if (checkAddress == null || "".equals(checkAddress)
                || pubKey == null || "".equals(pubKey)
                || priKey == null || "".equals(priKey)
                || token == null || "".equals(token)
                || sid == null || "".equals(sid)) {
            throw new TouclickException("参数有误");
        }
        Pattern pattern = Pattern.compile("^[_\\-0-9a-zA-Z]+$");
        Matcher matcher = pattern.matcher(checkAddress);
        if(!matcher.matches()){
            return new Status(Status.CHECKADDRESS_ERROR,"0", Status.getCause(Status.CHECKADDRESS_ERROR));
        }
        
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("i", token));
        params.add(new Parameter("b", pubKey));
        params.add(new Parameter("s",sid));
        try {
            params.add(new Parameter("ip", InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {

        }
        params.add(new Parameter("un", userName));
        params.add(new Parameter("ud", userId));
        String ran = UUID.randomUUID().toString();
        params.add(new Parameter("ran", ran));
        String sign = TouclickUtil.buildMysign(params, priKey);
        StringBuilder url = new StringBuilder();
        url.append(HTTP).append(checkAddress).append(CHECK_POSTFIX);
        params.add(new Parameter("sign", sign));
        Response response = null;
        try {
            response = client.get(url.toString(), params);
        } catch (TouclickException e1) {
            LOGGER.error(e1.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        if (response != null) {
            Result result = null;
            try {
            	System.out.println("info:"+response.getInfo());
            	result = mapper.readValue(response.getInfo(), Result.class);
                if(result.getCode() == 0){
                    if(result.getSign() != null && !"".equals(result.getSign())
                	        && result.getSign().equals(buildSign(result.getCode(),ran,priKey))){
                        return new Status(result.getCode(),result.getCkCode(),result.getMessage());
                    }else{
    	      	        return new Status(Status.SIGN_ERROR,result.getCkCode(), Status.getCause(Status.SIGN_ERROR));
                    }
                }
                return new Status(result.getCode(),result.getCkCode(),result.getMessage());
            } catch (Exception e) {
                LOGGER.error("transfer json error ..", e);
            }
            return new Status(Status.STATUS_JSON_TRANS_ERROR,"0", Status.getCause(Status.STATUS_JSON_TRANS_ERROR));
        }
        return new Status(Status.STATUS_HTTP_ERROR,"0", Status.getCause(Status.STATUS_HTTP_ERROR));
    }

    /**
     * 用户名密码校验后的回调方法
     *
     * @param checkAddress  二次验证地址，二级域名
     * @param token     二次验证口令，单次有效
     * @param sid session id
     * @param isLoginSucc 用户名和密码是否校验成功
     * @throws TouclickException
     */
    public void callback(String checkAddress,String sid, String token,String pubKey,String priKey,boolean isLoginSucc) throws TouclickException {
        if (checkAddress == null || "".equals(checkAddress)
                || token == null || "".equals(token)
                || pubKey == null || "".equals(pubKey)
                || priKey == null || "".equals(priKey)
                || sid == null || "".equals(sid)) {
            throw new TouclickException("参数有误");
        }
        Pattern pattern = Pattern.compile("^[_\\-0-9a-zA-Z]+$");
        Matcher matcher = pattern.matcher(checkAddress);
        if(!matcher.matches()){
            return ;
        }

        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("i", token));
        params.add(new Parameter("b", pubKey));
        params.add(new Parameter("s",sid));
        try {
            params.add(new Parameter("ip", InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {

        }
        params.add(new Parameter("su", isLoginSucc ? "1" : "0"));
        String ran = UUID.randomUUID().toString();
        params.add(new Parameter("ran", ran));

        String sign = TouclickUtil.buildMysign(params, priKey);
        params.add(new Parameter("sign", sign));

        StringBuilder url = new StringBuilder();
        url.append(HTTP).append(checkAddress).append(CALLBACK_POSTFIX);

        try {
        	client.get(url.toString(), params);
        } catch (TouclickException e1) {
            LOGGER.error(e1.getMessage());
        }
    }

    private String buildSign(int code, String ran, String priKey) {
        List<Parameter> params = new ArrayList<Parameter>();
	    params.add(new Parameter("code", code));
	    params.add(new Parameter("timestamp", ran));
	    return TouclickUtil.buildMysign(params, priKey);
    }

}
