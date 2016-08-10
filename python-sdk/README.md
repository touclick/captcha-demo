# 点触验证码 Python SDK

## 开发环境

 - Python (推荐2.7.0以上版本）
 - requests
 - tornado框架

## 文件说明

* setup.py、requirements.txt 安装SDK
* activate/ 激活功能，为了确保您的公钥能正常使用，请务必使用配套的SDK进行激活
* captcha-demo/ web项目调用示例
* captcha-python-sdk/ SDK源码

## 公钥激活

`为了公钥能正常使用，请务必进行激活，如更换SDK，则需要使用新SDK的激活程序重新进行激活`

`激活过后，建议删除 activate/ 文件夹`

进入activate文件夹，运行:

```bash
$ python start.py
```

在浏览器中访问http://127.0.0.1:8088 按照引导提示操作


## 使用指南

1. 安装SDK

  ```bash
  $ sudo python setup.py install
  ```

2. 初始化验证类

  ```python
  # pub_key 是你的公钥
  # pri_key 是你的私钥
  tc = TouclickLib(pub_key, pri_key)
  ```

3. 二次验证

  ```python
  tc = TouclickLib(pub_key, pri_key)
  sid = self.get_argument(tc.SID, "")
  check_address = self.get_argument(tc.CHECK_ADDRESS, "")
  token = self.get_argument(tc.TOKEN, "")
  code, check_code, msg = tc.check(check_address, sid, token)
  if code == 0:
      #执行自己的程序逻辑
      #tc.callback(check_address, sid, token, True)
      pass
  ```

  `check`方法的返回值为元组`(code, check_code, message)`，可能的取值如下所示：

  ```python
  (0, "ckCode", "验证正确")
  (1, "ckCode", "该验证已过期")
  (2, "ckCode", "公钥不可为空")
  (3, "ckCode", "一次验证返回的token为必需参数,不可为空")
  (4, "ckCode", "公钥不正确")
  (5, "ckCode", "CheckCode有误,请确认CheckCode是否和一次验证传递一致"),
  (6, "ckCode", "sign加密错误,请检查参数是否正确")
  (7, "ckCode", "一次验证错误")
  (8, "ckCode", "点触服务器异常")
  (9, "ckCode", "http请求异常")
  (10, "ckCode", "json转换异常,可能是请求地址有误,请检查请求地址(http://[checkAddress].touclick.com/sverify.touclick2?参数)")
  (11, "ckCode", "二次验证地址不合法")
  (12, "ckCode", "签名校验失败,数据可能被篡改")
  ```

## 运行demo

进入captcha-demo文件夹，

1. 在static/login.html中填写
  ```html
  <script src="http://js.touclick.com/js.touclick?b=公钥（从点触官网申请）" ></script>
  ```

2. 在start.py中填写
  ```python
  from touclick import TouclickLib

  pub_key = "" #公钥
  pri_key = "" #私钥
  ```

3. 运行demo
  ```bash
  $ python start.py
  ```

在浏览器中访问http://127.0.0.1:8088 即可看到demo界面

## 联系我们：

（商务洽谈）官Q1：3180210030 ，电话010-53608568

（技术支持）官Q1：495067988  
