<?php

$param['b'] = strip_tags($_POST['b']);
$priKey = strip_tags($_POST['z']);
$param['v'] = '5.2.0';
$param['sign'] = getSign($param, $priKey);
$url = 'http://js.touclick.com/sdk/version/notify?'.http_build_query($param);

// use curl
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
curl_setopt($ch, CURLOPT_HEADER, 0);
$result = curl_exec($ch);
curl_close($ch);
echo $result;

/**
 * @author: rainbow
 * @param: $params
 *  @param: $prikey
 * @return:md5($sign)
 */
function getSign($params,$prikey){
	ksort($params) ? $paramsStr = getStr($params) : exit('排序失败');
	$sign = $paramsStr.$prikey;
	return md5($sign);
}

/**
 * 数组=》字符串  key=value&key=value
 * @author: rainbow
 * @param: $params
 * @return: $paramsStr
 */
function getStr($params){
	$paramsStr = '';
	if (!$params || !is_array($params)) {
		exit('参数错误');
	}
	$keys = array_keys($params);
	foreach ($params as $key => $value)
	{
		if ($key == end($keys)) {
			$paramsStr .= $key.'='.$value;
		} else
		{
			$paramsStr .= $key.'='.$value.'&';
		}
	}
	return $paramsStr;
}