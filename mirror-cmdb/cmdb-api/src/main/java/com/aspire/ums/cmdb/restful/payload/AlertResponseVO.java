package com.aspire.ums.cmdb.restful.payload;

import com.aspire.ums.cmdb.systemManager.payload.Concat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertResponseVO
 * Author:   zhu.juwang
 * Date:     2019/7/29 17:06
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlertResponseVO {
    /**
     * 一级部门
     */
    private String department1;
    /**
     * 二级部门
     */
    private String department2;
    /**
     * 业务系统
     */
    private String bizSystem;
    /**
     * 业务联系人
     */
    private List<Concat> concatList;
}
