package com.hua.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱工具类
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 23:15
 */
public class CommonUtils {

    /**
     * 验证码字符集
     */
    private static final char[] CODES = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 检查邮箱是否合法
     * @param email 邮箱
     * @return 合法状态
     */
    public static boolean checkEmail(String email) {
        String regex =
                "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        // 正则表达式的模式 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        // 进行正则匹配
        return matcher.matches();
    }

    /**
     * 生成6位随机验证码
     * @return 验证码
     */
    public static String getRandomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(CODES[random.nextInt(CODES.length)]);
        }
        return String.valueOf(str);
    }

}
