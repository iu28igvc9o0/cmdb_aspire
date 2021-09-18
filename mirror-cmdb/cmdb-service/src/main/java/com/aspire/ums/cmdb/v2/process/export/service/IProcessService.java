package com.aspire.ums.cmdb.v2.process.export.service;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IProcessService
 * Author:   zhu.juwang
 * Date:     2019/9/6 16:07
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface IProcessService {
    /**
     * 下载报表文件
     * @param exportType 下载类型
     * @param exportParams 下载参数
     * @return
     */
    List<Map<String, Object>> exportReport(String exportType, Map<String, Object> exportParams);

    /**
     * 下载资源列表
     * @param params
     * @return
     */
    Map<String, String> exportInstance(Map<String, Object> params, String moduleId);

}
