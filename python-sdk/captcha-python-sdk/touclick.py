#!coding:utf8
import sys
import random
import json
import requests
import re
import string
from hashlib import md5


if sys.version_info >= (3,):
    xrange = range

VERSION = "1.1.0"

class TouclickLib(object):

    CHECK_CODE = "checkCode"
    CHECK_ADDRESS = "checkAddress"
    TOKEN = "token"

    STATUS = {
        "STATUS_OK": (0, ""),
        "STATUS_TOKEN_EXPIRED": (1, "该验证已过期"),
        "STATUS_NO_PUBKEY_ERROR": (2, "公钥不可为空"),
        "STATUS_TOKEN_ERROR": (3, "一次验证返回的token为必需参数,不可为空"),
        "STATUS_PUBKEY_ERROR": (4, "公钥不正确"),
        "CHECKCODE_ERROR": (5, "CheckCode有误,请确认CheckCode是否和一次验证传递一致"),
        "STATUS_PARAM_ERROR": (6, "sign加密错误,请检查参数是否正确"),
        "STATUS_VERIFY_ERROR": (7, "一次验证错误"),
        "STATUS_SERVER_ERROR": (8, "点触服务器异常"),
        "STATUS_HTTP_ERROR": (9, "http请求异常"),
        "STATUS_JSON_TRANS_ERROR": (10, "json转换异常,可能是请求地址有误,请检查请求地址(http://[checkAddress].touclick.com/sverify.touclick?参数)"),
        "STATUS_CHECKADDRESS_ERROR": (11, "二次验证地址不合法"),
        "STATUS_SIGN_ERROR": (12, "签名校验失败,数据可能被篡改")
    }

    HTTP = "http://"
    POSTFIX = ".touclick.com/sverify.touclick"

    ADDR_PATTERN = re.compile(r'^[_\-0-9a-zA-Z]+$')
    RAN_LETTERS = string.digits + string.ascii_uppercase + string.ascii_lowercase

    def __init__(self, pub_key, pri_key):
        assert pub_key != None and pub_key != ""
        assert pri_key != None and pri_key != ""
        self.pub_key = pub_key
        self.pri_key = pri_key

    def check(self, check_code, check_address, token, user_name="", user_id=""):
        """二次验证
        Args:
            token: 二次验证口令，单次有效
            check_address: 二次验证地址，二级域名
            checkCode: 校验码，开发者自定义，一般采用手机号或者用户ID，用来更细致的频次控制

        Returns:
            表示验证结果的元组
            例如：
                (0, "")
                (3, "一次验证返回的token为必需参数,不可为空")
        """

        if check_address == None or self.ADDR_PATTERN.match(check_address) == None:
            return self.STATUS["STATUS_CHECKADDRESS_ERROR"]

        ran = self._ran_string(32)
        params = {"ckcode": check_code, "i": token, "b": self.pub_key,
                    "un": user_name, "ud": user_id, "ip": "", "ran": ran}
        params["sign"] = self._sign(params, self.pri_key)
        url = self.HTTP + check_address + self.POSTFIX
        try:
            response = requests.get(url, params=params, timeout=30)
            if response.status_code == requests.codes.ok:
                try:
                    result = json.loads(response.text)
                    result_params = {"code": result["code"], "timestamp": ran}
                    if (result["code"] == 0) \
                        and ("sign" not in result \
                            or result["sign"] != self._sign(result_params, self.pri_key)):
                        return self.STATUS["STATUS_SIGN_ERROR"]
                    return (result["code"], result["message"])
                except:
                    return self.STATUS["STATUS_JSON_TRANS_ERROR"]
            else:
                return self.STATUS["STATUS_HTTP_ERROR"]
        except:
            return self.STATUS["STATUS_HTTP_ERROR"]

    def _sign(self, params, key):
        sorted_params = sorted(params.items(), key = lambda d: d[0])
        text = "&".join([k+"="+str(v) for k,v in sorted_params])
        text = text + key
        m = md5(text.encode("utf-8"))
        return m.hexdigest()

    def _ran_string(self, n):
        return "".join(random.choice(self.RAN_LETTERS) for _ in range(n))
