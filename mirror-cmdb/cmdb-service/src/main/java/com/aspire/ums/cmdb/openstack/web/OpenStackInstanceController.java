package com.aspire.ums.cmdb.openstack.web;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequestExtInfo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.CmdbVmwareInstanceLog;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRelationDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceRequestData;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;
import com.aspire.ums.cmdb.ipCollect.service.CmdbVmwareInstanceLogService;
import com.aspire.ums.cmdb.openstack.CmdbOpenStackAPI;
import com.aspire.ums.cmdb.openstack.enums.OpenStackEnums;
import com.aspire.ums.cmdb.openstack.service.IOpenstackInstanceService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.util.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/11/16 19:32
 */
@RestController
@Slf4j
public class OpenStackInstanceController implements CmdbOpenStackAPI {

    @Autowired
    private IOpenstackInstanceService instanceService;

    @Autowired
    private CmdbVmwareInstanceLogService instanceLogService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public ResultVo createInstance(@RequestBody InstanceCreateRequest instanceCreateRequest) {
        log.info(">>>>> 接收到自动化平台推送的[创建-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo();
        try {
            String requestBody = JsonMapper.getInstance().toJson(instanceCreateRequest);
            log.info("获取到的自动化请求requestBody={}", requestBody);
            instanceCreateRequest.setRequestBody(requestBody);
            taskExecutor.execute(() -> saveInstanceLog(instanceCreateRequest));

            instanceService.instanceCreate(instanceCreateRequest);
            resultInfo.setSuccess(true);
            resultInfo.setMsg("保存成功!");
        } catch (Exception e) {
            log.error("保存失败", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg("保存失败!");
        }

        log.info("<<<< 自动化平台推送的[创建-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo updateInstance(@RequestBody InstanceUpdateRequest instanceUpdateRequest) {
        log.info(">>>>> 接收到自动化平台推送的[修改-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo();
        try {
            String requestBody = JsonMapper.getInstance().toJson(instanceUpdateRequest);
            log.info("获取到的自动化请求requestBody={}", requestBody);

            // 抽取修改的字段和值，可能是数组或对象或单个字符串
            LinkedHashMap<String, Object> hashMap = com.jayway.jsonpath.JsonPath.parse(requestBody)
                    .read("$.data.ext_info.diff_data", LinkedHashMap.class);
            instanceUpdateRequest.getData().getExtInfo().setChangeData(hashMap);
            log.info("更新字段=={}", hashMap);

            instanceUpdateRequest.setRequestBody(requestBody);

            taskExecutor.execute(() -> saveInstanceLog(instanceUpdateRequest));

            instanceService.instanceUpdate(instanceUpdateRequest);
            resultInfo.setSuccess(true);
            resultInfo.setMsg("保存成功!");
        } catch (Exception e) {
            log.error("更新失败", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg("保存失败!");
        }
        log.info("<<<< 自动化平台推送的[修改-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo deleteInstance(@RequestBody InstanceDeleteRequest instanceDeleteRequest) {
        log.info(">>>>> 接收到自动化平台推送的[删除-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo();
        try {
            String requestBody = JsonMapper.getInstance().toJson(instanceDeleteRequest);
            log.info("获取到的自动化请求requestBody={}", requestBody);

            instanceDeleteRequest.setRequestBody(requestBody);

            taskExecutor.execute(() -> saveInstanceLog(instanceDeleteRequest));

            instanceService.instanceDelete(instanceDeleteRequest);
            resultInfo.setSuccess(true);
            resultInfo.setMsg("删除成功!");
        } catch (Exception e) {
            log.error("删除失败", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg("删除失败!");
        }
        log.info("<<<< 自动化平台推送的[删除-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo createInstanceRel(@RequestBody InstanceRelationCreateRequest instanceCreateRequest) {
        log.info(">>>>> 接收到自动化平台推送的[创建-实例关联关系]请求 >>>>");
        ResultVo resultInfo = new ResultVo();
        try {
            String requestBody = JsonMapper.getInstance().toJson(instanceCreateRequest);
            log.info("获取到的自动化请求requestBody={}", requestBody);
            instanceCreateRequest.setRequestBody(requestBody);
            taskExecutor.execute(() -> saveInstanceLog(instanceCreateRequest));

            instanceService.instanceRelationCreate(instanceCreateRequest);
            resultInfo.setSuccess(true);
            resultInfo.setMsg("保存成功!");
        } catch (Exception e) {
            log.error("保存失败", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg("保存失败!");
        }

        log.info("<<<< 自动化平台推送的[创建-实例关联关系]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo deleteInstanceRel(@RequestBody InstanceRelationDeleteRequest instanceDeleteRequest) {
        log.info(">>>>> 接收到自动化平台推送的[删除-实例关联关系]请求 >>>>");
        ResultVo resultInfo = new ResultVo();
        try {
            String requestBody = JsonMapper.getInstance().toJson(instanceDeleteRequest);
            log.info("获取到的自动化请求requestBody={}", requestBody);

            instanceDeleteRequest.setRequestBody(requestBody);

            taskExecutor.execute(() -> saveInstanceLog(instanceDeleteRequest));

            instanceService.instanceRelationDelete(instanceDeleteRequest);
            resultInfo.setSuccess(true);
            resultInfo.setMsg("删除成功!");
        } catch (Exception e) {
            log.error("删除失败", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg("删除失败!");
        }
        log.info("<<<< 自动化平台推送的[删除-实例关联关系]处理完成! <<<<<");
        return resultInfo;
    }

    /**
     * 保存云管请求的日志.
     *
     * @param instanceRequest
     *            云管instance请求
     * @return
     */
    private void saveInstanceLog(BaseInstanceRequest instanceRequest) {
        CmdbVmwareInstanceLog entity = new CmdbVmwareInstanceLog();
        InstanceRequestData data = instanceRequest.getData();
        BaseInstanceRequestExtInfo extInfo = data.getExtInfo();
        String objectId = extInfo.getObjectId();
        OpenStackEnums enums = OpenStackEnums.fromKey(objectId);
        if (enums == null) {
            return;
        }
        entity.setId(UUIDUtil.getUUID());
        entity.setEventId(data.getEventId());
        entity.setEventType(data.getEvent());
        entity.setObjectId(extInfo.getObjectId());
        entity.setInstanceId(extInfo.getInstanceId());
        entity.setObjectName(extInfo.getObjectName());
        entity.setObjectVersion(extInfo.getObjectVersion());
        entity.setOperator(data.getOperator());
        entity.setOptContent(instanceRequest.getRequestBody());
        entity.setOptDesc(data.getOptDescription());
        entity.setOptTimestamp(extInfo.getTimestamp());
        entity.setTargetCategory(data.getTargetCategory());
        entity.setTargetId(data.getTargetId());
        entity.setTargetName(data.getTargetName());
        entity.setSystem(instanceRequest.getSystem());
        entity.setVersion(extInfo.getVersion());
        instanceLogService.add(entity);
    }
}
