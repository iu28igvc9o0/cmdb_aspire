package com.aspire.ums.cmdb.v2.process.evaluation;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceType;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceProduceService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbItDeviceCountService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbProduceAssessmentService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.NumberValidator;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AssessmentScoreImportFactory
 * Author:   luowenbo
 * Date:     2019/10/16 16:24
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
public class AssessmentScoreImportFactory extends ImportFactory {
    private ICmdbDeviceProduceService cmdbDeviceProduceService;
    private ICmdbItDeviceCountService cmdbItDeviceCountService;
    private ICmdbDeviceTypeService cmdbDeviceTypeService;
    private ICmdbProduceAssessmentService cmdbProduceAssessmentService;
    private String thisProvince;
    private String thisQuarter;
    // 设备类型
    private Map<String,String> deviceTypeIdMap;

    @Override
    public void initBasic() {
        super.initBasic();
        Map<String, Object> importData = super.getProcessImportData(super.getImportProcess().getProcessId());
        Map<String, String> processParams = (Map<String, String>) importData.get("processParams");
        thisProvince = processParams.get("province");
        thisQuarter = processParams.get("quarter");
        deviceTypeIdMap = getColumnMap();
    }

    /*
     *   获取厂家列表
     * */
    private List<String> getProduceNameList(){
        List<String> rsList = new ArrayList<>();
        List<CmdbDeviceProduce> produceList = this.cmdbDeviceProduceService.list();
        for(CmdbDeviceProduce item : produceList) {
            rsList.add(item.getName());
        }
        return rsList;
    }

    /*
     *  从数据库中获取设备类型
     * */
    private Map<String,String> getColumnMap() {
        Map<String,String> rs = new HashMap<>();
        List<CmdbDeviceType> columnList = this.cmdbDeviceTypeService.list();
        for(CmdbDeviceType item : columnList) {
            String preName = item.getName();
            if(!(item.getChild().isEmpty())) {
                for (CmdbDeviceType child : item.getChild()) {
                    String suffixName = child.getName();
                    if("台数".equals(suffixName)) {
                        rs.put(preName,child.getId());
                    }
                }
            }
        }
        return rs;
    }

    @Override
    public void initSpringBean() {
        if (cmdbItDeviceCountService == null) {
            cmdbItDeviceCountService = SpringUtils.getBean(ICmdbItDeviceCountService.class);
        }
        if (cmdbProduceAssessmentService == null) {
            cmdbProduceAssessmentService = SpringUtils.getBean(ICmdbProduceAssessmentService.class);
        }
        if (cmdbDeviceProduceService == null) {
            cmdbDeviceProduceService = SpringUtils.getBean(ICmdbDeviceProduceService.class);
        }
        if (cmdbDeviceTypeService == null) {
            cmdbDeviceTypeService = SpringUtils.getBean(ICmdbDeviceTypeService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        List<CmdbProduceAssessment> destList = new ArrayList<>();
        // 得分只允许的值
        List<Integer> numList = Arrays.asList(0,2,3,4,5);
        // 厂家名称
        String produceImport = dataMap.get("厂商名称[必填]");
        EmptyValidator.notEmpty("厂商名称[必填]", produceImport);
        DictValidator.validator("厂家名称[必填]",produceImport,getProduceNameList());
        // 设备类型
        String deviceType = dataMap.get("设备类型[必填]");
        EmptyValidator.notEmpty("设备类型[必填]", deviceType);
        DictValidator.validator("设备类型[必填]",deviceType, new ArrayList(deviceTypeIdMap.keySet()));
        CmdbItDeviceCount tmpObj = new CmdbItDeviceCount();
        tmpObj.setProduce(produceImport);
        tmpObj.setDeviceTypeId(deviceTypeIdMap.get(deviceType));
        tmpObj.setProvince(thisProvince);
        tmpObj.setQuarter(thisQuarter);
        // 根据厂家、分支机构、季度、设备类型 查询实体
        CmdbItDeviceCount instance = null;
        List<CmdbItDeviceCount> itDeviceCountList = this.cmdbItDeviceCountService.listByEntity(tmpObj);
        if(!itDeviceCountList.isEmpty()) {
            instance=itDeviceCountList.get(0);
        }
        if(instance == null) {
            throw new RuntimeException("[" +  produceImport + "]厂家目前没有[" + deviceType + "]设备" );
        }
        int flag = 0;
        CmdbProduceAssessment addObj = null;
        // 查询条件实体
        CmdbProduceAssessment selectObj = new CmdbProduceAssessment();
        for(String key: dataMap.keySet()) {
            if("厂商名称[必填]".equals(key) || "设备类型[必填]".equals(key)) {
                continue;
            }
            String columnValue = dataMap.get(key);
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            flag++;
            // flag = 1表示得分 || flag = 2 表示描述
            if(flag == 1) {
                addObj = new CmdbProduceAssessment();
                int bIndex = key.indexOf("[");
                if(bIndex == -1) {
                    continue;
                }
                addObj.setMetricName(key.substring(0,bIndex));
                selectObj.setMetricName(key.substring(0,bIndex));
                // 验证分数 值范围是list
                NumberValidator.validateWithList(key,columnValue,numList);
                addObj.setScore(Integer.parseInt(columnValue));
            } else if(flag == 2) {
                addObj.setDeviceCountId(instance.getId());
                selectObj.setDeviceCountId(instance.getId());
                addObj.setScoreDescription(columnValue);
                // 判断数据库是新增还是更新，更新就得获取cmdb_it_device_count的ID
                List<CmdbProduceAssessment> cmdbProduceAssessmentList = this.cmdbProduceAssessmentService.listByEntity(selectObj);
                if(!cmdbProduceAssessmentList.isEmpty()) {
                    addObj.setId(cmdbProduceAssessmentList.get(0).getId());
                }
                if (addObj != null) {
                    destList.add(addObj);
                }
                flag = 0;
            }
        }
        try {
            this.cmdbProduceAssessmentService.save(destList);
        } catch (Exception e) {
            throw new RuntimeException("新增信息失败:" + e.getMessage(), e);
        }
    }
}
