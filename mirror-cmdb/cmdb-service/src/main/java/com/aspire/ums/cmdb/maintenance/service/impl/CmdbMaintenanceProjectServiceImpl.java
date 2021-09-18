package com.aspire.ums.cmdb.maintenance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.client.CmdbApprovalESClient;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenancePersonInfoMapper;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceProjectInstanceMapper;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceProjectMapper;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceServiceNumMapper;
import com.aspire.ums.cmdb.maintenance.payload.*;
import com.aspire.ums.cmdb.maintenance.service.*;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:46
*/
@Service
@Slf4j
public class CmdbMaintenanceProjectServiceImpl implements ICmdbMaintenanceProjectService {

    @Autowired
    private CmdbMaintenanceProjectMapper mapper;
    @Autowired
    private ICmdbMaintenanceProjectMfrsService mfrsService;
    @Autowired
    private ICmdbMaintenanceProjectInstanceService projectInstanceService;
    @Autowired
    private CmdbMaintenanceProjectInstanceMapper projectInstanceMapper;
    @Autowired
    private MaintenSoftService maintenSoftService;
    @Autowired
    private IHardWareUseService hardWareUseService;
    @Autowired
    private CmdbMaintenanceServiceNumMapper cmdbMaintenanceServiceNumMapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private CmdbCollectApprovalService approvalService;
    @Autowired
    private ConfigDictService configDictService;
    @Autowired
    private CmdbMaintenancePersonInfoMapper cmdbMaintenancePersonInfoMapper;
    @Autowired
    private CmdbApprovalESClient approvalESClient;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbMaintenanceProject> list() {
        return mapper.list();
    }

    @Override
    public Result<Map<String, Object>> getSimpleList(CmdbMaintenanceProjectQuery entity) {
        if (StringUtils.isNotEmpty(entity.getPageNo()) && StringUtils.isNotEmpty(entity.getPageSize())) {
            entity.setPageNo((entity.getPageNo() - 1) * entity.getPageSize());
        }
        List<Map<String, Object>> list = mapper.getSimpleList(entity);
        Integer totalCount = mapper.getSimpleListCount(entity);
        return new Result<>(totalCount, list);
    }

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    @Override
    public Result<CmdbMaintenanceProject> listByEntity(CmdbMaintenanceProjectQuery entity) {
        if (StringUtils.isNotEmpty(entity.getPageNo()) && StringUtils.isNotEmpty(entity.getPageSize())) {
            entity.setPageNo((entity.getPageNo() - 1) * entity.getPageSize());
        }
        List<CmdbMaintenanceProject> list = mapper.listByEntity(entity);
        Integer totalCount = mapper.listByEntityCount(entity);
        return new Result<CmdbMaintenanceProject>(totalCount, list);
    }

