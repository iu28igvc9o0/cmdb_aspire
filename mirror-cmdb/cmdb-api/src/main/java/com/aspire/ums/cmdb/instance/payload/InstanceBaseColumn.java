package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceBaseColumn
 * Author:   zhu.juwang
 * Date:     2019/7/2 10:25
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstanceBaseColumn {
    private String instanceId;
    private String name;
    private String moduleId;
    private String insertTime;
    private String updateTime;
    private String moduleName;
    private String roomId;
    private String roomName;
    private String bizSystemName;
    private String ip;
    private String bizSystem;
    private Integer isDelete;
    private String idcType;
    private String deviceRegion;
    private String deviceClass;

}
