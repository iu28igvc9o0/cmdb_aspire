package com.aspire.mirror.controller;

import java.util.List;

import com.aspire.common.BaseController;
import com.aspire.mirror.bean.IndicationLimitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.IGatewayAPI;
import com.aspire.mirror.bean.Result;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationLimitService;
import com.aspire.mirror.service.IIndicationService;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.controller.MirrorController
 */
@RestController
@Slf4j
public class GatewayController extends BaseController<GatewayController> implements IGatewayAPI {

    @Autowired
    private IIndicationService indicationService;
    @Autowired
    private IIndicationLimitService iIndicationLimitService;
	@Override
	public String getGatewayIndicationData(String indicationOwner, String catalogBox, String indicationFrequency, String group) {
		 List<IndicationEntity> gatewayList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
				 indicationOwner,catalogBox, indicationFrequency, group, null);
		 return JSONArray.fromObject(gatewayList).toString();
	}
	
	
	@Override
	public Result saveGatewayMerit(@RequestBody List<IndicationLimitEntity> limitList){
		//LimitEntityReq req = (LimitEntityReq)JSONObject.toBean(meritsLimits, LimitEntityReq.class);
		iIndicationLimitService.saveMerits(limitList);
		return Result.success();
	}
	
	 
}
