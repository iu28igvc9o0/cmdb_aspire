package com.aspire.ums.cmdb.cmic.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 17:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbModelTabsBase {
    //ID
    private String tabsId;
    //模型ID
    private String modelId;
    //TAB名称
    private String tabName;
    //引入组件名称
    private String subassemblyName;
    //显示顺序
    private Integer sortIndex;
    //参数配置
    private String paramConfig;
    //回调配置
    private String callbackConfig;

    public CmdbModelTabsBase(String tabsId) {
        this.tabsId = tabsId;
    }

    public CmdbModelTabsBase(String modelId, String tabName) {
        this.modelId = modelId;
        this.tabName = tabName;
    }
}
