using System;
using System.Collections.Generic;
using System.Net;
using System.Text.RegularExpressions;
using Touclick;
using System.Net.Sockets;
using TouClick;
using System.Text;
using System.IO;
using LitJson;

namespace TouClickSDk
{
    [Serializable()]
    public class TouClick
    {
        private const string HTTP = "http://";
        private const string POSTFIX = ".touclick.com/sverify.touclick2";
        private const string CALLBACK_POSTFIX = ".touclick.com/callback";
        public Status check(String sid, String checkAddress, String token, String pubKey, String priKey)
        {
            return this.check(sid, checkAddress, token, pubKey, priKey, "", "");
        }
        public  Status check(String sid, String checkAddress, String token, String pubKey, String priKey, String userName, String userId)
        {

            

            if (sid == null
                    || checkAddress == null || "".Equals(checkAddress)
                    || pubKey == null || "".Equals(pubKey)
                    || priKey == null || "".Equals(priKey)
                    || token == null || "".Equals(token))
            {
                throw new TouclickException("参数有误");
            }
            string pattern = "^[_\\-0-9a-zA-Z]+$";
            Regex regex = new Regex(pattern);
            bool isMatch = regex.IsMatch(checkAddress);
            if (!isMatch)
            {             
                return new Status(Touclick.Status.CHECKADDRESS_ERROR, Touclick.Status.getCause(Touclick.Status.CHECKADDRESS_ERROR));
            }

            List<Parameter> List = new List<Parameter>();
            List.Add(new Parameter("s", sid));
            List.Add(new Parameter("i", token));
            List.Add(new Parameter("b", pubKey));
            string strIP = null;
            try
            {
                foreach (IPAddress _IPAddress in Dns.GetHostEntry(Dns.GetHostName()).AddressList)
                {
                    if (_IPAddress.AddressFamily.ToString() == "InterNetwork")
                    {
                        strIP = _IPAddress.ToString();
                    }
                }
                List.Add(new Parameter("ip", strIP));
            }
            catch (SocketException e1)
            {
                Console.Write(e1.Message);
            }
       
            List.Add(new Parameter("un", userName));
            List.Add(new Parameter("ud", userId));
            string ran = System.Guid.NewGuid().ToString();
            List.Add(new Parameter("ran", ran));
            MD5Sign.tcu t = new MD5Sign.tcu();
            string sign = t.buildMysign(List, priKey);
            if(sign.Length == 0)
            {
                return new Status(Status.SIGN_ERROR, Status.getCause(Status.SIGN_ERROR));
            }
            List.Add(new Parameter("sign", sign));
            string url = HTTP + checkAddress + POSTFIX;
            string urlstr = t.get(List, url);
            HttpWebResponse response = null;
            
            Console.Write(url);
            try
            {
                response = Request(urlstr);                
            }
            catch (TouclickException e1)
            {
                Console.Write(e1.Message);
            }
            if (response != null)
             {
                    if (HttpStatusCode.OK != response.StatusCode)
                    {                  
                        throw new TouclickException(MD5Sign.Response.getCause(int.Parse(response.StatusCode.ToString())));
                    }
                    else
                    {
                        try
                        {                      
                        string content = "";
                        using (StreamReader sr = new StreamReader(response.GetResponseStream()))
                        {
                            content = sr.ReadToEnd();
                        }
                        Result result = new Result();
                        JsonData data = JsonMapper.ToObject(content);
                        int code = (int)data["code"];
                        string signback = (string)data["sign"];
                        string message = (string)data["message"];
                        if (result.code == 0)
                            {
                                if (signback != null && !"".Equals(signback)
                                    && signback.Equals(buildSign(code, ran, priKey)))
                                {
                                    return new Status(code, message);
                                }
                                else
                                {
                                    return new Status(Status.SIGN_ERROR, Status.getCause(Status.SIGN_ERROR));
                                }
                            }

                        }catch(Exception e)
                        {
                            Console.Write("transfer json error..", e);
                        }
                    }
                return new Status(Status.STATUS_JSON_TRANS_ERROR, Status.getCause(Status.STATUS_JSON_TRANS_ERROR));
            }
                return new Status(Status.STATUS_HTTP_ERROR, Status.getCause(Status.STATUS_HTTP_ERROR)); 
        }
        /**
         * 用户名密码校验后的回调方法
         *
         * @param checkAddress  二次验证地址，二级域名
         * @param token     二次验证口令，单次有效
         * @param sid session id
         * @param isLoginSucc 用户名和密码是否校验成功
         * @throws TouclickException
         */
        public void callback(String checkAddress, String sid, String token, String pubKey, String priKey, bool isLoginSucc)
        {
        if (checkAddress == null || "".Equals(checkAddress)
                || token == null || "".Equals(token)
                || pubKey == null || "".Equals(pubKey)
                || priKey == null || "".Equals(priKey)
                || sid == null || "".Equals(sid)) {
                throw new TouclickException("参数有误");
            }
            string pattern = "^[_\\-0-9a-zA-Z]+$";
            Regex regex = new Regex(pattern);
            bool isMatch = regex.IsMatch(checkAddress);
            if (!isMatch)
            {
                return ;
            }

            List <Parameter> paramsList = new List<Parameter>();
            paramsList.Add(new Parameter("i", token));
            paramsList.Add(new Parameter("b", pubKey));
            paramsList.Add(new Parameter("s", sid));
            string strIP = null;
            try
            {
                foreach (IPAddress _IPAddress in Dns.GetHostEntry(Dns.GetHostName()).AddressList)
                {
                    if (_IPAddress.AddressFamily.ToString() == "InterNetwork")
                    {
                        strIP = _IPAddress.ToString();
                    }
                }
                paramsList.Add(new Parameter("ip", strIP));
            }
            catch (SocketException e1)
            {
                Console.Write(e1.Message);
            }
            paramsList.Add(new Parameter("su", isLoginSucc? "1" : "0"));
            string ran = System.Guid.NewGuid().ToString();
            paramsList.Add(new Parameter("ran", ran));
            MD5Sign.tcu t = new MD5Sign.tcu();
            string sign = t.buildMysign(paramsList, priKey);
            paramsList.Add(new Parameter("sign", sign));

            string url = HTTP + checkAddress + CALLBACK_POSTFIX;
            string urlstr = t.get(paramsList, url);
            HttpWebResponse response = null;
            Console.Write(url);
            try
            {
                response = Request(urlstr);                
            }
            catch (TouclickException e1)
            {
                Console.Write(e1.Message);
            }
           
           
        }

        public HttpWebResponse Request(string url)
        {
           
            StringBuilder respBody = new StringBuilder();

            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            HttpWebResponse response = null;
            try
            {               
                response = (HttpWebResponse)request.GetResponse();            
                Stream respStream = response.GetResponseStream();
            }
            catch (WebException ex)
            {
                response = (HttpWebResponse)ex.Response;
                return response;
            }
            return response;
        }

        private String buildSign(int code, String ran, String priKey)
        {
            List<Parameter> paramsList = new List<Parameter>();
            paramsList.Add(new Parameter("code", code.ToString()));
            paramsList.Add(new Parameter("timestamp", ran));
            MD5Sign.tcu tcu = new MD5Sign.tcu();
            return tcu.buildMysign(paramsList, priKey);
        }

    }
}