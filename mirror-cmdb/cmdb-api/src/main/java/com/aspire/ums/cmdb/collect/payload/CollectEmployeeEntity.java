package com.aspire.ums.cmdb.collect.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectEmployeeEntity
 * Author:   zhu.juwang
 * Date:     2019/3/12 10:26
 * Description: 采集通知对象实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
public class CollectEmployeeEntity {
    /*
    ID
     */
    private String id;
    /*
    采集配置ID
     */
    private String collectId;
    /*
    类型 通知/审批
     */
    private String opertype;
    /*
    对象名称
     */
    private String employeeName;
    /*
    对象地址
     */
    private String employeeAddr;
}
