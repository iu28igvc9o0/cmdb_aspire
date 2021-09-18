package com.aspire.ums.cmdb.report.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceInsertReq
 * Author:   hangfang
 * Date:     2020/4/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cmdb31ProvinceInsertReq {

    /**
     * 归属类型
     */
    private String resourceOwner;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 归属表名
     */
    private String resourceClass;

    /**
     * 提交月份
     */
    private String submitMonth;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 各列数据值
     * [{
     *      idc_type: 'xx',
     *      settingId01: reportValue01,
     *      settingId02: reportValue02,
     *      settingId03: reportValue03
     * }]
     */
//    private List<Map<String, Object>> dataList;
    private List<Cmdb31ProvinceTable> tableList;

}
