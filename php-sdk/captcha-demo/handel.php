<?php
/**
 * @date: 2016-3-23
 * @author : rainbow 
 */
require dirname(dirname(__FILE__)).'/captcha-php-sdk/touclick.php';
header("Content-type: text/html; charset=utf-8");
if (empty($_POST['checkAddress'])||empty($_POST['token'])) {
	exit('提交数据为空');
}
$checkCode = strip_tags($_POST['checkCode']);
$checkAddress = strip_tags($_POST['checkAddress']);
$token = strip_tags($_POST['token']);
if (!preg_match('/^[\w\-]/',$checkAddress)){
	exit('参数格式不正确');
}
/*$PUBKEY 、 $PRIKEY 从http://admin.touclick.com注册获取 */
$PUBKEY = '';
$PRIKEY = '';

$touclick = new TouClick($PUBKEY,$PRIKEY);
$res = $touclick->check($checkCode, $checkAddress, $token);

//$res['code'] 的详细说明请看README.md
if ($res) {
	var_dump ($res);
} else {
	var_dump('验证成功');
}
?>
