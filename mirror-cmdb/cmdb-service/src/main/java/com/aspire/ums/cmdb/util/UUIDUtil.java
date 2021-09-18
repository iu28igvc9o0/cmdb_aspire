package com.aspire.ums.cmdb.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UUIDUtil {
	
	 /**  
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。  
     * @return  
     */  
    public static String getUUID() {  
        return UUID.randomUUID().toString().replace("-", "");  
    } 
    
    /**
     * 根据时间生成主键
     * @return
     */
    public synchronized static  String getDateTimeUniqueId() {
        // 将时间变为字符串，到毫秒级别 生成17位
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String temp = sf.format(new Date());
        // 随机数 生成4位
        SecureRandom rand = new SecureRandom();
        Integer tailNum = rand.nextInt(9999);
        // 生成的ID
        String dbId = temp + tailNum;
        return dbId;
    }
    
   /* public static void main(String[] args) {
    	for(int i = 1 ;i<=24;i++){
    		System.out.println(i+" ==== "+UUIDUtil.getUUID());
    	}
	}*/
    
}
