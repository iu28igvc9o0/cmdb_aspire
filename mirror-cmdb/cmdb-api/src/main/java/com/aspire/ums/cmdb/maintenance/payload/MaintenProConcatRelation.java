package com.aspire.ums.cmdb.maintenance.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MaintenProConcatRelation
 * Author:   hangfang
 * Date:     2019/10/25
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@Data
public class MaintenProConcatRelation {

    private String id;

    // 联系人id
    private String concatId;
    // 人员类型id
    private String typeId;
}
