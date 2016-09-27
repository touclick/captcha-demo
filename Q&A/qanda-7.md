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
                readyCheck:function(){
                    if(TouClick.$('input2').value==''){
                        return {status:false,msg:'dfasd'}
                    }
                    return {status:true,checkCode:''}
                },
                onSuccess:function(obj){
                    TouClick.console("onclck",1);
                    // console.log(obj)
                    // console.log( tc.getStatus());
                },
                onFailure:function(){
                    TouClick.console('onFailure',1);
                    // console.log("on failure")
                },
                onClose:function(){
                    TouClick.console('onClose',1);
                    // console.log('on close')
                },
                onClick:function(){
                    TouClick.console("onclck",1);
                    // console.log("on click")
                },
                behaviorDom:'input2',
                hoverImg:"../img/hov.png",
                failureImg:"../img/hov.png",
                // successImg:"../img/hov.png",
                isOpenMask:function(env){
                        return true;
                    if(env == "pc"){
                    }else if(env=="mob"){
                        return true;
                    }
                },
                onMaskClick:function(e){
                    // console.log(this);
                    tc.close();
                    // console.log(e)
                },
                isCaptchaFloat:function(env){
                    //true 浮动样式
                    //false 嵌入样式
                    // return true;
                    // return true;

                    if(env == "pc"){
                        return true;
                    }else if(env == "mob"){
                        return true;
                    }
                }
            });
            TouClick.$('target2').onclick = function(){
                console.log(tc.getStatus())    //检测是否验证码成功  [true || false]
            }
            TouClick.$('target3').onclick = function(){
                tc.destory();
            }
        })
    </script>
</body>

</html>

```