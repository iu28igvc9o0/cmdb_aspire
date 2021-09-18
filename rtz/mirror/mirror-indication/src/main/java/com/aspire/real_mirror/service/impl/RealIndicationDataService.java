package com.aspire.real_mirror.service.impl;

import com.aspire.mirror.dao.IIndicationDAO;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.TimeUtil;
import com.aspire.real_mirror.service.IRealIndicationDataService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RealIndicationDataService
 * Author:   zhu.juwang
 * Date:     2019/11/11 10:11
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class RealIndicationDataService implements IRealIndicationDataService {

    @Autowired
    private IIndicationDAO indicationDAO;
    @Autowired
    private IIndicationDataService dataService;

    @Override
    public JSONObject getDayIndicationMailData(String indicationOwner, String indicationCatalog, String indicationPosition,
                                        String indicationFrequency, String calcDate) {
        List<IndicationEntity> entityList = indicationDAO.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(indicationOwner, indicationCatalog,
                indicationFrequency, null, indicationPosition);
        JSONObject returnObject = new JSONObject();
        if (entityList.size() == 0) {
            return returnObject;
        }
        calcDate = TimeUtil.clearDateStrPattern(calcDate);
        //获取指标计算结果数据
        for (IndicationEntity entity : entityList) {
            String indicationName = entity.getIndicationName().substring(2);
            JSONObject exceptionData = new JSONObject();
            if (returnObject.get(indicationName) != null) {
                exceptionData = returnObject.getJSONObject(indicationName);
            }
            List<Map<String, String>> exceptionList = new LinkedList<Map<String, String>>();
            List<Map<String, String>> dataList = dataService.getExceptionDataByIndicationIdAndProvinceCode(calcDate, null,
                    entity.getIndicationId(), null);
            if (dataList != null && dataList.size() > 0) {
                for (Map<String, String> dataEntity : dataList) {
                    if (StringUtils.isNotEmpty(dataEntity.get(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                        exceptionList.add(dataEntity);
                    }
                }
            }
            if (entity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_COUNTRY)) {
                JSONObject countryJson = new JSONObject();
                countryJson.put("indication", entity);
                countryJson.put("data", exceptionList);
                exceptionData.put(IndicationConst.INDICATION_GROUP_COUNTRY, countryJson);
            }
            if (entity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_PROVINCE)) {
                JSONObject provinceJson = new JSONObject();
                provinceJson.put("indication", entity);
                provinceJson.put("data", exceptionList);
                exceptionData.put(IndicationConst.INDICATION_GROUP_PROVINCE, provinceJson);
            }
            returnObject.put(indicationName, exceptionData);
        }
        return returnObject;
    }
}
