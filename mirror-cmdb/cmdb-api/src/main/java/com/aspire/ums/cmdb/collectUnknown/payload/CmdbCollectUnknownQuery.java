package com.aspire.ums.cmdb.collectUnknown.payload;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/10/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCollectUnknownQuery extends GeneralAuthConstraintsAware {

    /**
     * IP地址
     */
    private String ip;
    /**
     * 资源池
     */
    private String idcType;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 数据来源（监控上报/自动采集/苏研对接/自动发现）
     */
    private String dataFrom;

    /**
     * 处理状态 (0:待处理,1:已维护,2:已屏蔽)
     */
    private Integer handleStatus;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;

}
