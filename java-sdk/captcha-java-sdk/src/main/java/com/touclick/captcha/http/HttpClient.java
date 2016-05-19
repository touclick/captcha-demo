package com.touclick.captcha.http;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import com.touclick.captcha.exception.TouclickException;
import com.touclick.captcha.model.Parameter;

public class HttpClient implements java.io.Serializable {

    private static final long serialVersionUID = -176092625883595547L;
    private static final int OK = 200;// OK: Success!
    private static final int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    private static final int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    private static final int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    private static final int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    private static final int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    private static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Touclick team can investigate.
    private static final int BAD_GATEWAY = 502;// Bad Gateway: Touclick is down or being upgraded.
    private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Touclick servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.

    static Logger log = Logger.getLogger(HttpClient.class.getName());
    org.apache.commons.httpclient.HttpClient client = null;

    private MultiThreadedHttpConnectionManager connectionManager;
    private final static boolean DEBUG = false;

    public HttpClient() {
        this(150, 30000, 30000, 1024 * 1024);
    }

    public HttpClient(int maxConPerHost, int conTimeOutMs, int soTimeOutMs,
                      int maxSize) {
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connectionManager.getParams();
        params.setDefaultMaxConnectionsPerHost(maxConPerHost);
        params.setConnectionTimeout(conTimeOutMs);
        params.setSoTimeout(soTimeOutMs);

        HttpClientParams clientParams = new HttpClientParams();
        // 忽略cookie 避免 Cookie rejected 警告
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        client = new org.apache.commons.httpclient.HttpClient(clientParams,
                connectionManager);
    }

    public Response get(String url, List<Parameter> params) throws TouclickException {
        if (null != params && params.size() > 0) {
            String encodedParams = HttpClient.encodeParameters(params);
            if (-1 == url.indexOf("?")) {
                url += "?" + encodedParams;
            } else {
                url += "&" + encodedParams;
            }
        }
        GetMethod getmethod = new GetMethod(url);
        return httpRequest(getmethod);
    }

    public Response httpRequest(HttpMethod method) throws TouclickException {
        int responseCode = -1;
        try {
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            client.executeMethod(method);
            Header[] resHeader = method.getResponseHeaders();
            responseCode = method.getStatusCode();
            log("Response:");
            log("http StatusCode:" + String.valueOf(responseCode));

            for (Header header : resHeader) {
                log(header.getName() + ":" + header.getValue());
            }
            Response response = new Response();
            response.setInfo(method.getResponseBodyAsString());
            response.setStatus(responseCode);
            if (responseCode != OK) {
                throw new TouclickException(getCause(responseCode));
            }
            return response;

        } catch (IOException ioe) {
            throw new TouclickException(ioe.getMessage(), ioe);
        } finally {
            method.releaseConnection();
        }

    }

    /*
     * 对parameters进行encode处理
     */
    public static String encodeParameters(List<Parameter> postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.size(); j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(postParams.get(j).getName(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(postParams.get(j).getValue(),
                                "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();
    }

    private static String getCause(int statusCode) {
        String cause = null;
        switch (statusCode) {
            case NOT_MODIFIED:
                break;
            case BAD_REQUEST:
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            case NOT_AUTHORIZED:
                cause = "Authentication credentials were missing or incorrect.";
                break;
            case FORBIDDEN:
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            case NOT_FOUND:
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            case NOT_ACCEPTABLE:
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            case INTERNAL_SERVER_ERROR:
                cause = "Something is broken.  Please post to the group so the Touclick team can investigate.";
                break;
            case BAD_GATEWAY:
                cause = "Touclick is down or being upgraded.";
                break;
            case SERVICE_UNAVAILABLE:
                cause = "Service Unavailable: The Touclick servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            default:
                cause = "";
        }
        return statusCode + ":" + cause;
    }


    /**
     * log调试
     */
    private static void log(String message) {
        if (DEBUG) {
            log.debug(message);
        }
    }

}
