package com.aspire.ums.cmdb.IpResource.service;

import java.util.List;

/**
 * @author fanwenhui
 * @date 2020-06-18 15:04
 * @description 资产信息表导出
 */
public interface AssetExportExcelService {

    /**
     * 构建资产模板excel导出参数
     */
    List<List<Object>> getAssetExportInfo();

    /**
     * 构建资产模板excel头部参数
     */
    String[] buildAssetExcelHeader();
}
