package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ScreenExport
 * @Description 大屏导出标识实体
 * @Author luowenbo
 * @Date 2020/5/18 17:33
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenExportLogo {
    private String id;
    /*
     *  文件唯一标识
     * */
    private String fileUnqiue;
    /*
    *  下载路径
    * */
    private String path;
    /*
    *  状态
    * */
    private String status;
    /*
    *  备注
    * */
    private String remark;
    /*
    *  新增时间
    * */
    private String insertTime;
}