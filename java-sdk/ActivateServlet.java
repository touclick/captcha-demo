package com.touclick.captcha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Servlet implementation class ActivateServlet
 */
public class ActivateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	
    private static final String VERSION = "5.2.0";//不可修改
    private static final String URL = "http://js.touclick.com/sdk/version/notify";
	
    /**
     * Default constructor. 
     */
    public ActivateServlet() {
    }

    /**
      * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
      */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setCharacterEncoding("utf-8");
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	out.println("<!DOCTYPE html>");
	out.println("<html>");
	out.println("<head>");
	out.println("<title>点触验证码公钥激活</title>");
	out.println("<script src='http://captcha-static.touclick.com/SDK/active/main-5-2-0-2.js?lan=java&v=5.2.0'></script>");
	out.println("</head>");
	out.println("<body>");  
        out.println("</body>");  
        out.println("</html>"); 
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  	response.setCharacterEncoding("utf-8");
	response.setContentType("text/html"); 
	PrintWriter out = response.getWriter(); 
	String b = request.getParameter("b");
	String z = request.getParameter("z");
	List<Parameter> params = new ArrayList<Parameter>();
	params.add(new Parameter("b",b));
	params.add(new Parameter("v",VERSION));
	String sign = buildMysign(params, z);
	params.add(new Parameter("sign", sign));
	String result = sendGet(URL,params);
	out.print(result);
    }
	
    private String sendGet(String url, List<Parameter> params) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" ;
            String preParams = "";
            for(Parameter param : params){
            	preParams += "&"+param.getName() + "=" + param.getValue();
            }
            urlNameString += preParams.substring(1);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
    private String buildMysign(List<Parameter> params, String key) {
	String prestr = createLinkString(params);  //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
	String mysign = sign(prestr, key, "utf-8");
	return mysign;
    }

    private String createLinkString(List<Parameter> params) {
	Map<String,String> map = new HashMap<String,String>();
	for(Parameter p : params){
	    map.put(p.getName(), p.getValue());
	}
	List<String> keys = new ArrayList<String>(map.keySet());
	Collections.sort(keys);

	String prestr = "";

	for (int i = 0; i < keys.size(); i++) {
	    String key = keys.get(i);
	    String value = map.get(key);

	    if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
		prestr = prestr + key + "=" + value;
	    } else {
	        prestr = prestr + key + "=" + value + "&";
	    }
	}
	return prestr;
    }
	
    public String sign(String text, String key, String input_charset) {
     	text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
	
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}

class Parameter{
    private String name;
    private String value;
    
    public Parameter(String name, String value) {
	super();
	this.name = name;
	this.value = value;
    }
    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }
    public String getValue() {
	return value;
    }
    public void setValue(String value) {
	this.value = value;
    }
}
