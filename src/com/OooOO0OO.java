package com; /**
 * Created by qtfreet on 2017/2/24.
 */


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class OooOO0OO {
    private static final String hexString = "0123456789ABCDEF";
    public static final String DEFAULT_KEY = "qtfreet";

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String encode(String str, String key) {
        //根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        int len = bytes.length;
        int keyLen = DEFAULT_KEY.length();
        for (int i = 0; i < len; i++) {
            //对每个字节进行异或
            bytes[i] = (byte) (bytes[i] ^ DEFAULT_KEY.charAt(i % keyLen));
        }
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        //将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        String res = sb.toString();
        return res;
    }

    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String OooOOoo0oo(String str) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() / 2);
        //将每2位16进制整数组装成一个字节
        for (int i = 0; i < str.length(); i += 2)
            baos.write((hexString.indexOf(str.charAt(i)) << 4 | hexString.indexOf(str.charAt(i + 1))));
        byte[] b = baos.toByteArray();
        int len = b.length;
        int keyLen = DEFAULT_KEY.length();
        for (int i = 0; i < len; i++) {
            b[i] = (byte) (b[i] ^ DEFAULT_KEY.charAt(i % keyLen));
        }
        String res = null;
        try {
            res = new String(b, "UTF-8");  //处理class文件时需要指定编码，不然乱码
        } catch (UnsupportedEncodingException e) {
            res = "";
        }
        return res;

    }

}