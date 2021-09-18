package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ScreenExport;

import java.util.List;

/**
 * @ClassName ItCloudScreenExport
 * @Description 大屏导出
 * @Author luowenbo
 * @Date 2020/5/18 17:30
 * @Version 1.0
 */
public interface ItCloudScreenExportMapper {

    /*
    *  依据页面类型，查询出该页面所有的请求
    * */
    List<ScreenExport> listByType(String type);
}
