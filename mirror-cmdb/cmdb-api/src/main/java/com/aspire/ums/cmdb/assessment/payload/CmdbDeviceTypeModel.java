package com.aspire.ums.cmdb.assessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDeviceTypeModel
 * Author:   hangfang
 * Date:     2019/12/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbDeviceTypeModel {

    // 设备类型id
    private String deviceTypeId;
    // 设备类型名称
    private String deviceType;
    // 设备型号
    private String deviceModel;
}
