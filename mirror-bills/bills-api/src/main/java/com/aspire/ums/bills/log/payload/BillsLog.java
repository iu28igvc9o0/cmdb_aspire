package com.aspire.ums.bills.log.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BillsLog
 * Author:   hangfang
 * Date:     2021/3/1
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillsLog {
    /**
     * 操作人IP
     */
    private String ip;
    /**
     * 操作人IP
     */
    private String operateUser;
    /**
     * 操作对象(单价、日账单、月账单)
     */
    private String operateOBJ;
    /**
     *操作类型（新增，删除，修改）
     */
    private String operateType;
    /**
     * 操作内容
     */
    private String operateContent;
    /**
     * 操作时间
     */
    private String operateTime;
}


