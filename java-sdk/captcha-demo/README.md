#点触验证码 Java SDK


##开发环境
  
  - JDK 1.5及以上
  - tomcat

##文件说明

* ActivateServlet.java/ 激活功能,为了确保您的公钥能正常使用,请务必使用配套的SDK进行激活
* captcha-demo/ web项目调用演示
* captcha-java-sdk/ SDK源码
* lib/ 项目中可能需要的jar包
  
##演示demo

1. 在`TouclickController`中填写从点触官网注册获得的公钥和私钥
   ```java	
   private static final String PUBKEY = "";//公钥(从点触官网获取)
   private static final String PRIKEY = "";//私钥(从点触官网获取)
   ```
   
2. 在index.html中填写
   ```xml
      <script src="http://js.touclick.com/js.touclick?b=公钥(从点触官网获得)" ></script>
   ```
   
3. 运行demo   


##使用指南

####公钥激活
`为了公钥能正常使用,请务必进行激活,如更换SDK,则需要使用新SDK的激活程序重新进行激活
`激活过后,建议删除 ActivateServlet.java

1. 将ActivateServlet.java复制到自己的项目中或者新建web项目运行ActivateServlet.java

2. 在web.xml中添加如下配置
   ```xml
   <servlet>
      <servlet-name>activateServlet</servlet-name>
      <servlet-class>com.touclick.captcha.ActivateServlet</servlet-class>
   </servlet>
   <servlet-mapping>
      <servlet-name>activateServlet</servlet-name>
      <url-pattern>/activate.do</url-pattern>
   </servlet-mapping>
   ```

3. 启动项目访问`http://localhost:8080/项目名/activate.do`

   说明:端口号不一定是8080,依自己项目所使用的服务器为准;路径依自己放置的路径为准

4. 在打开的页面中按照引导提示操作激活

#激活成功后再进行如下操作:

####导包
导入java-sdk-1.0.0.jar以及其他相关jar包 (lib目录下)

####填写公钥私钥
   ```java
   private static final String PUBKEY = "";//公钥(从点触官网获取)
   private static final String PRIKEY = "";//私钥(从点触官网获取)
   ```

####调用SDK
   ```java
   String checkKey = request.getParameter("checkAddress");
   String token = request.getParameter("token");
   //一次验证传递的参数,同一次验证一样
   String checkCode = request.getParameter("checkCode");
   Status status = touclick.check(checkCode,checkKey,token,PUBKEY,PRIKEY);
   System.out.println("checkKey :"+checkKey + ",token:" + token+ ",checkCode:" + checkCode);
   System.out.println("code :"+status.getCode() + ",message:" + status.getMessage());
   if(status != null && status.getCode()==0){
       //执行自己的程序逻辑
   }
   ```

  `check`方法的返回值为status对象，对象属性code可能的取值如下所示：

  ```java
  (0, "验证正确")
  (1, "该验证已过期")
  (2, "公钥不可为空")
  (3, "一次验证返回的token为必需参数,不可为空")
  (4, "公钥不正确")
  (5, "CheckCode有误,请确认CheckCode是否和一次验证传递一致"),
  (6, "sign加密错误,请检查参数是否正确")
  (7, "一次验证错误")
  (8, "点触服务器异常")
  (9, "http请求异常")
  (10, "json转换异常,可能是请求地址有误,请检查请求地址(http://[checkAddress].touclick.com/sverify.touclick?参数)")
  (11, "二次验证地址不合法")
  (12, "签名校验失败,数据可能被篡改")
  ```
