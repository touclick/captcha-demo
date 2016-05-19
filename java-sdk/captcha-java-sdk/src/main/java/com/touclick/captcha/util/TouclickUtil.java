package com.touclick.captcha.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.touclick.captcha.model.Parameter;

/**
 * @author zhanwei
 * @ClassName: TouclickUtil
 * @Description: 生成sign
 * @date 2016年3月10日 下午1:30:01
 */
public class TouclickUtil {

    /**
     * 功能：生成签名结果
     *
     * @param params 要签名的数组
     * @param key    安全校验码
     * @return 签名结果字符串
     */
    public static String buildMysign(List<Parameter> params, String key) {
        String prestr = createLinkString(params);  //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = MD5.sign(prestr, key, "utf-8");
        return mysign;
    }

    /**
     * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(List<Parameter> params) {
        Map<String, String> map = new HashMap<String, String>();
        for (Parameter p : params) {
            map.put(p.getName(), p.getValue());
        }
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
}
