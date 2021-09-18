package com.aspire.aiops.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/5/2019 14:26
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
public class ResourceUtil {


    public static synchronized Properties loadResource(String fileName){
            Properties props = new Properties();
        InputStream inputStream = null;
        try {
                inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(fileName);
                props.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {

                    }
                }
            }
        return props;
    }

}
