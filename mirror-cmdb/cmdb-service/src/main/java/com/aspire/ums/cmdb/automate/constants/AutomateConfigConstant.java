package com.aspire.ums.cmdb.automate.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author fanwenhui
 * @date 2020-08-21 11:04
 * @description 新炬自动化平台对接常量
 */
@Component
public class AutomateConfigConstant {
    // 公网环境AccessKey
    private static String ACCESS_KEY;
    // 公网环境secretKey
    private static String SECRET_KEY;
    // 公网环境url
    private static String URL;
    // 同步类型 - 新增
    public static final String AUTOMATE_CREATE = "create";
    // 同步类型 - 更新
    public static final String AUTOMATE_UPDATE = "update";
    // 同步类型 - 删除
    public static final String AUTOMATE_DELETE = "delete";
    // 操作结果标识 - 成功
    public static final String AUTOMATE_SUCCESS = "0";
    // 操作结果标识 - 失败
    public static final String AUTOMATE_FAIL = "1";
    // 操作结果描述 - 同步成功
    public static final String AUTOMATE_SUCCESS_DESC = "同步成功";

    // 主机配置文件根路径
    public static final String AUTOMATE_CONF_ROOT = "/tmp/hostconf/data/";
    // 主机配置文件同步需要解析的文件名称
    public static final String AUTOMATE_CONF_JSON = "backup_info.json";
    // 主机配置文件同步的json文件信息中的时间戳key值
    public static final String AUTOMATE_CONF_JSON_TIME_KEY = "time";
    // 文件分隔符
    public static final String AUTOMATE_CONF_SEPARATOR = File.separator;

    @Value("${automate.config.access_key:50a662c0840a6f668378c1ca}")
    public void setAccessKey(String accessKey) {
        AutomateConfigConstant.ACCESS_KEY = accessKey;
    }

    @Value("${automate.config.secret_key:7652505561796f4250686c626e78444f4d424a466a4d767a434d4c515a48504f}")
    public void setSecretKey(String secretKey) {
        AutomateConfigConstant.SECRET_KEY = secretKey;
    }

    @Value("${automate.config.url:http://172.16.108.104:80}")
    public void setURL(String URL) {
        AutomateConfigConstant.URL = URL;
    }

    public static String getAccessKey() {
        return ACCESS_KEY;
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static String getURL() {
        return URL;
    }

}
