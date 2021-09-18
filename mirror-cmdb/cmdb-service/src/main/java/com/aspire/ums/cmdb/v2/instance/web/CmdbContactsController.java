package com.aspire.ums.cmdb.v2.instance.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.cmic.service.ICmdbCmicInstanceService;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.instance.ICmdbContactsAPI;
import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;
import com.aspire.ums.cmdb.v2.instance.service.CmdbContactsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 15:56
 */
@Slf4j
@RestController
public class CmdbContactsController implements ICmdbContactsAPI {

    @Autowired
    private CmdbContactsService contactsService;

    @Autowired
    private ICmdbCmicInstanceService iCmdbCmicInstanceService;

    @Autowired
    private ConfigDictMapper configDictMapper;

    @Override
    public ResultVo<CmdbContactsResponse> findContactsInfo(@RequestBody Map<String, Object> param) {
        log.info("CmdbContactsController.findContactsInfo param is {}", param);
        ResultVo resultVo = new ResultVo(false);
        try {
            if (param.containsKey("moduleId") && param.containsKey("instanceId")) {
                List<CmdbContactsResponse> dataList = contactsService.findContactsById(param);
                if (CollectionUtils.isEmpty(dataList)) {
                    resultVo.setMsg("查询无数据！");
                } else {
                    resultVo.setSuccess(true);
                    resultVo.setMsg("查询成功！");
                    resultVo.setData(dataList);
                }
            } else {
                log.info("参数type或instanceId不存在！");
                resultVo.setMsg("参数type或instanceId不存在！");
            }
        } catch (Exception e) {
            log.error("查询接口人失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg(String.format("查询失败！%s", e.getMessage()));
        }
        return resultVo;
    }

    @Override
    public ResultVo<CmdbContactsResponse> allocation(@RequestBody Map<String, Object> param) {
        log.info("CmdbContactsController.allocation param is {}", param);
        ResultVo resultVo = new ResultVo(true, "操作成功！");
        try {
            Object assignStatusObj = param.get("assign_status");
            Object survivalStatusObj = param.get("survival_status");
            if (assignStatusObj != null) {
                String assignStatus = assignStatusObj.toString();
                String assignStatusId = configDictMapper.getIdByNoteAndCol(assignStatus.trim(), "ipAllocationStatusType");
                if (StringUtils.hasText(assignStatusId)) {
                    param.put("assign_status", assignStatusId);
                }
            }
            if (survivalStatusObj != null) {
                String survivalStatus = survivalStatusObj.toString();
                String survivalStatusId = configDictMapper.getIdByNoteAndCol(survivalStatus.trim(), "survival_status");
                if (StringUtils.hasText(survivalStatusId)) {
                    param.put("survival_status", survivalStatusId);
                }
            }
            // String id1 = configDictMapper.getIdByNoteAndCol("未存活", "survival_status");
            // String id2 = configDictMapper.getIdByNoteAndCol("已存活", "survival_status");
            // String id3 = configDictMapper.getIdByNoteAndCol("未分配", "ipAllocationStatusType");
            // String id4 = configDictMapper.getIdByNoteAndCol("已分配", "ipAllocationStatusType");
            String operator = String.valueOf(param.get("operator"));
            Map<String, Object> result = iCmdbCmicInstanceService.updateIpInfo(operator, param, "自动通过");
            if (!result.get("flag").equals("success")) {
                resultVo.setSuccess(false);
                resultVo.setMsg(String.valueOf(result.get("msg")));
            }
        } catch (Exception e) {
            log.error("操作失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg(String.format("操作失败！%s", e.getMessage()));
        }
        return resultVo;
    }
}
