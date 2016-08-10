##点触验证码 Nodejs SDK

#### 更新历史

1. `1.0.8`及之前版本适配SDK版本v5-1-0(点触后台可查看)

2. `1.0.9`及以后版本适配SDK版本v5-2-0

#### DEMO

DEMO ：[http://github.com/touclick/captcha-demo/](https://github.com/touclick/captcha-demo/tree/master/nodejs-demo "nodejs-demo")

#### 安装

```bash
$ npm install --save touclick-nodejs-sdk
```

#### 如何使用

1. 引用：

	```javascript
	var tc = require("touclick-nodejs-sdk");
	```

2. 初始化

	```javascript
	/**
	 * @param pubkey 公钥
	 * @param prikey 私钥
	 * 公钥、私钥，在http://admin.touclick.com 注册以获取公钥和私钥
	 */
	tc.init(pubkey,prikey);
	```

3. 二次验证

	```javascript
	/**
	 * @param token 二次验证口令，单次有效
	 * @param checkAddress 二次验证地址，二级域名
	 * @param sid 验证批次号
 	 * @param callback 回调函数,参数：{code:0,message:'验证正确'}
	 */
	tc.check(token, checkAddress, sid, callback);

	```

	回调函数参数

	```javascript

	result = {code: 0,message: '验证正确'}

	callback(result)
	
	```


##### LOG4JS

默认日志输出为ERROR，可以通过修改环境变量 `LOG4JS_LEVEL`，更改日志输出等级

日志输出路径是 `touclick.log`

```javascript
var logger = log4js.getLogger('touclick'),
log4jsLevel = process.env.LOG4JS_LEVEL ||'ERROR';
logger.setLevel(log4jsLevel);
```
