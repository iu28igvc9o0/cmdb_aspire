package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author menglinjie
 */
public class UUIDUtil {
	
	 /**  
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。  
     * @return  
     */  
    public static String getUUID() {  
        return UUID.randomUUID().toString().replace("-", "");  
    } 
    

    /**
     * 根据传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat
     *            yyyyMMddhhmmss
     * @return
     */
    public static String getDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getRandomNum(int num){

        String numStr = "";
        try {
            // luowenbo 20200730 随机数生成,用SecureRandom随机生成[0,10)的整数
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            for(int i = 0; i < num; i++){
                numStr += random.nextInt(10);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return numStr;
    }
    /**
     * 生成id
     * @return
     */
    public static Long getGeneratID(){
        String sformat = "MMddhhmmssSSS";
        int num = 3;
        String idStr = getDate(sformat) + getRandomNum(num);
        Long id = Long.valueOf(idStr);
        return id;
    }
}
