
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Text;
using System.Web;

namespace TouclickSdk
{
    public class CaptchaActive : IHttpHandler
    {
        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
        public void ProcessRequest(HttpContext context)
        {

            string VERSION = "5.2.0";//不可修改
            string URL = "http://js.touclick.com/sdk/version/notify";
            var req = context.Request;
            var res = context.Response;
            string b = req["b"];
            string z = req["z"];
            List<Parameter> paramsList = new List<Parameter>();
            paramsList.Add(new Parameter("b", b));
            paramsList.Add(new Parameter("v", VERSION));
            string sign = buildMysign(paramsList, z);
            paramsList.Add(new Parameter("sign", sign));
            string result = sendGet(URL, paramsList);
            res.Write(result);
            Console.Write(result);
        }

        public string Request(string url)
        {
            Stopwatch timer = new Stopwatch();
            StringBuilder respBody = new StringBuilder();
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            try
            {

                timer.Start();
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                byte[] buf = new byte[8192];
                Stream respStream = response.GetResponseStream();
                int count = 0;
                do
                {
                    count = respStream.Read(buf, 0, buf.Length);
                    if (count != 0)
                        respBody.Append(Encoding.UTF8.GetString(buf, 0, count));
                }
                while (count > 0);
                timer.Stop();

                string responseBody = respBody.ToString();
                int statusCode = (int)(HttpStatusCode)response.StatusCode;
                double responseTime = timer.ElapsedMilliseconds / 1000.0;
            }
            catch (WebException ex)
            {
                HttpWebResponse response = (HttpWebResponse)ex.Response;
                string responseBody = "No Server Response";
                
                double responseTime = 0.0;
                return respBody.Append(responseBody).Append(responseTime).ToString();
            }
            return respBody.ToString();
        }

        private string sendGet(string url, List<Parameter> paramsList)
    {
        string result = "";
       
        try
        {
            string urlNameString = url + "?";
            string preParams = "";
            for (int i = 0; i < paramsList.Count; i++)
            {
                preParams += "&" + paramsList[i].name + "=" + paramsList[i].value;
            }
            urlNameString += preParams.Substring(1);
            result += Request(urlNameString);          
            }
        catch (Exception e)
        {
            Console.Write(e.Message);
        }
      
        return result;
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
        public class Parameter
        {
            public string b { get; set; }
            public string v { get; set; }


            public Parameter(string name, string value)
            {
                this.name = name;
                this.value = value;
            }
            public string name { get; set; }
            public string value { get; set; }
        }
}
}