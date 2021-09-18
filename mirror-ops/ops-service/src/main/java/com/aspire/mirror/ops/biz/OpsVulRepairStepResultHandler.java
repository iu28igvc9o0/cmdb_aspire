package com.aspire.mirror.ops.biz;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.ops.api.domain.AspNodeResultEnum;
import com.aspire.mirror.ops.api.domain.OpsMessageExtendMeta;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsStatusEnum;
import com.aspire.mirror.ops.api.domain.vulnerability.OpsVulnerabilityInstance;
import com.aspire.mirror.ops.api.domain.vulnerability.OpsVulnerabilityInstance.VulnerabilityStatus;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult;
import com.aspire.mirror.ops.biz.model.OpsMessageMeta;
import com.aspire.mirror.ops.biz.vulnerability.VulnerabilityBiz;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;

import lombok.extern.slf4j.Slf4j;


/**
 * 项目名称: ops-service
 * <p/>
 * <p>
 * 类名: OpsVulRepairStepResultHandler
 * <p/>
 * <p>
 * 类功能描述: 漏洞修复/回退步骤结果处理类
 * <p/>
 *
 * @author pengguihua
 * @date 2020年7月9日
 * @version V1.0 <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有
 */
@Slf4j
@Service
@Order(Ordered.LOWEST_PRECEDENCE)
class OpsVulRepairStepResultHandler extends AbstractAgentOpsStepResultHandler {
    @Autowired
    private OpsBaseDataBiz opsBaseDataBiz;
    @Autowired
    private VulnerabilityBiz vulBiz;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean aware(OpsActionAgentResult agentStepResult) {
        // 反馈还未完成, 直接跳过
        if (!agentStepResult.getOpsResultDetail().isOver()) {
            return false;
        }

        OpsMessageExtendMeta extendMeta = agentStepResult.getMeta().getExtendMeta();
        if (extendMeta != null && (OpsPipelineInstance.BIZ_CLASSIFY_VUL_REPAIR.equals(extendMeta.getBizClassify()) || OpsPipelineInstance.BIZ_CLASSIFY_VUL_GO_BACK.equals(extendMeta.getBizClassify()) || OpsPipelineInstance.BIZ_CLASSIFY_VUL_RECHECK.equals(extendMeta.getBizClassify()))) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OpsActionAgentResult agentStepResult) {
        OpsMessageMeta meta = agentStepResult.getMeta();
        OpsPipelineInstanceDTO currpipeInst = opsBaseDataBiz.queryOpsPipelineInstanceById(meta.getPipelineInstanceId());
        if (!currpipeInst.isExecuteOver()) {
            log.info("The vulnerability repair pipeline instance with id {} has not finished.", currpipeInst.getPipelineInstanceId());
            return;
        }

        VulnerabilityStatus status = VulnerabilityStatus.WAIT_REPAIR;
        String opreateType = OpsVulnerabilityInstance.OPERATE_REPAIR;
        String isFixed = "N";
        if (OpsStatusEnum.STATUS_9.getStatusCode().equals(currpipeInst.getStatus())
                && AspNodeResultEnum.STATUS_0.getStatusCode().equals(currpipeInst.getAspNodeResult())) {
            if (meta.getExtendMeta().getBizClassify().equals(OpsPipelineInstance.BIZ_CLASSIFY_VUL_REPAIR)) {
                status = VulnerabilityStatus.PROCESSED;
            } else if (meta.getExtendMeta().getBizClassify().equals(OpsPipelineInstance.BIZ_CLASSIFY_VUL_GO_BACK)) {
                status = VulnerabilityStatus.WAIT_REPAIR;
                opreateType = OpsVulnerabilityInstance.OPERATE_GO_BACK;
            } else {
                isFixed = "Y";
                status = VulnerabilityStatus.PROCESSED;
                opreateType = OpsVulnerabilityInstance.OPERATE_RECHECK;
            }
        }

        Object vulInstanceId = meta.getExtendMeta().getAttrValByKey("vulInstanceId");
        String operator = meta.getExtendMeta().getAttrValByKey("operator", String.class);
        String[] vulInstIdList = vulInstanceId.toString().split(",");
        for (String vulInstId : vulInstIdList) {
            Date now = new Date();
            // 回写漏洞实例状态
            OpsVulnerabilityInstance vulInstance = vulBiz.getVulnerabilityInstanceById(Long.valueOf(vulInstId));
            vulInstance.setLastOperator(operator);
            vulInstance.setStatus(status);
            vulInstance.setLastUpdateTime(now);
            vulInstance.setIsFixed(isFixed);
            if (opreateType.equals(OpsVulnerabilityInstance.OPERATE_REPAIR)) {
                vulInstance.setRepairPerson(operator);
                vulInstance.setRepairTime(now);
                vulInstance.setRepairPipelineInstanceId(currpipeInst.getPipelineInstanceId());
            } else if(opreateType.equals(OpsVulnerabilityInstance.OPERATE_GO_BACK)) {
                vulInstance.setGoBackPerson(operator);
                vulInstance.setGoBackTime(now);
                vulInstance.setGoBackPipelineInstanceId(currpipeInst.getPipelineInstanceId());
            } else{
                vulInstance.setRecheckPerson(operator);
                vulInstance.setRecheckTime(now);
                vulInstance.setRecheckPipelineInstanceId(currpipeInst.getPipelineInstanceId());
            }
            vulBiz.updateOpsVulnerabilityInstance(vulInstance);
        }

    }
}
