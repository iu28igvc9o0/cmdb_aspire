package com.aspire.ums.cmdb.v3.approve;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.util.ResultUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AutoPassApproveFactory
 * @Description 需要审核的数据项直接通过,数据发往ES
 * @Author luowenbo
 * @Date 2020/3/21 14:15
 * @Version 1.0
 */
public class AutoPassApproveFactory extends AbstractApproveFactory{

    private ICmdbInstanceService instanceService;

    @Override
    public Map<String, String> valid(CmdbCode cmdbCode, CmdbV3CodeApprove codeApprove, Object value) {
        Map<String, String> rsMp = new HashMap<>();
        rsMp.put(ResultUtils.SUCCESS,ResultUtils.SUCCESS);
        rsMp.put("status","pass");
        rsMp.put("msg",codeApprove.getApproveType());
        rsMp.put("reason",codeApprove.getApproveTypeExpression());
        // 封装审核对象
        CmdbCollectApproval approval = (CmdbCollectApproval)value;
        approval.setApprovalUser("系统");
        approval.setApprovalTime(new Date());
        approval.setApprovalStatus(1);
//        getInstanceService().updateInstance(approval.getInstanceId(), "系统", );
        return rsMp;
    }

    private ICmdbInstanceService getInstanceService() {
        if (instanceService == null) {
            instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        return instanceService;
    }
}
