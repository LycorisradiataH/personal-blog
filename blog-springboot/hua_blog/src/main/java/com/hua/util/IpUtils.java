package com.hua.util;

import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * ip工具类
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 22:24
 */
@SuppressWarnings("all")
public class IpUtils {

    /**
     * 获取用户ip地址
     * @param request 请求
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddr = null;
        try {
            ipAddr = request.getHeader("x-forwarded-for");
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddr)) {
                    // 根据网卡取本机配置的ip
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddr = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个ip为客户端真实ip，多个ip按照 ',' 逗号分割
            if (ipAddr != null && ipAddr.length() > 15) {
                // == 15
                if (ipAddr.indexOf(",") > 0) {
                    ipAddr = ipAddr.substring(0, ipAddr.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddr = "";
        }
        return ipAddr;
    }

    /**
     * 解析ip地址
     * @param ipAddr ip地址
     * @return 解析后的ip地址
     */
    public static String getIpSource(String ipAddr) {
        try {
            URL url = new URL("http://opendata.baidu.com/api.php?query="
                    + ipAddr + "&co=&resource_id=6006&oe=utf8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                            url.openConnection().getInputStream(), "utf-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            Map map = JSON.parseObject(result.toString(), Map.class);
            List<Map<String, String>> data = (List<Map<String, String>>) map.get("data");
            return data.get(0).get("location");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取访问设备
     *
     * @param request 请求
     * @return {@link UserAgent} 访问设备
     */
    public static UserAgent getUserAgent(HttpServletRequest request){
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

}
