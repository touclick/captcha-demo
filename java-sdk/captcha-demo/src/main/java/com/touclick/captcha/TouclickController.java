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
* @date 2016年3月11日 上午9:32:17
 */
@Controller
public class TouclickController {
    
    private TouClick touclick = new TouClick(); 
    
    private static final String PUBKEY = "";//公钥(从点触官网获取)
    private static final String PRIKEY = "";//私钥(从点触官网获取)
    
    /**
    * @Title: verify
    * @Description: 服务端请求TouClick二次验证
    * @param @param request
    * @param @param response    设定文件
    * @return void    返回类型
    * @throws
     */
    @RequestMapping(value = "/verify",method = RequestMethod.POST)
    public void  verify(final HttpServletRequest request,HttpServletResponse response){
    	
        String checkAddress = request.getParameter("checkAddress");
        String token = request.getParameter("token");
        //一次验证传递的参数,同一次验证一样
        String checkCode = request.getParameter("checkCode");
        Status status = null;
        try {
            status = touclick.check(checkCode,checkAddress,token,PUBKEY,PRIKEY);
        } catch (TouclickException e) {
            System.out.println(e);
        }
        System.out.println("checkAddress :"+checkAddress + ",token:" + token+ ",checkCode:" + checkCode);
        System.out.println("code :"+status.getCode() + ",message:" + status.getMessage());
        if(status != null && status.getCode()==0){
            //执行自己的程序逻辑
        }
        
    }
    
}
