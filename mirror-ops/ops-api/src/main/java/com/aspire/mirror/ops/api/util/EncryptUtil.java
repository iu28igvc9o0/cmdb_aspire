package com.aspire.mirror.ops.api.util;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Base64Utils;

import lombok.extern.slf4j.Slf4j;
 
@Slf4j
public class EncryptUtil {
	public static String		DEFAULT_KEY		 = "aes-default-keys";					// 密钥, 必须为16 byte长度
	private static final String	TRANSFORM		 = "AES/CBC/PKCS5Padding";				// 加密器类型: 加密算法为AES,加密模式为CBC,补码方式为PKCS5Padding
	private static final String	DEFAULT_CHARSET  = "UTF-8";
	private static final int	OFFSET			 = 16;									// 偏移量
	
	public static final String	ALGORITHM_AES	 = "AES";								
	public static final String	ALGORITHM_BASE64 = "BASE64";
	
	/** 
	 * 功能描述: 设置全局默认AES加密key
	 * <p>
	 * @param key
	 */
	public static void setDefaultAesKey(String key) {
		if (StringUtils.isNotBlank(key) && key.getBytes().length == 16) {
			DEFAULT_KEY = key;
		}
	}

	public static String aesEncrypt(String content) {
        return aesEncrypt(content, DEFAULT_KEY);
    }
 
    /** 
     * 功能描述: AES加密  
     * <p>
     * @param content
     * @param key
     * @return
     */
    public static String aesEncrypt(String content, String key) {
        try {
            //构造密钥
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), ALGORITHM_AES);
            //创建初始向量iv用于指定密钥偏移量(可自行指定但必须为128位)，因为AES是分组加密，下一组的iv就用上一组加密的密文来充当
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, OFFSET);
            //创建AES加密器
            Cipher cipher = Cipher.getInstance(TRANSFORM);
            byte[] byteContent = content.getBytes(DEFAULT_CHARSET);
            //使用加密器的加密模式
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //使用BASE64对加密后的二进制数组进行编码
            return Base64Utils.encodeToString(result);
        } catch (Exception e) {
            log.info("Exception when encrypt.", e);
        }
        return null;
    }


    public static String aesDecrypt(String content) {
        return aesDecrypt(content, DEFAULT_KEY);
    }

    /** 
     * 功能描述: AES(256)解密  
     * <p>
     * @param content
     * @param key
     * @return
     */
    public static String aesDecrypt(String content, String key) {
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), ALGORITHM_AES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, OFFSET);
            Cipher cipher = Cipher.getInstance(TRANSFORM);
            //解密时使用加密器的解密模式
            cipher.init(Cipher.DECRYPT_MODE, skey, iv); 
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(result); 
        } catch (Exception e) {
            log.info("Exception when decrypt.", e);
        }
        return content;
    }
    
    
    /** 
     * 功能描述: Base64加密  
     * <p>
     * @param content
     * @return
     */
    public static String base64Encrypt(String content) {
    	return Base64Utils.encodeToString(content.getBytes(Charset.forName(DEFAULT_CHARSET)));
    }
    
    /** 
     * 功能描述: Base64解密  
     * <p>
     * @param content
     * @return
     */
    public static String base64Decrypt(String content) {
    	return new String(Base64Utils.decodeFromString(content), Charset.forName(DEFAULT_CHARSET));
    }
    
    public static void main(String[] args) {
        System.out.println(base64Encrypt("Nmzw$yjd2@19"));
//        String content = "08a44a2d$a702^4585^b20d!ffce42ac9604";
//        String encryptResult = aesEncrypt(content);
//        System.out.println("Raw content：" + content);
//        System.out.println("Encrypt result：" + encryptResult);
//        System.out.println("Decrypt result：" + aesDecrypt(encryptResult));
    }
}