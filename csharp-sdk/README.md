#点触验证码 Net SDK


##开发环境
  
  - .Net 2.0
  - IIS

##文件说明
 
* csharp-demo/ web项目调用演示
* csharp-sdk/ SDK源码
* activate/ 激活功能
* lib/ 依赖dll文件



##使用指南

`为了公钥能正常使用，请务必进行激活，如更换SDK，则需要使用新SDK的激活程序重新进行激活`

1. 在csharp-demo/CaptchaDemo.cs中填写公钥以及私钥。

    ```csharp
    private static final String PUBKEY = "";//公钥(从点触官网获取)
    private static final String PRIKEY = "";//私钥(从点触官网获取)
    ```

2. 在netDemo.html中填写

    ```xml
    <script src="http://js.touclick.com/js.touclick?b=公钥(从点触官网获得)" ></script>
    ```

3. 公钥激活

    `为了公钥能正常使用，请务必进行激活，如更换SDK，则需要使用新SDK的激活程序重新进行激活`

    `激活过后，建议删除CaptchaActive.cs`
	
    * 将CaptchaActive.cs复制到自己的项目中或者新建web项目运行CaptchaActive.cs

    * 在Web.config中添加如下配置
    ```xml
    <system.webServer>
      <validation validateIntegratedModeConfiguration="false" />
      <handlers>
        <add name="active" path="activate.touclick" verb="*" type="TouclickSdk.CaptchaActive"  />
        <add name="demo" path="postdata" verb="*" type="TouclickSdk.CaptchaDemo"  />
      </handlers>
    </system.webServer>
    ```

    * 启动项目访问`http://localhost/项目名/index.html`
       
    * 在打开的页面中填写配套公钥与私钥进行激活，激活成功后可在官网后台查看版本号
 
4. 运行Demo

    * 添加csharp-sdk为csharp-demo项目的依赖

    * 启动项目访问`http://localhost/netDemo.html`

     	说明:端口号依自己项目所使用的服务器为准;路径依自己放置的路径为准

####调用SDK

将csharp-sdk作为依赖添加到实际项目中

调用方式：

```csharp
string username = context.Request.Form["username"];
string pwd = context.Request.Form["password"];
string checkAddress = context.Request.Form["checkAddress"];
string token = context.Request.Form["token"];
//一次验证传递的参数,同一次验证一样
string sid = context.Request.Form["sid"];
TouClickSDk.TouClick t = new TouClickSDk.TouClick();
Touclick.Status status = t.check(sid, checkAddress, token, PUBKEY, PRIKEY, username, pwd);          
Console.Write("checkAddress :" + checkAddress + ",token:" + token + ",sid:" + sid);
Console.Write("code :" + status.Code + ",message:" + status.Message);          
if (status != null && status.Code == 0)
{
    var isLoginSucc = false;
    //执行自己的程序逻辑               
}              
```

`check`方法的返回值为status对象，对象属性code可能的取值如下所示：

```
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

#### 联系我们：
（商务洽谈）官Q1：3180210030 ，电话010-53608568

（技术支持）官Q1：495067988  