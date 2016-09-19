
## TouClick Captcha JavaScript Lib (v5-2-x) API文档


### 验证码类：TouClick
	
## 静态方法:

```javascript
/* 类似于 jQuery.ready 的功能，验证码相关代码均应在实参函数体中实现 */
TouClick.ready(function(){}) 
```

####### 调用实例:

```javascript
TouClick.ready(function(){
	...
	/*
	 * 在此处实现验证码调用逻辑
	 */
	...
});
```

## 构造函数:
		
```javascript
/*
 *  构造函数，调用方式可以是 new TouClick()  也可以是  TouClick()
 *  @param dom：验证码代码要嵌入的位置 default : document.body
 *  @param options: 初始化参数
 */
TouClick(dom, options) 
```

####### OPTIONS

```javascript
var options = {

	/*
	 * 验证成功以后的回调函数
	 * 需要调用者实现这个函数，并将参数 token,checkAddress,sid 传到服务器端
	 *
	 * @必须参数
	 */
	onSuccess: function( obj ) {
		/* 
		 * 点触 SDK会对 obj 赋值，obj 共有3个属性值分别是 token/checkAddress/sid 
		 * 均为字符串类型 ,请开发者将 3个属性值传输到后端服务，以进行二次验证
		 */
	},

	/*
	 * 表单是否填写完成的校验函数
	 * 
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 */
	readyCheck: function(){
		/*
		 * 若用户名或其他必须字段没有填写，可返回如下对象
		 * return {status: false, errorMsg: "表单未完成的提示信息"}
		 */

		/*
		 * 若需正常进行点触校验，则返回 {status: true, ckCode:''} ，ckCode用法请咨询点触技术服务,底部有联系方式
		 * return {status: true, ckCode:''}
		 */
	},

	/*
	 * 需要监控键盘行为的输入框 id 
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 */
	behaviorDom: ''

}
```

#######调用示例:

example1:

```html
<div id="target1"></div>
<!--以下隐藏域为示例代码，开发者自行选择将这3个属性传输到后台的方式-->
<input id="token" type="hidden"/>
<input id="checkAddress" type="hidden"/>
<input id="sid" type="hidden"/>

<script type="text/javascript">
	var tc = new TouClick('target1',{
		onSuccess: function( obj){
			document.getElementById('token') = obj.token;
			document.getElementById('checkAddress') = obj.checkAddress;
			document.getElementById('sid') = obj.sid;
		}
	});
</script>
```

example2:

```html
<div id="target2"></div>
<!--以下隐藏域为示例代码，开发者自行选择将这3个属性传输到后台的方式-->
<input id="token" type="hidden"/>
<input id="checkAddress" type="hidden"/>
<input id="sid" type="hidden"/>

<script type="text/javascript">
	var tc = new TouClick(document.getElementById('target2'),{
		onSuccess: function( obj){
			document.getElementById('token') = obj.token;
			document.getElementById('checkAddress') = obj.checkAddress;
			document.getElementById('sid') = obj.sid;
		}
	});
</script>
```

##对象方法:

```javascript
/* 
 * 销毁验证码实例并且从页面中移除验证码相关html
 * @return void
 */
destory

/*
 * 获取验证码状态，如用户已完成验证，则返回 true，若未完成验证则返回 false
 * @return boolean
 */
getStatus
```
#######调用示例:

```html
<button id="destory"></button>
<button id="submit"></button>
<script type="text/javascript">
	var tc = new TouClick(document.getElementById('target2'),{
		onSuccess: function( obj){
			document.getElementById('token') = obj.token;
			document.getElementById('checkAddress') = obj.checkAddress;
			document.getElementById('sid') = obj.sid;
		}
	});

	document.getElementById('destory').onclick = function(){
		tc.destory();//销毁
	}
	//提交表单的时候判断是否
	document.getElementById('submit').onclick = function(){
		var status = tc.getStatus();
		if(status){
			// 允许提交表单
		}else{
			// 给用户未完成验证的提醒
		}
	}
</script>
```
## 联系我们：
（商务洽谈）官Q1：3180210030 ，电话010-53608568

（技术支持）官Q1：495067988  

