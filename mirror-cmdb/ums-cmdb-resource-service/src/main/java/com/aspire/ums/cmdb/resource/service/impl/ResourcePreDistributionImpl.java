package com.aspire.ums.cmdb.resource.service.impl;

import com.aspire.ums.cmdb.resource.entity.ResourceDistributionRecord;
import com.aspire.ums.cmdb.resource.entity.ResourcePreDistribution;
import com.aspire.ums.cmdb.resource.mapper.ResourceDistributionRecordMapper;
import com.aspire.ums.cmdb.resource.mapper.ResourcePreDistributionMapper;
import com.aspire.ums.cmdb.resource.service.ResourcePreDistributionService;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.birp.modules.osa.service.impl
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2018/9/26 17:12
 * 版本: v1.0
 */
@Service
public class ResourcePreDistributionImpl implements ResourcePreDistributionService {
	
	private static Logger log = Logger.getLogger(ResourcePreDistributionImpl.class);

	@Autowired
	private ResourceDistributionRecordMapper resourceDistributionRecordMapper;
	
	@Autowired
	private ResourcePreDistributionMapper resourcePreDistributionMapper;
	
	

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int assertDevice(Map<String, Object> params, String username) throws Exception {
		List<ResourceDistributionRecord> relList = new ArrayList<>();
		List<Map<String,Object>> deviceData= (List<Map<String,Object>>)params.get("data");
		String preId = params.get("preId").toString();
		List<Map<String,Object>> deviceList = new ArrayList<>(); 
//		User user = UserUtils.getUser();
		
		Map<String, Object> preParams = Maps.newHashMap();
		preParams.put("id", preId);
		preParams.put("status", "1");
//		preParams.put("update_id", user.getLoginName());
		preParams.put("update_id", username);
		int preResult = resourcePreDistributionMapper.updateStatus(preParams);
		if(preResult ==0) {
			throw new Exception("1");
			// return 1;
		}
		for(Map<String,Object> r:deviceData) {
			ResourceDistributionRecord rel = new  ResourceDistributionRecord();
			
			rel.setPre_distribute_id(Integer.parseInt(preId));
			rel.setDevice_id( r.get("ID").toString());
			rel.setDevice_ip( r.get("DEVICE_IP").toString());
			rel.setOperate_content("资源预分配");
//			rel.setOperate_id(user.getLoginName());
			rel.setOperate_id(username);
			rel.setOperate_time(new Date());
			relList.add(rel);
			
			Map<String,Object> device = Maps.newHashMap();
			device.put("id",  r.get("ID"));
			device.put("is_assign",  "2");
			deviceList.add(device);
			
		}
		// int recodeResult = resourceDistributionRecordDao.insertBatch(relList);
		int deviceResult = resourcePreDistributionMapper.batchUpdateDeviceAssets(deviceList);
		if(deviceResult == deviceData.size()) {
			int recodeResult = resourceDistributionRecordMapper.insertBatch(relList);
			if(recodeResult == deviceResult) {
				return recodeResult;
			}else {
				throw new Exception("3");
			}
		}else {
			throw new Exception("2");
		}
	}

	@Override
	public int getResourcePreDistributionCount(Map<String, Object> params) {
		return resourcePreDistributionMapper.getResourcePreDistributionCount(params);
	}

	@Override
	public List<Map<String, String>> getResourcePreDistributionList(Map<String, Object> params) {
		return resourcePreDistributionMapper.getResourcePreDistributionList(params);
	}

	@Override
	public int saveResourcePreDistributionExcelData(List<ResourcePreDistribution> resourcePreDistributionList) {
		return resourcePreDistributionMapper.saveResourcePreDistributionExcelData(resourcePreDistributionList);
	}
}
