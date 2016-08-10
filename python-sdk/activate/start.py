# coding:utf-8
import tornado.ioloop
import tornado.web
import tornado.gen
import requests

from hashlib import md5

VERSION = "5.2.0"
URL = "http://js.touclick.com/sdk/version/notify"

class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.render("static/index.html",)


class ActivateHandler(tornado.web.RequestHandler):
    def post(self):
        b = self.get_argument("b", "")
        z = self.get_argument("z", "")
        params = {"b": b, "v": VERSION}
        params["sign"] = self._sign(params, z)
        try:
            resp = requests.get(URL, params=params, timeout=30)
            if resp.status_code == requests.codes.ok:
                self.write(resp.text)
            else:
                self.send_error(resp.status_code)
        except:
            self.send_error()

    def _sign(self, params, key):
        sorted_params = sorted(params.items(), key = lambda d: d[0])
        text = "&".join([k+"="+str(v) for k,v in sorted_params])
        text = text + key
        m = md5(text.encode("utf-8"))
        return m.hexdigest()


if __name__ == "__main__":
    app = tornado.web.Application([
                                      (r"/", MainHandler),
                                      (r"/activate", ActivateHandler)
                                  ], debug=True)
    app.listen(8088)
    tornado.ioloop.IOLoop.instance().start()
