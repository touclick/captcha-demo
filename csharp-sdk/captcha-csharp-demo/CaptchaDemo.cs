using System;
using System.Collections.Generic;
using System.Web;
using TouclickSdk;
using Touclick;

namespace TouclickSdk
{
    public class CaptchaDemo : IHttpHandler
    {

        private static string PUBKEY = "";//公钥(从点触官网获取)
        private static string PRIKEY = "";//私钥(从点触官网获取)
        private static string usernamecb = "admin";
        private static string pwdcb = "admin";//DEMO演示用，一般情况可忽略
        public bool IsReusable
        {
            get
            {
                return false;
            }
        }

       
        public string buildMysign(List<Parameter> List, String key)
        {
            string prestr = createLinkString(List);  //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            string mysign = sign(prestr, key, "utf-8");
            return mysign;
        }
        public string createLinkString(List<Parameter> List)
        {

            Dictionary<string, string> map = new Dictionary<string, string>();
            for (int i = 0; i < List.Count; i++)
            {
                map.Add(List[i].name, List[i].value);
            }
            List<string> keysList = new List<string>(map.Keys);
            keysList.Sort();
            string prestr = "";
            for (int i = 0; i < keysList.Count; i++)
            {
                string key = keysList[i];
                string value = map[key];
                if (i == keysList.Count - 1)
                {//拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                }
                else
                {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
            return prestr;
        }
        public string sign(String text, String key, String input_charset)
        {
            text = text + key;
            if (text.Trim().Length == 0)
            {
                return "";
            }
            else
            {
                return System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(text, "MD5").ToLower();
            }
        }

        public void ProcessRequest(HttpContext context)
        {
            string username = context.Request.Form["username"];
            string pwd = context.Request.Form["password"];
            string checkAddress = context.Request.Form["checkAddress"];
            string token = context.Request.Form["token"];
            //一次验证传递的参数,同一次验证一样
            string sid = context.Request.Form["sid"];
            TouClickSDk.TouClick t = new TouClickSDk.TouClick();
            Touclick.Status status = t.check(sid, checkAddress, token, PUBKEY, PRIKEY, username, pwd);          
            Console.Write("checkAddress :" + checkAddress + ",token:" + token + ",sid:" + sid);
            Console.Write("code :" + status.Code + ",message:" + status.Message);
          
            if (status != null && status.Code == 0)
            {
                var isLoginSucc = false;
                //执行自己的程序逻辑
                if(username == usernamecb && pwd == pwdcb)
                {
                    isLoginSucc = true;
                }
                t.callback(checkAddress, sid, token, PUBKEY, PRIKEY, isLoginSucc);
                context.Response.Write(status.Message);
            }            
        }
    }
}
