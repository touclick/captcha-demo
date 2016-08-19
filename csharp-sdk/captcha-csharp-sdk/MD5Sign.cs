using Touclick;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace MD5Sign
{
    public class tcu

    {
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
                {
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
            if(text.Trim().Length == 0)
            {
                return "";
            }else
            {

                return System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(text, "MD5").ToLower();
            }
        }
      
        public string get(List<Parameter> paramsList, string url)
        {
            if (null != paramsList && paramsList.Count > 0) {
                string encodedParams = this.encodeParameters(paramsList);
                if (-1 == url.IndexOf("?"))
                {
                    url += "?" + encodedParams;
                }
                else
                {
                    url += "&" + encodedParams;
                }
            }
            return url;
        }
        public string encodeParameters(List<Parameter> postParamsList)
        {
            StringBuilder buf = new StringBuilder();
            for (int j = 0; j < postParamsList.Count; j++)
            {
                if (j != 0)
                {
                    buf.Append("&");
                }
                try
                {
                    buf.Append(System.Web.HttpUtility.UrlEncode(postParamsList[j].name, System.Text.Encoding.UTF8))
                            .Append("=")
                            .Append(System.Web.HttpUtility.UrlEncode(postParamsList[j].value,
                                     System.Text.Encoding.UTF8));
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }
            }
            return buf.ToString();
        }
       
    }
}
