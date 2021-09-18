package com.aspire.ums.cmdb.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudResult
 * Author:   hangfang
 * Date:     2021/1/13
 * Description: 返回给云管数据格式
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudResult<A> {
    private String returncode;
    private String returnmsg;
    private List<A> rcd;
}
