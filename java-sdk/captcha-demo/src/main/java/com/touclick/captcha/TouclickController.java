package com.touclick.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.touclick.captcha.exception.TouclickException;
import com.touclick.captcha.model.Status;
import com.touclick.captcha.service.TouClick;

/**
* @ClassName: TouclickController
* @Description: 请求二次验证的服务端
* @author zhanwei
* @date 2016年5月17日 下午4:32:42
* @version 1.0
 */
@Controller
public class TouclickController {
    
    private TouClick touclick = new TouClick(); 
    
    private static final String PUBKEY = "";//公钥(从点触官网获取)
    private static final String PRIKEY = "";//私钥(从点触官网获取)
    
    /**
     * @throws TouclickException 
    * @Title: verify
    * @Description: 服务端请求TouClick二次验证
    * @param @param request
    * @param @param response    设定文件
    * @return void    返回类型
    * @throws
     */
    @RequestMapping(value = "/verify",method = RequestMethod.POST)
    public void  verify(final HttpServletRequest request,HttpServletResponse response) throws TouclickException{
    	/*
    	*  token 二次验证口令，单次有效
    	*  checkAddress 二次验证地址，二级域名
    	*  checkCode 校验码，开发者自定义，一般采用手机号或者用户ID，用来更细致的频次控制
    	*/
        String checkAddress = request.getParameter("checkAddress");
        String token = request.getParameter("token");
        String sid = request.getParameter("sid");
        Status status = touclick.check(checkAddress,sid,token,PUBKEY,PRIKEY);
        System.out.println("checkAddress :"+checkAddress + ",token:" + token);
        System.out.println("code :"+status.getCode() + ",message:" + status.getMessage());
        if(status != null && status.getCode()==0){
            //执行自己的程序逻辑
        }
        
    }
    
}
