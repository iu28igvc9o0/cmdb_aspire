package com.aspire.ums.cmdb.cmic.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 17:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbModelTabsResp extends CmdbModelTabsBase{
    //模型名称
    private String modelName;
}
