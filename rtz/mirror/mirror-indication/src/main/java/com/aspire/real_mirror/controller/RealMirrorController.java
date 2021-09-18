package com.aspire.real_mirror.controller;

import com.aspire.common.BaseController;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.service.IIndicationService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.real_mirror.IRealMirrorAPI;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RealMirrorController
 * Author:   zhu.juwang
 * Date:     2019/10/18 10:17
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class RealMirrorController extends BaseController<RealMirrorController> implements IRealMirrorAPI {
    @Autowired
    private IIndicationDataService dataService;
    @Autowired
    private IIndicationService indicationService;
    @Override
    public Object getIndicationList(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency) throws RuntimeException {
        return indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(indicationOwner, indicationCatalog, indicationFrequency, null, indicationPosition);
    }

    @Override
    public JSONObject getIndicationData(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency, String calcDate, String provinceCode, String period) throws RuntimeException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put("provinceCode", provinceCode);
        if (IndicationConst.INDICATION_FREQUENCY_MINUTE.equals(indicationFrequency)) {
            return dataService.getMinuteIndicationData(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency, calcDate);
        } else {
            return dataService.getExceptionData(indicationOwner, indicationCatalog, null, indicationFrequency, calcDate, null, provinceCode, indicationPosition);
        }
    }

    @Override
    public JSONObject getSendEmailContent(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency, String calcDate) throws RuntimeException {
        return null;
    }

    @Override
    public JSONArray reloadIndication() throws RuntimeException {
        return null;
    }

    @Override
    public void reCalcIndication(String indicationOwner, String indicationCatalog, String indicationPosition, String indicationFrequency, String beginDate, String endDate) throws RuntimeException {

    }

    @Override
    public void synchronizationLimits() {

    }
}
