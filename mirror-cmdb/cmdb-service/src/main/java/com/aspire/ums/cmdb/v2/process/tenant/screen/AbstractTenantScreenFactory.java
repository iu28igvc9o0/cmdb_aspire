package com.aspire.ums.cmdb.v2.process.tenant.screen;

import com.aspire.ums.cmdb.v2.process.ImportFactory;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: AbstractTenantScreenFactory
 * Author:   zhu.juwang
 * Date:     2020/4/22 9:25
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
public abstract class AbstractTenantScreenFactory extends ImportFactory {

    public List<Map<String, Object>> podList;
    public List<Map<String, Object>> idcTypeList;

    @Override
    public void initBasic() {
        super.initBasic();
        podList = getDictList("select pod_code `key`, pod_name `value` from cmdb_pod_manager");
        idcTypeList = getDictList("select idc_code `key`, idc_name `value` from cmdb_idc_manager");
    }

    @Override
    public abstract void initSpringBean();

    @Override
    public abstract void execute(Map<String, String> dataMap);
}
