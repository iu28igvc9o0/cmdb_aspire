package com.aspire.ums.cmdb.v3.approve;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.util.ResultUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AutoRejectedApproveFactory
 * @Description 需要审核的数据项直接驳回,数据发往ES
 * @Author luowenbo
 * @Date 2020/3/21 14:16
 * @Version 1.0
 */
public class AutoRejectedApproveFactory extends AbstractApproveFactory{

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Map<String, String> valid(CmdbCode cmdbCode, CmdbV3CodeApprove codeApprove, Object value) {
        Map<String, String> rsMp = new HashMap<>();
        rsMp.put(ResultUtils.SUCCESS,ResultUtils.SUCCESS);
        rsMp.put("status","reject");
        rsMp.put("msg",codeApprove.getApproveType());
        rsMp.put("reason",codeApprove.getApproveTypeExpression());
        // 封装审核对象
        CmdbCollectApproval approval = (CmdbCollectApproval)value;
        approval.setApprovalStatus(2);
        approval.setApprovalUser("系统");
        approval.setApprovalTime(new Date());
        getKafkaTemplate().send("cmdb_approval_info", JSON.toJSONString(approval));
        return rsMp;
    }

    private KafkaTemplate getKafkaTemplate() {
        if (kafkaTemplate == null) {
            kafkaTemplate = SpringUtils.getBean(KafkaTemplate.class);
        }
        return kafkaTemplate;
    }
}
