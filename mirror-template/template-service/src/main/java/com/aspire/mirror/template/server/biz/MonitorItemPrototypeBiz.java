package com.aspire.mirror.template.server.biz;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.model.ItemsDTO;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype.MonitorItemPrototypeQueryModel;
import com.aspire.mirror.template.api.dto.model.TriggersDTO;
import com.aspire.mirror.template.server.dao.MonitorItemPrototypeDao;

/** 
 *
 * 项目名称: template-service 
 * <p/>
 * 
 * 类名: MonitorItemPrototypeBiz
 * <p/>
 *
 * 类功能描述: 指标原型管理业务类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年1月27日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Service
@Transactional
public class MonitorItemPrototypeBiz {
	@Autowired
	private MonitorItemPrototypeDao	dao;

	@Autowired
	private ItemsBiz				itemsBiz;

	@Autowired
	private TriggersBiz				triggerBiz;
	
	public GeneralResponse saveItemPrototype(MonitorItemPrototype prototype) {
		try {
			Pair<Boolean, String> checkResult = checkData(prototype);
			if (!checkResult.getLeft()) {
				return new GeneralResponse(false, checkResult.getRight());
			}
			if (prototype.getId() != null) {
				prototype.setUpdateTime(new Date());
				dao.updateItemPrototype(prototype);
			} else {
				prototype.setCreateTime(new Date());
				prototype.setUpdateTime(prototype.getCreateTime());
				dao.insertItemPrototype(prototype);
			}
		} catch (Exception e) {
			return new GeneralResponse(false, e.getMessage());
		}
		return new GeneralResponse(true, null, prototype);
	}
	
	/** 
	 * 功能描述: 检查数据  
	 * <p>
	 * @param prototype
	 * @return
	 */
	private Pair<Boolean, String> checkData(MonitorItemPrototype prototype) {
		if (StringUtils.isBlank(prototype.getPrototypeLabel())) {
			return Pair.of(false, "The value of prototypeLabel can't be null.");
		}
		if (StringUtils.isBlank(prototype.getItemName())) {
			return Pair.of(false, "The value of itemName can't be null.");
		}
		return Pair.of(true, null);
	}
	
	public GeneralResponse removeItemPrototype(Long id) {
		try {
			dao.deleteItemPrototypeById(id);
		} catch (Exception e) {
			return new GeneralResponse(false, e.getMessage());
		}
		return new GeneralResponse();
	}
	
	public GeneralResponse batchRemoveItemPrototype(List<Long> idList) {
		try {
			dao.batchDeleteItemPrototypeById(idList);
		} catch (Exception e) {
			return new GeneralResponse(false, e.getMessage());
		}
		return new GeneralResponse();
	}
	
	@Transactional(readOnly=true, isolation=Isolation.REPEATABLE_READ)
	public PageResponse<MonitorItemPrototype> queryItemPrototypeList(MonitorItemPrototypeQueryModel queryParams) {
		PageResponse<MonitorItemPrototype> resp = new PageResponse<>();
		List<MonitorItemPrototype> resultList = dao.queryItemPrototypeList(queryParams);
		Integer totalCount = CollectionUtils.isEmpty(resultList) ? 0 : dao.queryTotalItemPrototypeListCount(queryParams); 
		resp.setCount(totalCount);
		resp.setResult(resultList);
		return resp;
	}
	
	/** 
	 * 功能描述: 同步指标原型的配置到所有指标实例  
	 * <p>
	 * @param id
	 * @return
	 */
	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public GeneralResponse syncRefreshItemsConfigByItemPrototype(Long id) {
		MonitorItemPrototype prototype = dao.queryItemPrototypeById(id);
		if (prototype == null) {
			return new GeneralResponse(false, "There is no item prototype with id: " + id);
		}
		
		ItemsDTO param = new ItemsDTO();
		param.setPrototypeId(id);
		List<ItemsDTO> applyItemList = itemsBiz.select(param);
		for (ItemsDTO updateData : applyItemList) {
			updateData.setName(prototype.getItemName());
			updateData.setKey(prototype.getScriptId());
			updateData.setBizGroup(prototype.getScriptGroupName());
			updateData.setItemGroup(prototype.getPriority());
			updateData.setItemExt(prototype.getItemExt());
			itemsBiz.updateByPrimaryKey(updateData);
			
			TriggersDTO updateTriggerData = new TriggersDTO();
			updateTriggerData.setItemId(updateData.getItemId());
			updateTriggerData.setExpression(prototype.getExpression() + prototype.getDefaultThresholdVal());
			updateTriggerData.setName(updateData.getName());
			updateTriggerData.setPriority("5");
			updateTriggerData.setStatus("ON");
			updateTriggerData.setUrl("");
			updateTriggerData.setParam("");
			
			TriggersDTO triggerParam = new TriggersDTO();
			// triggerParam.setPriority("5");
			triggerParam.setItemId(updateData.getItemId());
			// 监控项和触发器是一对多，但在创建巡检模板时，一个指标下只有一个触发器，故此处只更新第一条
			List<TriggersDTO> triggerList = triggerBiz.select(triggerParam);
			if (CollectionUtils.isEmpty(triggerList)) {
				triggerBiz.insertByBatch(Collections.singletonList(updateTriggerData));
			} else {
				updateTriggerData.setTriggerId(triggerList.get(0).getTriggerId());
				triggerBiz.updateByPrimaryKey(updateTriggerData);
			}
		}
		return new GeneralResponse();
	}
}
