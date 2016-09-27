1.我的网站使用dz平台建站，使用了https后验证码就出不来了
 
 	原因：dz插件版本的验证码是http的，新版发布后才支持https

 	解决：找到source/plugin/touclick/touclick.class.php这个文件

       	把里边的 http://js.touclick.com 的地方 改成 //js.touclick.com，即去掉前面的http:（目前只有一处）
 
2.我的网站用的dz插件版本验证码，pc端能出来验证吗，移动端出不来

 	原因：旧版dz插件有一版确实不支持移动端

 	解决：新版发布后会支持

3.我是dz论坛安装了之后 不能正常注册了 怎么办？

 	解决：看是不是因为验证码出不来，查看是不是问题1的原因。