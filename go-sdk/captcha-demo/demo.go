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
  check_code := req.PostFormValue(touclick.CHECK_CODE)
  check_address := req.PostFormValue(touclick.CHECK_ADDRESS)
  token := req.PostFormValue(touclick.TOKEN)
  user_name := ""
  user_id := ""
  fmt.Println("check_code:" + check_code + ", check_address:" + check_address + ", token: " + token)
  status := tc.Check(check_code,check_address,token,user_name,user_id)
  // Success, todo
  if status.Code == 0 {
     //Todo, your own business
  }
  fmt.Println("code: " + strconv.Itoa(status.Code) + ", msg: " + status.Msg)
  w.Write([]byte("code: " + strconv.Itoa(status.Code) + ", msg: " + status.Msg))
}

func main(){
  http.HandleFunc("/",indexHandler)
  http.HandleFunc("/verify",verifyHandler)
  http.ListenAndServe(":8080",nil)
}
