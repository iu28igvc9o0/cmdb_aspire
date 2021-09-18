package com.aspire.ums.cmdb.ipCollect.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 云管vmware同步常量.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 17:29
 */
@Component
public class VmwareConfigConstant {

    /**
     * 公网环境AccessKey.
     */
    public static String ACCESS_KEY;

    /**
     * 公网环境secretKey.
     */
    public static String SECRET_KEY;

    /**
     * 公网环境url
     */
    public static String URL;

    @Value("${vmware.config.access_key:773d6a9c4bcf11e573cdcff0}")
    public void setAccessKey(String accessKey) {
        VmwareConfigConstant.ACCESS_KEY = accessKey;
    }

    @Value("${vmware.config.secret_key:76795445526b4f53667a78505563505a584172434e547865446a614375416562}")
    public void setSecretKey(String secretKey) {
        VmwareConfigConstant.SECRET_KEY = secretKey;
    }

    @Value("${vmware.config.url:http://172.16.108.104:80}")
    public void setURL(String URL) {
        VmwareConfigConstant.URL = URL;
    }
}
