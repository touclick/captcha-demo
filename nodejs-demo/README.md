# 点触验证码 Nodejs DEMO

### 开发环境
* Nodejs 0.12.x

### 文件说明

* `activate/` 激活功能，为了确保您的公钥能正常使用，请务必使用配套的SDK进行激活
* `post.js` 是二次验证的调用示例

### 使用指南

1. 安装依赖

	```bash
	$ npm install #安装依赖包
	```
2. 填写公钥

	请修改 post.js ，填写公钥与私钥

	```javascript
	/**
	 * 请于http://admin.touclick.com 注册以获取公钥与私钥
	 */
	var pubkey = "",
		prikey = "";
		
	touclickSdk.init(pubkey, prikey);
	```

	请修改 index.html ,在正确位置填写公钥

	```xml
	<script src="http://js.touclick.com/js.touclick?b=公钥(从点触官网申请)" ></script>
	```

3. 公钥激活

	`为了公钥能正常使用，请务必进行激活，如更换SDK，则需要使用新SDK的激活程序重新进行激活`

	`激活过后，建议删除 activate/ 文件夹`

	```bash
	$ cd nodejs-demo/activate
	$ node activate.js #务必在 nodejs-demo/activate 下执行
	```

	浏览器访问 `http://127.0.0.1:3000/`, 按照页面提示进行激活，激活成功后，退出`node`

4. 启动服务

	```bash
	$ cd nodejs-demo
	$ npm start
	```

	浏览器访问`http://127.0.0.1:3000/index.html`就可体验

### 依赖

```xml
"dependencies": {
    "finalhandler": "^0.4.1",
    "router": "^1.1.4",
    "touclick-nodejs-sdk": "^1.0.0" 
  }
```
`touclick-nodejs-sdk` 是点触验证码Nodejs SDK ，当您准备将点触添加到您的项目中去时，请先依赖该项目

```bash
$ cd /project/to/path 
$ npm install --save touclick-nodejs-sdk
```

关于SDK，详见 [https://github.com/zyayhj/touclick-nodejs-sdk](https://github.com/zyayhj/touclick-nodejs-sdk#readme "nodejs-sdk")

### 联系我们：
（商务洽谈）官Q1：3180210030 ，电话010-53608568

（技术支持）官Q1：495067988  

