package com.aspire.ums.cmdb.v2.process.resource;

import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceResp;
import com.aspire.ums.cmdb.deviceStatistic.service.InvalidResourceService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DateValidator;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/27
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class InvalidResImportFactory extends ImportFactory {

    private InvalidResourceService invalidResourceService;
    private List<Map<String, String>> poolList;

    @Override
    public void initBasic() {
        super.initBasic();
        poolList = getDictList("idcType", null, null, null);
    }

    @Override
    public void initSpringBean() {
        if (invalidResourceService == null) {
            invalidResourceService = SpringUtils.getBean(InvalidResourceService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        InvalidResourceResp invalidResource = new InvalidResourceResp();
        EmptyValidator.notEmpty("资源池", dataMap.get("资源池"));
        DictValidator.validator("资源池", dataMap.get("资源池"), this.poolList);
        invalidResource.setIdcType(dataMap.get("资源池"));
        invalidResource.setDepartment1(dataMap.get("一级部门"));
        invalidResource.setDepartment2(dataMap.get("二级部门"));
        invalidResource.setBizSystem(dataMap.get("业务系统"));
        invalidResource.setPodName(dataMap.get("POD名称"));

        try {
            Integer.parseInt(dataMap.get("物理计算资源(台)"));
        } catch (Exception e) {
            throw new RuntimeException("列[物理计算资源(台)] 数量输入有误, 请输入整数");
        }
        invalidResource.setPhysicalNumber(dataMap.get("物理计算资源(台)"));

        try {
            Integer.parseInt(dataMap.get("虚拟计算资源(台)"));
        } catch (Exception e) {
            throw new RuntimeException("列[虚拟计算资源(台)] 数量输入有误, 请输入整数");
        }
        invalidResource.setVirtualNumber(dataMap.get("虚拟计算资源(台)"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        invalidResource.setPlanTime(dateFormat.format(DateValidator.validate("计划资源回收/清理时间", dataMap.get("计划资源回收/清理时间"))));

        invalidResource.setRealityTime(dateFormat.format(DateValidator.validate("实际资源回收/清理时间", dataMap.get("实际资源回收/清理时间"))));
        List<InvalidResourceResp> resourceResps = new ArrayList<>();
        resourceResps.add(invalidResource);
        this.invalidResourceService.insertInvalidResource(resourceResps);
    }
}
