package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ScreenExport
 * @Description 大屏导出数据实体
 * @Author luowenbo
 * @Date 2020/5/18 17:33
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenExport {
    private String id;
    /*
     *  IP
     * */
    private String ip;
    /*
     *  端口
     * */
    private String port;
    /*
     *  请求方式
     * */
    private String reqMethod;
    /*
     *  路径
     * */
    private String url;
    /*
     *  请求头
     * */
    private String reqHeader;
    /*
     *  请求参数
     * */
    private String param;
    /*
     *  excel中sheet的名称
     * */
    private String sheetName;
    /*
     *  导出文件中数据标题行
     * */
    private String excelHeader;
    /*
     *  页面类型
     * */
    private String pageType;
    /*
     *  页面类型
     * */
    private String rspFlag;
    /*
     *  页面类型
     * */
    private String rspType;
    /*
     *  页面类型
     * */
    private String rspKey;
    /*
     *  页面类型
     * */
    private String isSort;
}