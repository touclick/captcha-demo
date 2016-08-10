/**
* @FileName: activate.js
* @Description: 激活
* @author delete
* @date 2016年5月18日 下午1:36:01
* @version 1.0
 */
var finalhandler = require('finalhandler'), http = require('http'), Router = require('router'), fs = require('fs'),querystring = require('querystring'), request = require('request');
var touclickSdk = require('touclick-nodejs-sdk');
var VERSION = "5.2.0",URL = "http://js.touclick.com/sdk/version/notify?"

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
router.get('/index.html', renderFile("index.html"));

router.post('/activate',function(req, res){
	req.setEncoding('utf-8');
    var postData = "";
    req.addListener("data", function (postDataChunk) {
        postData += postDataChunk;
    });
    req.addListener("end", function () {
        var params = querystring.parse(postData);
        console.log(JSON.stringify(params));
        var z = params["z"];
        var ret = {"b":params["b"],"v":VERSION};//b、v 的顺序不能颠倒
        console.log("z:"+z);
        console.log(ret);
        ret["sign"] = touclickSdk.md5(querystring.stringify(ret) + z);
        console.log(ret);
        console.log(URL + querystring.stringify(ret))
        request({
        	method: "GET",
        	uri: URL + querystring.stringify(ret),
        	timeout: 5000
        },function (error, response, body) {
			if (!error && response.statusCode == 200) {
				res.end(body);
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
