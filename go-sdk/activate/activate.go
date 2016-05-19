package main

import (
  "net/http"
  "net/url"
  "os"
  "io/ioutil"
  "sort"
  "fmt"
  "crypto/md5"
  "encoding/hex"
)

var htmlFileName = "static/index.html"
var VERSION = "5.1.0"
var URL = "http://js.touclick.com/sdk/version/notify"

func indexHandler(w http.ResponseWriter,req *http.Request){
  fd ,err := os.Open(htmlFileName)
  defer fd.Close()
  if err != nil{
    w.Write([]byte("Load " + htmlFileName + " error."))
    return
  }

  data ,_ := ioutil.ReadAll(fd)
  w.Write(data)
}

func activateHandler(w http.ResponseWriter, req *http.Request){
  req.ParseForm()
  b := req.PostFormValue("b")
  z := req.PostFormValue("z")
  params := map[string]string{
    "b" : b,
    "v" : VERSION,
  }

  params["sign"] =  _sign(params,z)
  values := url.Values{}
  for k := range params{
    values.Set(k,params[k])
  }

  resp,err := http.PostForm(URL,values)
  if err != nil {
    w.Write([]byte("{code:300,message:激活失败，未知错误}"))
    return
  }
  defer resp.Body.Close()
  body, err := ioutil.ReadAll(resp.Body)
  if err != nil{
    fmt.Println("Read body error")
    return
  }
  w.Write([]byte(body))
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

func main(){
  http.HandleFunc("/",indexHandler)
  http.HandleFunc("/activate",activateHandler)
  http.ListenAndServe(":8088",nil)
}
