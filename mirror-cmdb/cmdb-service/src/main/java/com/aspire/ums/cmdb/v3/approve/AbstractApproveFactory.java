package com.aspire.ums.cmdb.v3.approve;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;

import java.util.Map;

/**
 * @ClassName AbstractApproveFactory
 * @Description 审核工厂类
 * @Author luowenbo
 * @Date 2020/3/21 13:37
 * @Version 1.0
 */
public abstract class AbstractApproveFactory {

    /**
     * 验证方法
     * @param cmdbCode 码表对象
     * @param codeApprove 验证对象
     * @param value 验证对象
     * @return
     */
    public abstract Map<String, String> valid(final CmdbCode cmdbCode, final CmdbV3CodeApprove codeApprove, final Object value);

}
