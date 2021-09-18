package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ScreenValidate
 * @Description 大屏导入数据验证
 * @Author luowenbo
 * @Date 2020/5/6 14:12
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenValidate {
    private String id;
    private String systemTitleId;
    /*
    *  数据验证类型
    * */
    private String validateType;
    /*
    *  验证结果
    * */
    private String validateResult;
    /*
    *  验证的月报时间
    * */
    private String monthlyTime;
    /*
    *  验证时间
    * */
    private String insertTime;
    /*
    *  是否删除
    * */
    private String isDelete;
}
