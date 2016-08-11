<?php
class TouClick{
	var $pubkey;
	var $prikey;
	
	function __construct($pubkey='',$prikey=''){
		$this->pubkey = $pubkey;
		$this->prikey = $prikey;
	}
	
	/**
	 * @date: 2016-3-23 上午10:20:04
	 * @author : rainbow
	 * @param token 二次验证口令，单次有效
	 * @param checkAddress 二次验证地址，二级域名
	 * @param checkCode 校验码，开发者自定义，一般采用手机号或者用户ID，用来更细致的频次控制
	 * @return :验证成功 或 array(2) { ["code"]=> int(1) ["message"]=> string(23) "二次验证token失效" } 
	 */
	public function check($sid, $checkAddress, $token, $userName = '', $userId = 0) {
		if (empty ($checkAddress) || empty ($token) || empty($this->prikey) || empty($this->pubkey)) {
			exit ( '参数为空' );
		}
		if (!preg_match('/^[\w\-]/',$checkAddress)){
			exit('参数格式不正确');
		}
		$params ['s'] = $sid;
		$params ['un'] = $userName;
		$params ['ud'] = $userId;
		$params ['ip'] = @gethostbyname ( $_ENV ['COMPUTERNAME'] );
		$params ['i'] = $token;
		$params ['b'] = $this->pubkey;
		$params ['ran'] = md5(strtotime('now'));
		$sign = $this->getSign ( $params, $this->prikey );
		$params ['sign'] = $sign;
		$paramStr = $this->getStr ( $params );
		$url = "http://" . $checkAddress . ".touclick.com/sverify.touclick2" . '?' . $paramStr;
		// use curl
		$ch = curl_init ();
		curl_setopt ( $ch, CURLOPT_URL, $url );
		curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, 1 );
		curl_setopt ( $ch, CURLOPT_HEADER, 0 );
		$result = curl_exec ( $ch );
		curl_close ( $ch );
		$check = is_object(json_decode($result)) ? get_object_vars(json_decode($result)) : json_decode($result);
		if ($check['code'] == 0) {
			$checkParam['code'] = $check['code'];
			$checkParam['timestamp'] = $params ['ran'];
			$sign = $this->getSign($checkParam, $this->prikey);
			if ($sign == $check['sign']) {
				$res['code'] = 12;
				$res['message'] = '验证成功';
				$res['checkCode'] = $check['ckCode'];
				return $res;
			} else {
				$res['code'] = 12;
				$res['message'] = '签名校验失败,数据可能被篡改';
				return $res;
			}
		} else {
			unset($check['timestamp']);
			unset($check['sign']);
			return $check; 
		}
	}

	/**
     * 用户名密码校验后的回调方法
     *
     * @param checkAddress  二次验证地址，二级域名
     * @param token     二次验证口令，单次有效
     * @param sid session id
     * @param isLoginSucc 用户名和密码是否校验成功
     * 
     */

	public function callback($sid,$checkAddress,$token,$isLoginSucc){
		if (empty ($checkAddress) || empty ($sid) || empty ($token) || empty($this->prikey) || empty($this->pubkey)) {
			exit ( '参数为空' );
		}
		if (!preg_match('/^[\w\-]/',$checkAddress)){
			exit('参数格式不正确');
		}
		$params ['s'] = $sid;
		$params ['ip'] = @gethostbyname ( $_ENV ['COMPUTERNAME'] );
		$params ['i'] = $token;
		$params ['b'] = $this->pubkey;
		$params ['ran'] = md5(strtotime('now'));
		$params ['su'] = $isLoginSucc ? '1' : '0';
		$sign = $this->getSign ( $params, $this->prikey );
		$params ['sign'] = $sign;
		$paramStr = $this->getStr ( $params );
		$url = "http://" . $checkAddress . ".touclick.com/callback" . '?' . $paramStr;
		$ch = curl_init ();
		curl_setopt ( $ch, CURLOPT_URL, $url );
		curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, 1 );
		curl_setopt ( $ch, CURLOPT_HEADER, 0 );
		curl_exec ( $ch );
		curl_close ( $ch );
	}

	/**
	 * @date: 2016-3-23 上午10:30:15
	 * 
	 * @author : rainbow
	 * @param : $params        	
	 * @param : $prikey        	
	 * @return :md5($sign)
	 */
	private function getSign($params, $prikey) {
		ksort ( $params ) ? $paramsStr = $this->getStr ( $params ) : exit ( '排序失败' );
		$sign = $paramsStr . $prikey;
		return md5 ( $sign );
	}

	/**
	 * 数组=》字符串 key=value&key=value
	 * 
	 * @author : rainbow
	 * @param : $params        	
	 * @return : $paramsStr
	 */
	private function getStr($params) {
		$paramsStr = '';
		if (! $params || ! is_array ( $params )) {
			exit ( '参数错误' );
		}
		$keys = array_keys ( $params );
		foreach ( $params as $key => $value ) {
			if ($key == end ( $keys )) {
				$paramsStr .= $key . '=' . $value;
			} else {
				$paramsStr .= $key . '=' . $value . '&';
			}
		}
		return $paramsStr;
	}

}

?>
