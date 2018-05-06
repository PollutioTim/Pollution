package com.tim.pollution.utils;

import java.security.MessageDigest;

/**
 * Created by lenovo on 2018/4/28.
 */

public final class UtilMD5 {
    private static final String[] h = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private UtilMD5() {
    }

    public static String MD5Encode(String origin, String charsetname) {
        String r = null;

        try {
            r = new String(origin);
            MessageDigest m = MessageDigest.getInstance("MD5");
            if(charsetname != null && !"".equals(charsetname)) {
                r = byteArrayToHexString(m.digest(r.getBytes(charsetname)));
            } else {
                r = byteArrayToHexString(m.digest(r.getBytes()));
            }
        } catch (Exception var4) {
            ;
        }

        return r;
    }

    public static String MD5EncodeCount(String origin, String charsetname, int count) {
        String r = origin;

        for(int i = 0; i < count; ++i) {
            r = MD5Encode(r, charsetname);
        }

        return r;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer r = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            r.append(byteToHexString(b[i]));
        }

        return r.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if(b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return h[d1] + h[d2];
    }

}
