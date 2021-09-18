package com.aspire.ums.cmdb.systemManager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BizSystemQuery
 * Author:   zhu.juwang
 * Date:     2019/8/29 11:24
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizSystemQuery {
    /**
     * 父业务系统ID
     */
    private String parentBizId;
    /**
     * 父业务系统名称
     */
    private String parentBizName;
    /**
     * 业务系统名称
     */
    private String bizName;
    /**
     * 是否已经下线
     */
    private String isDisable;
    /**
     * 一级部门名称
     */
    private String department1;
    /**
     * 二级部门名称
     */
    private String department2;
    /**
     * 业务系统联系人
     */
    private String concatName;
}
