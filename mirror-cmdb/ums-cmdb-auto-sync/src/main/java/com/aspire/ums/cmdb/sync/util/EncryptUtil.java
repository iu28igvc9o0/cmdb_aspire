package com.aspire.ums.cmdb.sync.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;   
import javax.crypto.SecretKey;   
import javax.crypto.SecretKeyFactory;   
import javax.crypto.spec.DESKeySpec;   
import javax.crypto.spec.IvParameterSpec; 

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密算法。
 * <br/>1.MD5 
 * <br/>2.SHA-256
 * <br/>3.对称加解密算法。
 */
public class EncryptUtil {
    private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

	/**
	 * 使用MD5加密
	 * @param inStr
	 * @return
	 * @throws Exception
	 */
	public static String encryptMd5(String inStr) throws Exception {

		MessageDigest md = null;
		String out = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(inStr.getBytes());
			return new String( Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}

	}



    /**
     * 使用MD5加密
     * @param source
     * @return
     * @throws Exception
     */
    public static String md5(String source) {
        // logger.info("MD5 source: " + source);
        String result = null;
        // Used to convert 16-byte hexadecimal characters.
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source.getBytes("UTF-8"));
            // MD5 calculation is a 128-bit long integer, that is with 16-byte byte.
            byte tmp[] = md.digest();
            // Each byte expressed in hexadecimal using 2 characters,so that 32 bytes as
            // hexadecimal.
            char str[] = new char[16 * 2];
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
            logger.error("SlpMD5Util:generateCheckCode", e);
        }
        return result;
    }
	


	
	/**
	 * 输出明文按sha-256加密后的密文
	 * @param inputStr 明文
	 * @return
	 */
	public static synchronized String encryptSha256(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte digest[] = md.digest(inputStr.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
			return null;
		}
	}
	
	 


	private static String byte2hex(byte[] b) {

		String hs = "";
		String stmp = "";
		
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			//if(16==hs.length())	break;
		}
		return hs.toLowerCase();
	}
	
	
	/**
	 * 密钥
	 */
	private static final String key = "@#$%^6a7";   
	       
    /**
     * 对称解密算法
     * @param message
     * @return
     * @throws Exception
     */
    public static String decrypt(String message) throws Exception {   
        byte[] bytesrc = stringToBytes(message);   
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));   
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");   
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);   
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));   
  
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);   
  
        byte[] retByte = cipher.doFinal(bytesrc);   
        return new String(retByte, "UTF-8");   
    }   
	  
    /**
     * 对称加密算法
     * @param message
     * @return
     * @throws Exception
     */
    public static  String encrypt(String message) throws Exception {   
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   
  
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));   
  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");   
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);   
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));   
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);   
  
        String str=bytesToString( cipher.doFinal(message.getBytes("UTF-8")));
        return str;
    }   
  
    /**
     * String转Byte数组   
     * @param temp
     * @return
     */
    private static byte[] stringToBytes(String temp) {   
        byte digest[] = new byte[temp.length() / 2];   
        for (int i = 0; i < digest.length; i++) {   
            String byteString = temp.substring(2 * i, 2 * i + 2);   
            int byteValue = Integer.parseInt(byteString, 16);   
            digest[i] = (byte) byteValue;   
        }   
  
        return digest;   
    }   
       
    /**
     * Byte数组转String   
     * @param b
     * @return
     */
    private static String bytesToString(byte b[]) {   
        StringBuffer hexString = new StringBuffer();   
        for (int i = 0; i < b.length; i++) {   
            String plainText = Integer.toHexString(0xff & b[i]);   
            if (plainText.length() < 2)   
                plainText = "0" + plainText;   
            hexString.append(plainText);   
        }   
 
        return hexString.toString();   
    }

	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}

	
	
//	public static void main(String[] args) throws Exception {
////		String encode = EncryptUtil.encrypt("Bpmmy123*");
////		System.out.println(encode);
////		String decode = EncryptUtil.decrypt(encode);
////		System.out.println(decode);
//		String encode = EncryptUtil.md5("O+sa-2@18");
//	     System.out.println(encode);
//
//
//
//	}



	
	
}
