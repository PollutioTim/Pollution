package com.tim.pollution.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by lili on 2017/10/24 0024.
 * 文本正则工具类
 */

public class RuleStringUtils {

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）/^0?1[3|4|5|7|8][0-9]\d{8}$/
        总结起来就是第一位必定为1，第二位必定为3或5或8或7（电信运营商），其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 验证密码格式
     */
    public static boolean isPWD(String pwd) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < pwd.length(); i++) {
            if (Character.isDigit(pwd.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(pwd.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{6,20}$";
        boolean isRight = isDigit && isLetter && pwd.matches(regex);
        return isRight;
    }

    /**
     * 判断密码和确认密码是否想等
     */
    public static boolean isEqualsString(String a, String b) {
        if (a.equals(b)) {
            return true;
        }
        return false;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static boolean getNumPwd(String numPwd) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = true;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < numPwd.length(); i++) {
            if (Character.isDigit(numPwd.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(numPwd.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = false;
            }
        }
        String regex = "[0-9]{6}";
        boolean isRight = isDigit && isLetter && numPwd.matches(regex);
        Log.e("lili","isRight="+isRight);
        return isRight;
    }

    /**
     * 判断字段是否为身份证 符合返回ture
     * @param str
     * @return boolean
     */
    /**
     * 身份证正则表达式
     */
    public static final String IDCARD="((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" +
            "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" +
            "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";
    public static  boolean isIdCard(String str) {
        if(TextUtils.isEmpty(str)) return false ;
        if(str.trim().length() == 15 || str.trim().length() == 18) {
            return Regular(str,IDCARD);
        }else {
            return false ;
        }

    }
    /**
     * 匹配是否符合正则表达式pattern 匹配返回true
     * @param str 匹配的字符串
     * @param pattern 匹配模式
     * @return boolean
     */
    private static  boolean Regular(String str,String pattern){
        if(null == str || str.trim().length()<=0)
            return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }


}
