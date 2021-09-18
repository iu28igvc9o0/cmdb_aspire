package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ItCloudScreenValidateRequest
 * @Description IT云租户大屏数据验证请求类
 * @Author luowenbo
 * @Date 2020/5/6 14:16
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItCloudScreenValidateRequest {
    /*
     * 标题
     **/
    private String systemTitle;
    /*
    * 验证类型
    **/
    private String validateType;
    /*
    *  验证的月报时间
    * */
    private String monthlyTime;

    /*
    *  表名
    * */
    private String tableName;

    /*
    *  设备类型
    * */
    private String deviceType;

    /*
    *  硬件类型
    * */
    private String hardwareType;
}
