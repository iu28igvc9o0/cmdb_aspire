package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 15:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbContactsResponse {
    private String id;
    // 联系人名字
    private String businessCmContact;
    // 电话
    private String businessCmTel;
    // 邮箱
    private String businessCmMail;
    // 独立业务线
    private String businessLevel1;
    // 独立业务线子模块
    private String businessLevel2;
}
