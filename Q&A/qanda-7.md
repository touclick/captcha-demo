5.2.0版本的验证码更新内容
	
	1、事件：onFailure、onClose、onMaskClick、onSuccess、onClick

    2、配置：hoverImg、failureImg、successImg、isOpenMask、isCaptchaFloat、readyCheck、behaviorDom

	3、对象方法：close、destory、getStatus

demo:
```
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Demo</title>
</head>
<body style="padding-left:100px;" class="touclick-bg">
    <div id="body">
        <span id="a"></span>
        <!--宿主位置-->
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <input /></br>
        <style>
            input{
                margin-top:3px;
            }
        </style>
        <div id="target1" style="width:400px;">
                <input /></br>
                <input /></br>
                <input /></br>
                <input /></br>
                <input id="input2" /></br>
        </div>
        <div id="target2" style="width:400px;height:35px;margin-top:10px; background-color: #399dd8;">
            target2
        </div>
       <div id="target3" style="width:400px;height:35px;margin-top:10px; background-color: #399dd8;">
           target3
       </div>
    </div>
    <script>
        TouClick.ready(function(){

            var tc= TouClick('target1',{
                // 检测表单是否填写完整
                readyCheck:function(){
                    if(TouClick.$('input2').value==''){
                        return {status:false,msg:'表单未填写完整的提示信息'}
                    }
                    return {status:true,ckCode:''}
                },

                //验证成功后的回调函数
                onSuccess:function(obj){
                    console.log("success");
                },

                //验证失败后的回调函数
                onFailure:function(){

                    console.log("failure")
                },

                //手动关闭验证码的回调函数
                onClose:function(){

                    console.log('close')
                },

                //点击验证码的回调函数
                onClick:function(){

                    console.log("click")
                },

                //用户行为监控
                behaviorDom:'input2',

                //自定义点击验证码提示图案。
                hoverImg:"../img/hov.png",

                //自定义失败后显示的图案
                failureImg:"../img/hov.png",

                //自定义成功后显示的图案
                successImg:"../img/hov.png",
                
                /*
                    设置遮罩层显示状态
                    true:  显示
                    false: 隐藏
                 */
                isOpenMask:function(env){
                    if(env == "pc"){
                        return true;
                    }else if(env=="mob"){
                        return true;
                    }
                },

                /*
                    点击遮罩层效果
                 */
                onMaskClick:function(e){
                    tc.close();
                },

                /*
                 * 设置验证码位置
                 */
                isCaptchaFloat:function(env){
                    //true 浮动样式
                    //false 嵌入样式
                    
                    if(env == "pc"){
                        return true;
                    }else if(env == "mob"){
                        return true;
                    }
                }
            });
            
            /*
                检测验证码是否验证成功
                true: 成功
                false: 失败
             */
            TouClick.$('target2').onclick = function(){
                console.log(tc.getStatus());    
            };

            /*
                移除验证码插件
             */
            TouClick.$('target3').onclick = function(){
                tc.destory();
            }
        })
    </script>
</body>

</html>

```