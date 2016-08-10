# 点触验证码 Golang SDK

## 开发环境

 - Golang (推荐1.4.1以上版本）

## 文件说明

* activate/ 激活功能，为了确保您的公钥能正常使用，请务必使用配套的SDK进行激活
* captcha-demo/ web项目调用示例
* touclick/ SDK源码

## 公钥激活

`为了公钥能正常使用，请务必进行激活，如更换SDK，则需要使用新SDK的激活程序重新进行激活`

`激活过后，建议删除 activate/ 文件夹`

进入activate文件夹，运行:

```bash
$ go run activate.go
```

在浏览器中访问http://127.0.0.1:8088,按照引导提示操作


## 使用指南

1. 安装SDK

  将下载下来的touclick文件夹放于$GOPATH/src目录下

2. 初始化验证类

  ```golang

  import touclick

  //pub_key 是你的公钥
  //pri_key 是你的私钥
  var tc = touclick.NewTouclick(pub_key, pri_key)
  ```

3. 二次验证

  ```golang
  var tc = touclick.NewTouclick(pub_key, pri_key)
  check_code := req.PostFormValue(touclick.CHECK_CODE)
  check_address := req.PostFormValue(touclick.CHECK_ADDRESS)
  token := req.PostFormValue(touclick.TOKEN)
  user_name := ""  //可选
  user_id := ""    //可选
  status := tc.Check(check_code,check_address,token,user_name,user_id)
  if status.code == 0{
      #执行自己的程序逻辑
  }
  ```

  `Check`方法的返回对象为Status`，可能的取值如下所示：

  ```golang
  Status{Code : 0, Msg : ""}
  Status{Code : 1, Msg : "该验证已过期"}
  Status{Code : 2, Msg : "公钥不可为空"}
  Status{Code : 3, Msg : "一次验证返回的token为必需参数,不可为空"}
  Status{Code : 4, Msg : "公钥不正确"}
  Status{Code : 5, Msg : "CheckCode有误,请确认CheckCode是否和一次验证传递一致"}
  Status{Code : 6, Msg : "sign加密错误,请检查参数是否正确"}
  Status{Code : 7, Msg : "一次验证错误"}
  Status{Code : 8, Msg : "点触服务器异常"}
  Status{Code : 9, Msg : "http请求异常"}
  Status{Code : 10, Msg : "json转换异常,可能是请求地址有误,请检查请求地址(http://[checkAddress].touclick.com/sverify.touclick?参数)"}
  Status{Code : 11, Msg : "二次验证地址不合法"}
  Status{Code : 12, Msg : "签名校验失败,数据可能被篡改"}
  ```

## 运行demo

进入captcha-demo文件夹，

1. 在static/login.html中填写
  ```html
  <script src="http://js.touclick.com/js.touclick?b=公钥（从点触官网申请）" ></script>
  ```

2. 在demo.go中填写
  ```golang
  import touclick

  var pubKey = "你的公钥（官网申请）"
  var priKey = "你的私钥（官网申请）"
  ```

3. 运行demo
  ```bash
  $ go run demo.go
  ```

在浏览器中访问http://127.0.0.1:8080 即可看到demo界面

### 联系我们：
（商务洽谈）官Q1：3180210030 ，电话010-53608568

（技术支持）官Q1：495067988  
