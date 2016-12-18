
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

	/*
	 * 验证失败以后的回调函数
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 */
	onFailure:function(){
		/*
		 * 验证码验证失败事件。
		 * 可以写入验证失败后的提示信息，提高用户体验
		 *
		 */
	},

	/*
	 * 关闭验证码事件
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 */
	onClose:function(){
        /*
		 * 点击关闭按钮关闭验证码事件
		 *
		 */
    },

    /*
	 * 点击验证码图案事件
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 */
	onClick:function(){
        /*
		 * 验证码图案每一次点击的回调函数
		 *
		 */
    },
	
	/*
	 * 自定义点击图案
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 */
    hoverImg:"url",
	
	/*
	 * 自定义验证失败后显示的图案
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 */
    failureImg:"url",

    /*
	 * 设置验证码遮罩层
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 *
	 */
	 isOpenMask:function(env){
		/*
		 * 设置验证码的遮罩层
		 *  return true;  设置遮罩层
		 *  return false; 取消遮罩层
		 *  默认 return false;
		 *
		 *  可以使用官方提供的 env 参数
		 *  mob:屏幕宽度小于530
		 *  pc: 屏幕宽度大于530
		 */
		if(env == "pc"){
			return true;    //屏幕大于530px时 return true 或者 false 进行设置遮罩层设置
        }else if(env=="mob"){
            return true;    //屏幕小于530px时 return true 或者 false 进行设置遮罩层设置
        }
	 },

	 /*
	 * 点击遮罩层事件
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 *
	 */
	onMaskClick:function(e){
		/*
		 *  如果有遮罩，用户点击遮罩触发的事件
		 *  建议用法：可以利用该事件 注册 关闭验证码的 动作
		 */
    },

    /*
	 * 设置验证码位置
	 *
	 * @非必须参数,具体使用可联系点触技术服务,底部有联系方式
	 *
	 */
	isCaptchaFloat:function(env){
		/*
		 * 设置验证码的显示位置。
		 * 建议用法：配合env参数使用。 
		 *
		 */
    }
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
			document.getElementById('token').value = obj.token;
			document.getElementById('checkAddress').value = obj.checkAddress;
			document.getElementById('sid').value = obj.sid;
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
			document.getElementById('token').value = obj.token;
			document.getElementById('checkAddress').value = obj.checkAddress;
			document.getElementById('sid').value = obj.sid;
		}
	});
</script>
```
example3:

```html
<!-- jQuery 写法-->
<div id="target2"></div>
<!--以下隐藏域为示例代码，开发者自行选择将这3个属性传输到后台的方式-->
<input id="token" type="hidden"/>
<input id="checkAddress" type="hidden"/>
<input id="sid" type="hidden"/>

<script type="text/javascript">
	var tc = new TouClick($('#target2')[0],{
		onSuccess: function( obj){
			$('#token').val(obj.token);
			$('#checkAddress').val(obj.checkAddress);
			$('#sid').val(obj.sid);
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

/*
 * 触发关闭验证码动作。
 * @return void
 */
close
```
#######调用示例:

```html
<button id="destory"></button>
<button id="submit"></button>
<script type="text/javascript">
	var tc = new TouClick(document.getElementById('target2'),{
		onSuccess: function( obj){
			document.getElementById('token').value = obj.token;
			document.getElementById('checkAddress').value = obj.checkAddress;
			document.getElementById('sid').value = obj.sid;
		},
		onMaskClick:function(e){
            tc.close();   //点击关闭验证码
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
### 联系我们：

（客户服务）官Q1：<a href="https://touclick.com/?service=0" target="_blank">800161394</a> ，电话010-53608568

（技术支持）官Q1：<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=eae024d881e951c69bb4bbb41d1af9be9f4b861eb86bf48e8f35cf27cc24d98e">205658675</a>

