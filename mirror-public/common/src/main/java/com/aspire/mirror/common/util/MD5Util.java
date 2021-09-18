
package com.aspire.mirror.common.util;

import java.security.MessageDigest;

/**
 * md5加密密码为32.
 * */
public class MD5Util {

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }
    
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static void main(String args[]) {
        String key = "sanguowudi7y001ad";
        String area = "1";      //��Ϸ����
        String player = "aaa";     //Aƽ̨�ʺ�
        String order_sn = "123456";   //�������?        String coin = "1000";      //��Ϸ��
        String bonus_coin = "0"; //������Ϸ��

        String password = "123456";
        
        String orgid = "orgid123";
        String groupid  = "groupid123";
        String typeid = "typeid123";
        
        String roleid = "owuer930303roleid";
        
        
        String functionfirstMenuID = "khkhkkluiyi=";
        
        
       
        String sn = MD5Util.MD5Encode(functionfirstMenuID, null).toLowerCase();
        System.out.println(MD5Util.MD5Encode("Nmzw$yjd2@19", null));

    }
}
