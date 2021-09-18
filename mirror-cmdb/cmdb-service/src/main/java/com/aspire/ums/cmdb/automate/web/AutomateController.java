package com.aspire.ums.cmdb.automate.web;

import com.aspire.ums.cmdb.automate.IAutomateAPI;
import com.aspire.ums.cmdb.automate.constants.AutomateConfigConstant;
import com.aspire.ums.cmdb.automate.service.AutomateInstanceLogService;
import com.aspire.ums.cmdb.automate.service.AutomateService;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.util.JsonUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fanwenhui
 * @date 2020-08-21 17:15
 * @description 自动化模型对外处理接口
 */
@Slf4j
@RestController
public class AutomateController implements IAutomateAPI {

    @Autowired
    private AutomateService automateService;
    @Autowired
    private AutomateInstanceLogService logService;
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${kafka.topic.automate_host_syn:automate_host_syn}")
    private String topic;
    @Value(value = "${kafka.topic.partition.automate_host_syn:0}")
    private String partition;

    @Override
    public ResultVo create(@RequestBody String param) {
        log.info(">>>>> 接收到主机配置模型[创建-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo(true, "保存成功!");
        Map<String, Object> synParam = automateService.buildSynParam(param, AutomateConfigConstant.AUTOMATE_CREATE);
        String synJson = JsonUtil.toJacksonJson(synParam);
        kafkaTemplate.send(topic, partition,synJson);
        logService.saveKafkaHostLog(synParam);
        log.info("<<<< 主机配置模型[创建-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo update(@RequestBody String param) {
        log.info(">>>>> 接收到主机配置模型[修改-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo(true, "更新成功！");
        Map<String, Object> synParam = automateService.buildSynParam(param, AutomateConfigConstant.AUTOMATE_UPDATE);
        String synJson = JsonUtil.toJacksonJson(synParam);
        kafkaTemplate.send(topic, partition,synJson);
        logService.saveKafkaHostLog(synParam);
        log.info("<<<< 接收到主机配置模型[修改-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo delete(@RequestBody String param) {
        log.info(">>>>> 接收到主机配置模型[删除-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo(true, "删除成功！");
        Map<String, Object> synParam = automateService.buildSynParam(param, AutomateConfigConstant.AUTOMATE_DELETE);
        String synJson = JsonUtil.toJacksonJson(synParam);
        kafkaTemplate.send(topic, partition,synJson);
        logService.saveKafkaHostLog(synParam);
        log.info("<<<< 接收到主机配置模型[删除-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo getAutomateHostDetail(@RequestBody Map<String,String> param) {
        ResultVo resultVo = new ResultVo(false, "查询失败！");
        if (null == param) {
            resultVo.setMsg("参数不能为空");
            return resultVo;
        }
        String ip = param.get("ip");
        if (StringUtils.isEmpty(ip)) {
            resultVo.setMsg("设备IP入参不能为空");
            return resultVo;
        }
        Map<String, Object> detail = automateService.getAutomateHostDetail(ip);
        if (null == detail) {
            resultVo.setMsg("没有对应的主机配置信息");
            return resultVo;
        }
        resultVo.setSuccess(true);
        resultVo.setMsg("查询成功");
        resultVo.setData(detail);
        return resultVo;
    }

    @Override
    public List<Map<String, Object>> getAutomateColumns() {
        return automateService.getAutomateColumns();
    }

    @Override
    public ResultVo syncModule4Redis() {
        ResultVo resultVo = new ResultVo(true, "模型录入redis成功！");
        boolean synFlag = automateService.syncModule4Redis();
        if (!synFlag) {
            resultVo.setSuccess(false);
            resultVo.setMsg("模型录入redis失败");
        }
        return resultVo;
    }

    @Override
    public Map<String, List<String>> buildExportHeaderList() {
        return automateService.buildExportHeaderList();
    }

    @Override
    public ResultVo synAutomateConfFile() {
        ResultVo resultVo = new ResultVo(true, "主机配置文件同步成！");
        try {
            automateService.synAutomateConfFile();
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setMsg("主机配置文件同步失败");
            log.error("主机配置文件同步失败",e);
        }
        return resultVo;
    }

    @Override
    public List<Map<String, String>> findAutomateConfList(@RequestBody Map<String,Object> param) {
        return automateService.findAutomateConfList(param);
    }
}