    @Override
    public Result<Map<String,Object>> listNotMoney(CmdbMaintenanceProjectQuery entity) {
        if (StringUtils.isNotEmpty(entity.getPageNo()) && StringUtils.isNotEmpty(entity.getPageSize())) {
            entity.setPageNo((entity.getPageNo() - 1) * entity.getPageSize());
        }
        List<Map<String,Object>> list = mapper.listNotMoney(entity);
        // 时间戳转换格式
        for(Map<String,Object> item : list) {
            item.put("service_start_time",item.get("service_start_time").toString());
            item.put("service_end_time",item.get("service_end_time").toString());
        }
        Integer totalCount = mapper.listNotMoneyCount(entity);
        return new Result<Map<String,Object>>(totalCount, list);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbMaintenanceProject get(CmdbMaintenanceProject entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void save(CmdbMaintenanceProject entity) {
        // 新增维保项目信息
        // 更新
        if (StringUtils.isNotEmpty(entity.getId())) {
            mapper.update(entity);
        } else {
            entity.setId(UUIDUtil.getUUID());
            if(StringUtils.isEmpty(entity.getQuarterFlag())) {
                entity.setQuarterFlag(UUIDUtil.getUUID());
            }
            mapper.insert(entity);
        }
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void update(CmdbMaintenanceProject entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param projectId 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public JSONObject deleteMaintenanceProject(String projectId) {
        JSONObject returnJson = new JSONObject();
        // 判断项目是否有被硬件维保使用
        List<LinkedHashMap> hardList = hardWareUseService.queryByProjectId(projectId);
        if (hardList != null && hardList.size() > 0) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "该项目已被硬件维保使用, 无法删除.");
            return returnJson;
        }
        // 判断项目是否有被软件维保使用
        MaintenSoftPageRequest maintenSoftPageRequest = new MaintenSoftPageRequest();
        maintenSoftPageRequest.setProjectId(projectId);
        PageBean<MaintenSoftPageResp> softList = maintenSoftService.selectMaintenSoftByPage(maintenSoftPageRequest);
        if (softList != null && softList.getCount() > 0) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "该项目已被软件维保使用, 无法删除.");
            return returnJson;
        }
        // 删除关联设备信息
        List<CmdbMaintenanceProjectInstance> instanceList = projectInstanceService.getProjectInstanceListByProjectId(projectId);
        if (instanceList != null && instanceList.size() > 0) {
            instanceList.forEach((instance) -> {
                projectInstanceMapper.delete(instance);
            });
        }
        // 删除项目信息
        CmdbMaintenanceProject project = new CmdbMaintenanceProject();
        project.setId(projectId);
        mapper.delete(project);
        returnJson.put("flag", "success");
        return returnJson;
    }

    @Override
    public List<Map<String, Object>> exportProjectList(CmdbMaintenanceProjectQuery query) {
        return mapper.exportProjectList(query);
    }

    @Override
    public CmdbMaintenanceProject getValidProjectByDeviceSn(String deviceSn) {
        List<CmdbMaintenanceProject> projectList = mapper.getValidProjectByDeviceSn(deviceSn);
        if (projectList != null && projectList.size() > 0) {
            return projectList.get(0);
        }
        return null;
    }

    @Override
    public void insertServiceNum(List<CmdbMaintenanceServiceNum> data) {
        String projectId = data.get(0).getProjectId();
        // 删除之前的服务数量
        cmdbMaintenanceServiceNumMapper.delete(projectId);
        for(CmdbMaintenanceServiceNum item : data) {
            item.setId(UUIDUtil.getUUID());
        }
        // 保存
        cmdbMaintenanceServiceNumMapper.insertBatch(data);
    }

