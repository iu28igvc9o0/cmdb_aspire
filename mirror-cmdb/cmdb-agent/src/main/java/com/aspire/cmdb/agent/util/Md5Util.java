package com.aspire.cmdb.agent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: Md5Util
 * <p/>
 *
 * 类功能描述: MD5加密工具
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
public class Md5Util {
    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static MessageDigest messagedigest = initMessageDigest();

    private static MessageDigest initMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("The MD5 algorithm is not supported by the MessageDigest.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        return getFileMD5String(file, 0, -1);
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @param start
     * @param length
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file, long start, long length) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); FileChannel ch = fis.getChannel()) {
	        long remain = file.length() - start;
	
	        if (length < 0 || length > remain) {
	            length = remain;
	        }
	
	        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, start, length);
	        messagedigest.update(byteBuffer);
	        return bufferToHex(messagedigest.digest());
        }
    }

    /**
     * 获取字符串的MD5加密的结果
     *
     * @param s
     * @return
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static void main(String[] args) {
        System.out.println(getMD5String("dw_ligongbao"));
        System.out.println(getMD5String("dw_ligongbao"));
        System.out.println(getMD5String("dw_ligongbao"));
    }
    
    public static String generateCheckCode(String source) {
		// logger.info("MD5 source: " + source);
		String result = null;
		// Used to convert 16-byte hexadecimal characters.
		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			//java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			messagedigest.update(source.getBytes("UTF-8"));
			// MD5 calculation is a 128-bit long integer, that is with 16-byte byte.
			byte[] tmp = messagedigest.digest();
			// Each byte expressed in hexadecimal using 2 characters,so that 32 bytes as
			// hexadecimal.
			char[] str = new char[16 * 2];
			int k = 0; // The index of character in convert result.
			// Convert each byte to hexadecimal of MD5.
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			// Convert the result from byte to string.
			result = new String(str);
		} catch (Exception e) {
			log.error("SlpMD5Util:generateCheckCode", e);
		}
		return result;
	}
}
