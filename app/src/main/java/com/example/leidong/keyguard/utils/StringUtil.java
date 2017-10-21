package com.example.leidong.keyguard.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by leidong on 2017/10/15
 */

public class StringUtil {
    /**
     * 得到带掩码的邮箱地址
     * @param email
     * @return
     */
    public static String getMaskedEmail(String email) {
        if (isNullOrEmpty(email))
            return "";
        return email.replaceAll("(?<=.).(?=[^@]*?.@)", "*");
    }

    /**
     * 得到带掩码的电话号码
     * @param phone
     * @return
     */
    public static String getMaskedPhoneNumber(String phone) {
        if (isNullOrEmpty(phone)) {
            return "";
        }
        String ret;
        if (phone.length() < 4) {
            return phone.substring(0, 1) + getStars(phone.substring(1));
        }
        ret = phone.substring(phone.length() - 4, phone.length());
        ret = getStars(phone.substring(0, phone.length() - 4)) + ret;
        return ret;
    }

    /**
     * 使用*替换所有字符
     * @param src
     * @return
     */
    private static String getStars(String src) {
        if (isNullOrEmpty(src))
            return "";
        return src.replaceAll("\\S", "*");
    }

    /**
     *
     * @param src
     * @return
     */
    public static String getStarsIgnoreWhite(String src) {
        if (isNullOrEmpty(src))
            return "";
        return src.replaceAll("\\s", "*");
    }

    /**
     * 字符串为空判断
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str);
    }

    /**
     * 字符串数组为空判断
     * @param strs
     * @return
     */
    public static boolean isNullOrEmpty(String ... strs) {
        for (String s : strs) {
            if (!isNullOrEmpty(s))
                return false;
        }
        return true;
    }

    private static HashMap<String, ArrayList> charMap = null;

    private static HashMap getCharMap() {
        if (charMap != null) {
            return charMap;
        }
        charMap = new HashMap<>();
        ArrayList add = new ArrayList();
        add.add("@");
        add.add("4");
        charMap.put("A",add);
        add = new ArrayList();
        add.add("8");
        charMap.put("B", add);

        add = new ArrayList();
        add.add("3");
        charMap.put("E", add);

        add = new ArrayList();
        add.add("6");
        charMap.put("G", add);

        add = new ArrayList();
        add.add("#");
        charMap.put("H", add);

        add = new ArrayList();
        add.add("!");
        add.add("1");
        charMap.put("I", add);

        add = new ArrayList();
        add.add("0");
        charMap.put("O", add);

        add = new ArrayList();
        add.add("9");
        charMap.put("Q", add);

        add = new ArrayList();
        add.add("5");
        add.add("$");
        charMap.put("S", add);

        add = new ArrayList();
        add.add("7");
        charMap.put("T", add);

        add = new ArrayList();
        add.add("\\/");
        charMap.put("V", add);

        add = new ArrayList();
        add.add("2");
        charMap.put("Z", add);

        return charMap;
    }

    /**
     *
     * @param word
     * @return
     */
    public static String getMaskedWord(String word) {
        word = word.toUpperCase();
        char[] chars = word.toCharArray();
        StringBuilder ret = new StringBuilder();
        HashMap<String, ArrayList> map = getCharMap();
        for (char c : chars) {
            if (getRandBool()) {
                if (getRandBool())
                    c = Character.toLowerCase(c);
                ret.append(c);
                continue;
            }
            ArrayList<String> masks = map.get(String.valueOf(c));
            if (masks == null || masks.size() == 0){
                if (getRandBool())
                    c = Character.toLowerCase(c);
                ret.append(c);
            } else {
                ret.append(masks.get(getRandInt(masks.size())));
            }
        }
        return ret.toString();
    }

    /**
     * 随即产生true或者false
     * @return
     */
    public static boolean getRandBool() {
        SecureRandom random = new SecureRandom();
        return random.nextBoolean();
    }

    /**
     * 随机产生整形
     * @param range
     * @return
     */
    public static int getRandInt(int range) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(range);
    }

    /**
     * 处理时间格式
     * @param timeStamp
     * @return
     */
    public static String timeStampToTime(String timeStamp) {
        if (isNullOrEmpty(timeStamp)) {
            return "--:--:--";
        }
        Long l = Long.valueOf(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(l));
    }
}
