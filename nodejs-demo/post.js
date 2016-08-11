/**
* @FileName: touclick.js
* @Description: 二次验证, 服务端验证
* @author delete
* @date 2016年5月18日 下午1:36:01
* @version 1.0
 */
var finalhandler = require('finalhandler'), http = require('http'), Router = require('router'), fs = require('fs'),querystring = require('querystring');
var touclickSdk = require('touclick-nodejs-sdk');
/**
 * 请于http://admin.touclick.com 注册以获取公钥与私钥
 */
var pubkey = "你的公钥（官网申请）",
	prikey = "你的私钥（官网申请）";

var username = "admin",password = "admin";//DEMO演示用，一般情况可忽略

touclickSdk.init(pubkey, prikey);

/**
 * on linux,you can "$ export PORT=3001".
 */
var port = normalizePort(process.env.PORT || '3000');

function renderFile(filePath){
	return function(req, res){
		fs.readFile(filePath, function(err, data){
			res.writeHead(200, {'Content-Type': 'text/html'});
			res.write(data);
			res.end();
		});
	}
}

var router = Router();

router.get('/', renderFile("index.html"));
router.get('/index', renderFile("index.html"));
router.get('/index.html', renderFile("index.html"));

router.post('/postdata',function(req, res){
	req.setEncoding('utf-8');
    var postData = "";
    req.addListener("data", function (postDataChunk) {
        postData += postDataChunk;
    });
    req.addListener("end", function () {
        var params = querystring.parse(postData);
        console.log(JSON.stringify(params));

        var token = params["token"],checkAddress = params["checkAddress"],sid = params["sid"];
        
        touclickSdk.check(token,checkAddress,sid, function(result,ckCode){
			console.log(result);
			console.log(ckCode);//请查阅点触的技术文档，或咨询点触工程师了解ckCode用法，有助于增强安全性；一般情况下可忽略该参数
	        if(result["code"] === 0){
	        	var isLoginSucc = false;
	        	if(params["username"] == username && params["password"] == password){
	        		isLoginSucc = true;
	        	}
	        	//通知点触服务器是否登录成功，既用户名和密码校验是否正确
	        	touclickSdk.callback(token,checkAddress,sid,isLoginSucc);
	        	res.end("Captcha verify SUCCESS; username and password : " + isLoginSucc);
	        }else{
	        	res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'});
				res.end(JSON.stringify(result));
	        }
        });
       
    });
});
/**
* Create HTTP server.
*/
var server = http.createServer(function(req, res) {
	console.log(req.url)
  	router(req, res, finalhandler(req, res));
})

/**
 * Listen on provided port, on all network interfaces.
 */

server.listen(port);
server.on('error', onError);
server.on('listening', onListening);

/**
 * Normalize a port into a number, string, or false.
 */

function normalizePort(val) {
	var port = parseInt(val, 10);

	if (isNaN(port)) {
	// named pipe
	return val;
	}

	if (port >= 0) {
	// port number
	return port;
	}

	return false;
}

/**
 * Event listener for HTTP server "error" event.
 */

function onError(error) {
	if (error.syscall !== 'listen') {
	throw error;
	}

	var bind = typeof port === 'string'
	? 'Pipe ' + port
	: 'Port ' + port;

	// handle specific listen errors with friendly messages
	switch (error.code) {
	case 'EACCES':
		console.error(bind + ' requires elevated privileges');
		process.exit(1);
		break;
	case 'EADDRINUSE':
		console.error(bind + ' is already in use');
		process.exit(1);
		break;
	default:
		throw error;
	}
}

/**
 * Event listener for HTTP server "listening" event.
 */

function onListening() {
	var addr = server.address();
	var bind = typeof addr === 'string'
	? 'pipe ' + addr
	: 'port ' + addr.port;
	console.log('Listening on ' + bind);
}
