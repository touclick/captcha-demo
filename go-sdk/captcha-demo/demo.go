package main

import (
  "net/http"
  "os"
  "io/ioutil"
  "touclick"
  "strconv"
  "fmt"
)

var htmlFileName = "static/login.html"
var URL = "http://js.touclick.com/sdk/version/notify"
var pubKey = "你的公钥（官网申请）"
var priKey = "你的私钥（官网申请）"
var tc = touclick.NewTouclick(pubKey,priKey)

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

func verifyHandler(w http.ResponseWriter, req *http.Request){
  req.ParseForm()
  sid := req.PostFormValue(touclick.SID)
  check_address := req.PostFormValue(touclick.CHECK_ADDRESS)
  token := req.PostFormValue(touclick.TOKEN)
  user_name := ""
  user_id := ""
  fmt.Println("sid:" + sid + ", check_address:" + check_address + ", token: " + token)
  status := tc.Check(check_address,sid,token,user_name,user_id)
  // Success, todo
  if status.Code == 0 {
     //Todo, your own business,check you username and password etc.

     // if username and password check successfully,you can call Callback
     tc.Callback(check_address,sid,token,true)
  }
  fmt.Println("code: " + strconv.Itoa(status.Code) + ", msg: " + status.Msg)
  w.Write([]byte("code: " + strconv.Itoa(status.Code) + ", msg: " + status.Msg))
}

func main(){
  http.HandleFunc("/",indexHandler)
  http.HandleFunc("/postdata",verifyHandler)
  http.ListenAndServe(":8080",nil)
}
