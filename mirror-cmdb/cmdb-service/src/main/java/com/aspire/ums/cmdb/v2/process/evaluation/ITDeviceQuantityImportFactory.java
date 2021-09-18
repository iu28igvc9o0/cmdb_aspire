package com.aspire.ums.cmdb.v2.process.evaluation;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceType;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceProduceService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceTypeService;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbItDeviceCountService;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.NumberValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ITDeviceQuantityImportFactory
 * Author:   luowenbo
 * Date:     2019/10/12 16:47
 * Description:  IT设备量信息导入工厂类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ITDeviceQuantityImportFactory extends ImportFactory {
    private ICmdbDeviceProduceService cmdbDeviceProduceService;
    private ICmdbItDeviceCountService cmdbItDeviceCountService;
    private ICmdbDeviceTypeService cmdbDeviceTypeService;
    private String thisProvince;
    private String thisCreateUsername;
    private String thisCreateUserPhone;
    private String thisQuarter;
    private Map<String,String> deviceTypeIdMap;
    private List<String> produceList;

    @Override
    public void initBasic() {
        super.initBasic();
        Map<String, Object> importData = super.getProcessImportData(super.getImportProcess().getProcessId());
        Map<String, String> processParams = (Map<String, String>) importData.get("processParams");
        thisProvince = processParams.get("province");
        thisCreateUsername = processParams.get("createUsername");
        thisCreateUserPhone = processParams.get("createUserPhone");
        thisQuarter = processParams.get("quarter");
        deviceTypeIdMap = getColumnMap();
        produceList = getProduceNameList();
    }

    @Override
    public void formatErrorReason(Map<String, Object> errorLineData) {
        super.formatErrorReason(errorLineData);
        errorLineData.remove("create_username");
        errorLineData.remove("create_user_phone");
        errorLineData.remove("quarter");
    }

    /*
    *   从list中查询具体的值
    * */
    private CmdbItDeviceCount getSpecificValue(List<CmdbItDeviceCount> data,String deviceTypeId) {
        for(CmdbItDeviceCount item: data) {
            if(deviceTypeId.equals(item.getDeviceTypeId())) {
                return item;
            }
        }
        return null;
    }

    /*
    *   获取存在数据库中的厂家名称
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
    *  从数据库中获取可能存在的列名称
    * */
    private Map<String,String> getColumnMap() {
        Map<String,String> rs = new HashMap<>();
        List<CmdbDeviceType> columnList = this.cmdbDeviceTypeService.list();
        for(CmdbDeviceType item : columnList) {
            String preName = item.getName();
            if(!(item.getChild().isEmpty())) {
                for (CmdbDeviceType child : item.getChild()) {
                    String suffixName = child.getName();
                    rs.put(preName + "[" + suffixName + "]",child.getId());
                }
            }
        }
        return rs;
    }

    @Override
    public void initSpringBean() {
        if (cmdbDeviceProduceService == null) {
            cmdbDeviceProduceService = SpringUtils.getBean(ICmdbDeviceProduceService.class);
        }
        if (cmdbItDeviceCountService == null) {
            cmdbItDeviceCountService = SpringUtils.getBean(ICmdbItDeviceCountService.class);
        }
        if (cmdbDeviceTypeService == null) {
            cmdbDeviceTypeService = SpringUtils.getBean(ICmdbDeviceTypeService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        List<CmdbItDeviceCount> desList = new ArrayList<>();
        // 厂家名称
        String produceImport = dataMap.get("厂家名称[必填]");
        EmptyValidator.notEmpty("厂家名称[必填]",  dataMap.get("厂家名称[必填]"));
        DictValidator.validator("厂家名称[必填]", produceImport, this.produceList);
        // 每个实体都是一样的数据，厂家、分支机构、季度
        CmdbItDeviceCount tmpObj = new CmdbItDeviceCount();
        tmpObj.setProduce(produceImport);
        tmpObj.setProvince(thisProvince);
        tmpObj.setQuarter(thisQuarter);
        // 根据厂家和分支机构查询实体
        List<CmdbItDeviceCount> sourceList = this.cmdbItDeviceCountService.listByEntity(tmpObj);
        // 判断是更新还是新增
        for(String key: dataMap.keySet()) {
            String columnValue = dataMap.get(key);
            int bIndex = key.indexOf("[");
            int eIndex = key.indexOf("]");
            if(bIndex == -1) {
                continue;
            }
            String numType = key.substring(bIndex+1,eIndex);
            if("台数".equals(numType)) {
                if(columnValue != null && !("".equals(columnValue))) {
                    NumberValidator.validateWithScope(key,columnValue,0,999999);
                }
            } else if("容量(TB)".equals(numType)) {
                if(columnValue != null && !("".equals(columnValue))) {
                    NumberValidator.validatePositive(key,columnValue);
                }
            } else {
                continue;
            }
            if(!deviceTypeIdMap.containsKey(key)) {
                throw new RuntimeException("列[" + key + "]不在取值范围中");
            }
            CmdbItDeviceCount dealObj = getSpecificValue(sourceList, deviceTypeIdMap.get(key));
            if(dealObj == null) {
                if(!("".equals(columnValue))) {
                    dealObj = new CmdbItDeviceCount();
                    dealObj.setDeviceTypeId(deviceTypeIdMap.get(key));
                    dealObj.setProduce(produceImport);
                    dealObj.setProvince(thisProvince);
                    dealObj.setQuarter(thisQuarter);
                } else {
                    continue;
                }
            }
            dealObj.setCount(columnValue);
            dealObj.setCreateUsername(thisCreateUsername);
            dealObj.setCreateUserPhone(thisCreateUserPhone);
            desList.add(dealObj);
        }
        try {
            this.cmdbItDeviceCountService.insert(desList);
        } catch (Exception e) {
            throw new RuntimeException("新增信息失败:" + e.getMessage(), e);
        }
    }
}
