package com.srmn.xwork.androidlib.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by kiler on 2015/10/7.
 */
public class StringUtil {


    public static boolean isNullOrEmpty(String value) {
        if (value == null)
            return true;
        if (value.equals(""))
            return true;
        if (value.length() == 0)
            return true;
        return false;
    }

    public static String formatDistance(double value) {
        if (value < 1000)
            return String.format("%3.1f", value) + " m";
        else
            return String.format("%3.3f", value / 1000) + " km";
    }

    public static String formatTime(int value) {
        int hours = value / 3600;
        int minute = (value % 3600) / 60;
        int second = ((value % 3600) % 60);
        return String.format("%d", hours) + ":" + String.format("%02d", minute) + "'" + String.format("%02d", second) + "''";
    }


    public static String decodeBase64(String decodeString) {
        if (StringUtil.isNullOrEmpty(decodeString))
            return "";
        return new String(Base64.decode(decodeString, Base64.DEFAULT));
    }


    public static String encodeBase64(String encodeString) {
        if (StringUtil.isNullOrEmpty(encodeString))
            return "";
        return new String(Base64.encode(encodeString.getBytes(), Base64.DEFAULT));
    }


    private static final int BUFFER_SIZE = 1024;
    public static final String ENCODE_UTF_8 = "UTF-8";

    public static String encryptBASE64(String encodeString) {
        if (StringUtil.isNullOrEmpty(encodeString)) {
            return "";
        }
        try {
            byte[] encode = encodeString.getBytes(ENCODE_UTF_8);
            // base64 加密
            return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), ENCODE_UTF_8);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String decryptBASE64(String decodeString) {
        if (StringUtil.isNullOrEmpty(decodeString)) {
            return "";
        }
        try {
            byte[] encode = decodeString.getBytes(ENCODE_UTF_8);
            // base64 解密
            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), ENCODE_UTF_8);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String encryptGZIP(String encodeString) {
        if (StringUtil.isNullOrEmpty(encodeString)) {
            return "";
        }

        try {
            // gzip压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(baos);
            gzip.write(encodeString.getBytes(ENCODE_UTF_8));

            gzip.close();

            byte[] encode = baos.toByteArray();

            baos.flush();
            baos.close();

            // base64 加密
            return new String(Base64.encode(encode, Base64.DEFAULT));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String decryptGZIP(String decodeString) {
        if (StringUtil.isNullOrEmpty(decodeString)) {
            return null;
        }

        try {

            byte[] decode = Base64.decode(decodeString, Base64.DEFAULT);

            //gzip 解压缩
            ByteArrayInputStream bais = new ByteArrayInputStream(decode);
            GZIPInputStream gzip = new GZIPInputStream(bais);

            byte[] buf = new byte[BUFFER_SIZE];
            int len = 0;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ((len = gzip.read(buf, 0, BUFFER_SIZE)) != -1) {
                baos.write(buf, 0, len);
            }
            gzip.close();
            baos.flush();

            decode = baos.toByteArray();

            baos.close();

            return new String(decode, ENCODE_UTF_8);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 十六进制字符串 转换为 byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
        // return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * byte[] 转换为 十六进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");

        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