    @Override
    public String syncInstanceMaintenanceInfo(String projectId,List<String> projectInstanceList) {
        List<Map<String, String>> instanceList = mapper.getProjectProduceConcat(projectId,projectInstanceList);
        if (instanceList != null) {
            for (Map<String, String> info : instanceList) {
                String instanceId = info.get("instance_id");
                if (StringUtils.isNotEmpty(instanceId)) {
                    try {
                        //维保开始时间
                        String strapTime = String.valueOf(info.get("service_start_time"));
                        if (strapTime.length() > 20) {
                            strapTime = strapTime.substring(0, 19);
                        }
                        Date date = DateUtils.parseDate(strapTime, new String[]{"yyyy-MM-dd HH:mm:ss"});
                        String startTime = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
                        maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_START_TIME, startTime);
                        //硬件设备报废时间
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.YEAR, Constants.CI_DEFAULT_USE_TIME);
                        String scrapTime = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
                        maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_SCRAP_TIME, scrapTime);
                    } catch (ParseException e) {
                        log.error("维保开始时间转化错误", e);
                    }
                    try {
                        //维保结束时间
                        String endTime = String.valueOf(info.get("service_end_time"));
                        if (endTime.length() > 20) {
                            endTime = endTime.substring(0, 19);
                        }
                        Date date = DateUtils.parseDate(endTime, new String[]{"yyyy-MM-dd HH:mm:ss"});
                        maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_END_TIME, DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
                    } catch (ParseException e) {
                        log.error("维保结束时间转化错误", e);
                    }
                    //服务供应商名称
                    String produceName = info.get("produce_name");
                    maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_PROVIDER, produceName);
                    //服务联系人
                    String produceConcatName = info.get("produce_concat_name");
                    maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_PROVIDER_CONT, produceConcatName);
                    //服务联系人电话
                    String produceConcatPhone = info.get("produce_concat_phone");
                    maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_PROVIDER_CONT_PHONE, produceConcatPhone);
                    //服务联系人邮箱
                    String produceConcatEmail = info.get("produce_concat_email");
                    maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_PROVIDER_CONT_EMAIL, produceConcatEmail);
                    //使用年限 固定是5年
                    maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_LIFE_PERIOD, "5");
                    //维护部门
                    maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_DEPT_OPERATION, "卓望运维团队");
                    // 资源池 和 设备分类
                    String idcType = info.get("idcType");
                    String deviceClass = info.get("device_class");
                    CmdbMaintenancePersonInfo personInfo = cmdbMaintenancePersonInfoMapper.getOne(idcType,deviceClass);
                    if (personInfo != null) {
                        //维护人员
                        maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_OPS, personInfo.getName());
                        //维护人员电话
                        maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_OPS_PHONE, personInfo.getPhone());
                        //维护人员邮箱
                        maintenInfoToApprove(instanceId, Constants.CI_MAINTEN_OPS_EMAIL, personInfo.getEmail());
                    }
                }
            }
        }
        return "同步结束";
    }

    /**
     * 对比维保信息, 生成维保审核数据
     * @param instanceId
     * @param filedCode
     * @param filedValue
     */
    private void maintenInfoToApprove(String instanceId, String filedCode, String filedValue) {
        // 先获取主机对应的模型
        CmdbInstance queryInstance = new CmdbInstance();
        queryInstance.setId(instanceId);
        CmdbInstance cmdbInstance = instanceService.get(queryInstance);
        if (cmdbInstance != null) {
            Module module = moduleService.getModuleDetail(cmdbInstance.getModuleId());
            if (module != null) {
                //String modelCode = module.getCode();
                // 获取完整设备详情
                Map<String, Object> instanceData = instanceService.getInstanceDetail(module.getId(),cmdbInstance.getId());
                if (instanceData != null && instanceData.containsKey(filedCode)) {
                    String instanceValue = String.valueOf(instanceData.get(filedCode));
                    if (!instanceValue.equals(filedValue)) {
                        CmdbCode queryCode = new CmdbCode();
                        queryCode.setFiledCode(filedCode);
                        // 获取码表ID
                        CmdbCode cmdbCode = codeService.get(queryCode);
                        if (cmdbCode != null) {
                            CmdbCollectApproval approval = new CmdbCollectApproval();
                            approval.setId(UUIDUtil.getUUID());
                            approval.setModuleId(module.getId());
//                            approval.setIp(instanceData.get("ip").toString());
                            approval.setInstanceId(instanceId);
//                            approval.setIdcType(instanceData.get("idcType").toString());
//                            approval.setDeviceType(instanceData.get("device_type") == null ? "" : instanceData.get("device_type").toString());
                            approval.setFiledName(cmdbCode.getFiledName());
                            approval.setCodeId(cmdbCode.getCodeId());
                            approval.setCurrValue(filedValue);
                            approval.setOldValue(instanceData.get(filedCode) == null ? "" : instanceData.get(filedCode).toString());
                            approval.setOperator("系统");
                            //approval.setApprovalStatus(1);
                            approval.setOperaterType("维保信息更新");
                            //approval.setApprovalUser("系统");
                            approval.setApprovalTime(new Date());
                            approval.setOperatorTime(new Date());
                            List<CmdbCollectApproval> approvalList = new ArrayList<>();
                            approvalList.add(approval);
                            approvalService.insert(approval);
                            approvalService.approve("系统",1,"",approvalList);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public List<Map<String, Object>> getMaintenObjByTimeAndSn(Map<String,String> mpValue) {
        return mapper.getMaintenObjByTimeAndSn(mpValue);
    }
}