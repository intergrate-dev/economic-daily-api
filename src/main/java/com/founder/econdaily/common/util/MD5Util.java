package com.founder.econdaily.common.util;

/**
 * Created by yangyibo on 17/2/7.
 */
import java.security.MessageDigest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5加密工具
 *
 */
public class MD5Util {

    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    private static final String SALT = "tamboo";

    public static String encode(String password) {
        password = password + SALT;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args) {
        // System.out.println(MD5Util.encode("abel"));
        String ss = "[{\"mapping\":[\"21.313854%,99.676375%\",\"74.473114%,99.676375%\",\"74.473114%,82.200647%\",\"21.313854%,82.200647%\"],\"articleID\":\"134345\"},{\"mapping\":[\"21.313854%,80.744337%\",\"74.473114%,80.744337%\",\"74.473114%,55.339806%\",\"21.313854%,55.339806%\"],\"articleID\":\"134346\"},{\"mapping\":[\"0.501502%,72.653722%\",\"19.057093%,72.653722%\",\"19.057093%,55.339806%\",\"0.501502%,55.339806%\"],\"articleID\":\"134347\"},{\"mapping\":[\"23.821366%,53.074434%\",\"1.504507%,53.074434%\",\"1.504507%,27.346278%\",\"1.504507%,19.902913%\",\"74.473114%,19.902913%\",\"74.473114%,27.669903%\",\"23.821366%,27.669903%\"],\"articleID\":\"134348\"},{\"mapping\":[\"76.980626%,69.902913%\",\"99.548237%,69.902913%\",\"99.548237%,19.255663%\",\"76.980626%,19.255663%\"],\"articleID\":\"134349\"},{\"mapping\":[\"76.980626%,80.420712%\",\"99.548237%,80.420712%\",\"99.548237%,70.711974%\",\"76.980626%,70.711974%\"],\"articleID\":\"134350\"},{\"mapping\":[\"25.075123%,49.838188%\",\"74.473114%,49.838188%\",\"74.473114%,27.346278%\",\"25.075123%,27.346278%\"],\"articleID\":\"134351\"},{\"mapping\":[\"66.198324%,17.637540%\",\"99.548237%,17.637540%\",\"99.548237%,11.812298%\",\"66.198324%,11.812298%\"],\"articleID\":\"134352\"}]";
        // JSONArray parseArray = JSON.parseArray(ss);
        // logger.info("parseArray", parseArray.toString());
        logger.info("parseArray", ss);


    }
}