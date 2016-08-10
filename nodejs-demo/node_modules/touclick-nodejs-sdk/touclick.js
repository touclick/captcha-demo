/**
* @FileName: touclick.js
* @Description: 二次验证, 服务端验证
* @author delete
* @date 2016年5月17日 下午6:37:06
* @version 1.0
 */
var _pubkey,_prikey;
var  HTTP = "http://", POSTFIX = ".touclick.com/sverify.touclick2?",POSTFIX_CB = ".touclick.com/callback?";
var status = {
	STATUS_OK :             {code: 0, message: '验证成功'},
    STATUS_TOKEN_EXPIRED :  {code: 1, message: '该验证已过期'},
    STATUS_NO_PUBKEY_ERROR :{code: 2, message: '公钥不可为空'},
    STATUS_TOKEN_ERROR :    {code: 3, message: '一次验证返回的token不可为空'},
    STATUS_PUBKEY_ERROR :   {code: 4, message: '公钥不正确或者checkCode与一次验证不符'},
    CHECKCODE_ERROR :       {code: 5, message: 'CheckCode有误,请确认CheckCode是否和一次验证传递一致'},
    STATUS_PARAM_ERROR :    {code: 6, message: 'sign加密错误,请检查参数是否正确'},
    STATUS_VERIFY_ERROR :   {code: 7, message: '一次验证错误'},
    STATUS_SERVER_ERROR :   {code: 8, message: '点触服务器异常'},
    STATUS_HTTP_ERROR :     {code: 9, message: 'http请求异常'},
    STATUS_JSON_TRANS_ERROR:{code: 10,message: 'json转换异常,可能是请求地址有误,请检查请求地址(http://sverify.touclick.com/sverify.touclick?参数'},
    CHECKADDRESS_ERROR :    {code: 11,message: '二次验证地址不合法'},
    SIGN_ERROR :            {code: 12,message: '签名校验失败,数据可能被篡改'}
},
statusByCode = (function(){
	var retArr = [];
	for(var item in status){
		retArr[status[item]['code']] = status[item];
	}
	return retArr;
})();

var crypto = require('crypto');
var md5 = function(content){
	var md5Handle = crypto.createHash('md5');
	md5Handle.update(content);
	return md5Handle.digest('hex');
}

var sign = (function(){
	var keyArr = ["s","i","b","un","ud","ip","ran"];
	keyArr.sort();

	return function(params,prikey){
		var signarr = [];
		keyArr.forEach(function(val){
			signarr.push(val + "=" + params[val]);
		});
		return md5(signarr.join("&") + prikey);
	}
})();

var signCallback = (function(){
	var keyArr = ["s","i","b","su","ip","ran"];
	keyArr.sort();

	return function(params,prikey){
		var signarr = [];
		keyArr.forEach(function(val){
			signarr.push(val + "=" + params[val]);
		});
		return md5(signarr.join("&") + prikey);
	}
})();

var getRandomString = function(){
	return crypto.randomBytes(24).toString('base64').replace(/\+|\//g,'0');
}
var request = require('request'), log4js = require('log4js'), qs = require('querystring');

log4js.configure({
    appenders: [
    	{ type: 'console' },
    	{ type: 'file', filename: 'touclick.log', category: 'touclick' }
    ]
});
var logger = log4js.getLogger('touclick'),log4jsLevel = process.env.LOG4JS_LEVEL ||'ERROR';
logger.setLevel(log4jsLevel);

module.exports  = {
	/**
	* @param pubkey 公钥
	* @param prikey 私钥
	* 在admin.touclick.com 中注册账号获取公钥与私钥
	 */
	init : function(pubkey, prikey){
		_pubkey = pubkey;
		_prikey = prikey;
		return pubkey&&prikey;
	},
	/**
	* @param token 二次验证口令，单次有效
	* @param checkAddress 二次验证地址，二级域名
	* @param sid 验证批次号
	* @param callback 回调函数,参数：{code:0,message:'验证正确'}
	 */
	check : function(token, checkAddress, sid, callback){
		if(!checkAddress || !/^[_\-0-9a-zA-Z]+$/.test(checkAddress) || !token || !sid){
			callback(status.STATUS_HTTP_ERROR);
		}
		var params = {"s": sid, "i": token, "b": _pubkey, "un": "", "ud": 0, "ip": "",ran: getRandomString()};

        params["sign"] = sign(params, _prikey);
        var url = HTTP + checkAddress + POSTFIX;
        logger.debug(url);
        try{
			request({
				method: "GET",
				uri: url + qs.stringify(params),
				timeout: 5000
			}, function (error, response, body) {
				if (!error && response.statusCode == 200) {
					logger.debug("response.statusCode:" + response.statusCode + "\tbody:" + body);
					var res;
					try{
						res = JSON.parse(body);
						if(!res){
							callback(status.STATUS_JSON_TRANS_ERROR);
						}
					}catch(e){
						logger.error(error);
						callback(status.STATUS_JSON_TRANS_ERROR);
					}
					if(res["sign"]){
						var checkParam = {"code":res["code"],"timestamp":params["ran"]};//code 与 timestamp 的顺序不可以修改
						var localSign = md5(qs.stringify(checkParam) + _prikey);
						logger.debug(localSign);
						if(localSign !== res["sign"]){
							callback(status.SIGN_ERROR);
						}
						callback(statusByCode[res["code"]], res["ckCode"]);
					}else if(res["code"] !== 0 ){
						logger.warn(res);
						callback(statusByCode[res["code"]])
					}else{
						logger.error("STATUS_SERVER_ERROR : "+ status.STATUS_SERVER_ERROR);
						callback(status.STATUS_SERVER_ERROR);
					}

				}else{
					logger.error(error);
					callback(status.STATUS_HTTP_ERROR);
				}
			})
        }catch(e){
        	logger.error(e);
        	callback(status.STATUS_HTTP_ERROR);
        }

	},
	callback : function(token, checkAddress, sid, isLoginSucc){
		if(!checkAddress || !/^[_\-0-9a-zA-Z]+$/.test(checkAddress) || !token || !sid){
			logger.error("param error in callback");
			return false;
		}
		params = { "ip": '', "i": token, "s": sid, "b": _pubkey, "su":(isLoginSucc?"1":"0"), "ran": getRandomString()};
		params["sign"] = signCallback(params, _prikey);
        var url = HTTP + checkAddress + POSTFIX_CB;
        logger.debug(url);
        try{
			request({
				method: "GET",
				uri: url + qs.stringify(params),
				timeout: 5000
			}, function (error, response, body) {
				
			})
        }catch(e){
        	logger.error(e);
        }

	},
	sign: sign,
	md5: md5
};