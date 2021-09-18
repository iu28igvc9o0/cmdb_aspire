package com.aspire.ums.bills.log.payload;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BillsLogRequest
 * Author:   hangfang
 * Date:     2021/3/5
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class BillsLogRequest extends  BillsLog{

    private Integer currentPage;
    private Integer pageSize;
}
