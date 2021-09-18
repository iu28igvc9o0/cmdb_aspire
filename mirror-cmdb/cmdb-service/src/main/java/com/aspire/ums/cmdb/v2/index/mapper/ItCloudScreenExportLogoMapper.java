package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ScreenExport;
import com.aspire.ums.cmdb.index.payload.ScreenExportLogo;

import java.util.List;

/**
 * @ClassName ItCloudScreenExport
 * @Description 大屏导出
 * @Author luowenbo
 * @Date 2020/5/18 17:30
 * @Version 1.0
 */
public interface ItCloudScreenExportLogoMapper {

    /*
    *  查询出当前页面类型时间最近的一条数据
    * */
    ScreenExportLogo getOneByFileUnique(String fileUnique);

    void insert(ScreenExportLogo logo);

    void update(ScreenExportLogo logo);

    void delete(String fileUnqiue);
}
