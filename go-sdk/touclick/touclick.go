package touclick

import (
    "net/http"
    "io/ioutil"
    "io"
    "crypto/md5"
    "crypto/rand"
    "encoding/base64"
    "encoding/hex"
    "sort"
    "encoding/json"
    "regexp"
    "strconv"
    "fmt"
)

const(
    HTTP = "http://"
    POSTFIX = ".touclick.com/sverify.touclick"
    CHECK_CODE = "checkCode"
    CHECK_ADDRESS = "checkAddress"
    TOKEN = "token"
)

type Touclick struct{
    pubKey string
    priKey string
}

type Status struct{
    Code int
    Msg string
}

type result struct {
    Code int  `json:"code"`
    Message string `json:"message"`
    Sign string `json:"sign"`
}

var StatusMap = make(map[string]*Status)
var checkAddressPattern *regexp.Regexp
func init(){
    StatusMap["STATUS_OK"] = &Status{Code : 0, Msg : ""}
    StatusMap["STATUS_TOKEN_EXPIRED"] = &Status{Code : 1, Msg : "该验证已过期"}
    StatusMap["STATUS_NO_PUBKEY_ERROR"] = &Status{Code : 2, Msg : "公钥不可为空"}
    StatusMap["STATUS_TOKEN_ERROR"] = &Status{Code : 3, Msg : "一次验证返回的token为必需参数,不可为空"}
    StatusMap["STATUS_PUBKEY_ERROR"] = &Status{Code : 4, Msg : "公钥不正确"}
    StatusMap["CHECKCODE_ERROR"] = &Status{Code : 5, Msg : "CheckCode有误,请确认CheckCode是否和一次验证传递一致"}
    StatusMap["STATUS_PARAM_ERROR"] = &Status{Code : 6, Msg : "sign加密错误,请检查参数是否正确"}
    StatusMap["STATUS_VERIFY_ERROR"] = &Status{Code : 7, Msg : "一次验证错误"}
    StatusMap["STATUS_SERVER_ERROR"] = &Status{Code : 8, Msg : "点触服务器异常"}
    StatusMap["STATUS_HTTP_ERROR"] = &Status{Code : 9, Msg : "http请求异常"}
    StatusMap["STATUS_JSON_TRANS_ERROR"] = &Status{Code : 10, Msg : "json转换异常,可能是请求地址有误,请检查请求地址(http://[checkAddress].touclick.com/sverify.touclick?参数)"}
    StatusMap["STATUS_CHECKADDRESS_ERROR"] = &Status{Code : 11, Msg : "二次验证地址不合法"}
    StatusMap["SIGN_ERROR"] = &Status{Code : 12, Msg : "签名校验失败,数据可能被篡改"}
    checkAddressPattern,_ = regexp.Compile("^[_\\-0-9a-zA-Z]+$")
}

func NewTouclick(pubKey,priKey string) *Touclick{
    t := &Touclick{
        pubKey : pubKey,
        priKey : priKey,
    }
    return t
}

/**
 * 二次验证
 * @Param check_code 一次验证返回的check code
 * @Param check_address 一次验证返回的check address
 * @Param token 一次验证返回的token
 * @Param user_name 用户名，可选
 * @Param user_id 用户ID，可选
 * @Return 返回Status
 */
func (t *Touclick) Check(check_code,check_address,token,user_name,user_id string) *Status {

    if !checkAddressPattern.MatchString(check_address){
        return StatusMap["STATUS_CHECKADDRESS_ERROR"]
    }

    params := map[string]string{
        "ckcode" : check_code,
        "i" : token,
        "b" : t.pubKey,
        "un" : user_name,
        "ud" : user_id,
        "ip" : "",
        "ran" : getRandString(),
    }

    params["sign"] = _sign(params,t.priKey)
    url := HTTP + check_address + POSTFIX
    url = url + "?" + makeUrlParam(params)

    resp, err := http.Get(url)
    if err != nil {
        return StatusMap["STATUS_HTTP_ERROR"]
    }
    defer resp.Body.Close()
    body, err := ioutil.ReadAll(resp.Body)
    if err != nil{
        return StatusMap["STATUS_HTTP_ERROR"]
    }
    var r result
    if err := json.Unmarshal(body,&r); err != nil{
        return StatusMap["STATUS_JSON_TRANS_ERROR"]
    }
    if r.Code == 0{
        if l := len(r.Sign); l != 0{
            retParams := map[string]string{
                "code" : strconv.Itoa(r.Code),
                "timestamp" : params["ran"],
            }
            sign := _sign(retParams,t.priKey)
            if sign == r.Sign{
                return &Status{Code : r.Code,Msg : r.Message}
            }
            return StatusMap["SIGN_ERROR"]
        }
    }
    return &Status{Code: r.Code, Msg : r.Message}
}

func makeUrlParam(params map[string]string) string{
    p := ""
    for k := range params {
        p = p + k + "=" + params[k] + "&"
    }
    return p
}

func _sign(p map[string]string,z string) string{
    var keys []string
    for k := range p {
        keys = append(keys,k)
    }
    sort.Strings(keys)
    var t string
    for i,k := range keys{
        if i == 0{
            t = k + "=" + p[k]
        }else{
            t = t + "&" + k + "=" + p[k]
        }
    }
    t = t + z
    return md5Sum(t)
}

func md5Sum(t string) string{
    h := md5.New()
    h.Write([]byte(t))
    ret := h.Sum(nil)
    return hex.EncodeToString(ret)
}

func getRandString() string{
    b := make([]byte,48)
    if _,err := io.ReadFull(rand.Reader,b); err != nil{
        return ""
    }
    return md5Sum(base64.URLEncoding.EncodeToString(b))
}
