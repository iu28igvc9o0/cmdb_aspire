package com.aspire.ums.cmdb.resource.web;

import com.aspire.ums.cmdb.resource.entity.ComboBox;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimate;
import com.aspire.ums.cmdb.resource.mapper.ResourceHighChartMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/resource/ResourceHighChart")
@Api("资源 HighChart 图表数据接口")
public class ResourceHighChartController {

	private static final Logger logger = Logger.getLogger(ResourceHighChartController.class);

	@Autowired
	private ResourceHighChartMapper resourceHighChartMapper;


	@RequestMapping(value = "/getResourcePool")
	@ResponseBody
	@ApiOperation(value = "获取资源池数据", notes = "获取资源池数据")
	public List<ComboBox> getResourceEstimate() {
		// 服务器类型分配总览
		// boolean select = true;
		List<ComboBox> res = new ArrayList<>();
		// 获取资源预估
		List<Map<String, String>> selectList = resourceHighChartMapper.getResourcePool();
		ComboBox resZon = new ComboBox();
		resZon.setText("总览");
		resZon.setSelected(true);
		res.add(resZon);
		if (selectList.size() > 0) {
			for (Map<String, String> map : selectList) {
				ComboBox response = new ComboBox();
				/*
				 * if (select) { response.setSelected(true); select = false; }
				 * else { response.setSelected(false); }
				 */
				response.setId(String.valueOf(map.get("id")));
				response.setText(map.get("text"));
				res.add(response);
			}
		}
		return res;
	}

	@RequestMapping(value = "/getOverviewData", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取资源总览数据", notes = "获取资源总览数据")
	public Map<String, Object> getOverviewData(@RequestBody ResourceEstimate param) {
		String remark = param.getRemark();
		Map<String, Object> returnData = getZLData(remark);
		return returnData;
	}

	@RequestMapping(value = "/getServerOverviewData", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取服务器总览数据", notes = "获取服务器总览数据")
	public Map<String, Object> getServerData(@RequestBody ResourceEstimate param) {
		String pool = param.getResource_pool();
		// 当期剩余数量
		int currpersyCount = resourceHighChartMapper.getServerOverview(pool, "currpersy", "server");
		// 当期已分配数量
		int currperyfpCount = resourceHighChartMapper.getServerOverview(pool, "currperyfp", "server");
		// 设备表服务器总数
		int serverCount = resourceHighChartMapper.getServerCount("percount", "server");
		// 当期服务器数量
		int currperCount = resourceHighChartMapper.getServerOverview(pool, "currpercount", "server");
		// 往期数量
		int perCount = serverCount - currperCount;
		// 不可分配
		int nodiscount = resourceHighChartMapper.getServerCount("nodiscount", "server");
		// 未来规划
		String futureplan = resourceHighChartMapper.getFutureplanServerCount();
		int futureplanCount = 0;
		if (null != futureplan) {
			futureplanCount = Integer.valueOf(futureplan);
		}
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("currpersyCount", currpersyCount);
		returnData.put("currperyfpCount", currperyfpCount);
		returnData.put("perCount", perCount);
		returnData.put("nodiscount", nodiscount);
		returnData.put("futureplanCount", futureplanCount);
		logger.info("getServerOverviewData :" + returnData.toString());
		return returnData;
	}

	@RequestMapping(value = "/getStorageOverviewData", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询存储总览数据", notes = "查询存储总览数据")
	public Map<String, Object> getStorageData(@RequestBody ResourceEstimate param) {
		String pool = param.getResource_pool();
		// 当期剩余数量
		int currpersyCount = resourceHighChartMapper.getServerOverview(pool, "currpersy", "storage");
		// 当期已分配数量
		int currperyfpCount = resourceHighChartMapper.getServerOverview(pool, "currperyfp", "storage");
		// 设备表服务器总数
		int serverCount = resourceHighChartMapper.getServerCount("percount", "storage");
		// 当期服务器数量
		int currperCount = resourceHighChartMapper.getServerOverview(pool, "currpercount", "storage");
		// 往期数量
		int perCount = serverCount - currperCount;
		// 不可分配
		int nodiscount = resourceHighChartMapper.getServerCount("nodiscount", "storage");
		// 未来规划
		String futureplan = resourceHighChartMapper.getFutureplanStorageCount();
		Double futureplanCount = 0.0d;
		if (null != futureplan) {
			futureplanCount = Double.valueOf(futureplan);
		}
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put("currpersyCount", currpersyCount);
		returnData.put("currperyfpCount", currperyfpCount);
		returnData.put("perCount", perCount);
		returnData.put("nodiscount", nodiscount);
		returnData.put("futureplanCount", futureplanCount);
		logger.info("getStorageOverviewData :" + returnData.toString());
		return returnData;
	}

	@RequestMapping(value = "/getServerEstimateData", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "查询服务器预估数据", notes = "查询服务器预估数据")
	public List<Map<String, Object>> getServerEstimateData(@RequestBody ResourceEstimate param) {
		// 服务器类型分配总览
		String pool = param.getResource_pool();
		List<Map<String, Object>> countyList = resourceHighChartMapper.getServerEstimateCount(pool, "服务器");
		List<Map<String, Object>> countwList = resourceHighChartMapper.getServerEstimateAssignCount(pool, "服务器");
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<String> keys = new ArrayList<String>();
		if (countwList.size() > 0 && countyList.size() > 0) {
			for (Map<String, Object> county : countyList) {
				for (Map.Entry<String, Object> entryy : county.entrySet()) {
					if (entryy.getKey().equals("SERVER_TYPE_1") && !keys.contains(entryy.getValue())) {
						keys.add(entryy.getValue().toString());
					}
				}
			}
			for (String k : keys) {
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("SERVER_TYPE_1", k);
				for (Map<String, Object> county : countyList) {
					for (Map.Entry<String, Object> enyy : county.entrySet()) {
						if (enyy.getKey().equals("SERVER_TYPE_1") && enyy.getValue().equals(k)) {
							temp.put("assign", county.get("assign"));
						}
					}
				}
				for (Map<String, Object> countw : countwList) {
					for (Map.Entry<String, Object> enww : countw.entrySet()) {
						if (enww.getKey().equals("SERVER_TYPE_1") && enww.getValue().equals(k)) {
							temp.put("unassign", countw.get("unassign"));
						}
					}
				}
				returnList.add(temp);
			}
		}
		logger.info("getServerEstimateData :" + returnList.toString());
		return returnList;
	}

	@RequestMapping(value = "/getStorageEstimateData", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取存储预估数据", notes = "获取存储预估数据")
	public List<Map<String, Object>> getStorageEstimateData(@RequestBody ResourceEstimate param) {
		// 存储类型分配总览
		String pool = param.getResource_pool();
		List<Map<String, Object>> countyList = resourceHighChartMapper.getServerEstimateCount(pool, "存储设备");
		List<Map<String, Object>> countwList = resourceHighChartMapper.getServerEstimateAssignCount(pool, "存储设备");
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		List<String> keys = new ArrayList<String>();
		if (countwList.size() > 0 && countyList.size() > 0) {
			for (Map<String, Object> county : countyList) {
				for (Map.Entry<String, Object> entryy : county.entrySet()) {
					if (entryy.getKey().equals("SERVER_TYPE_1") && !keys.contains(entryy.getValue())) {
						keys.add(entryy.getValue().toString());
					}
				}
			}
			for (String k : keys) {
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("SERVER_TYPE_1", k);
				for (Map<String, Object> county : countyList) {
					for (Map.Entry<String, Object> enyy : county.entrySet()) {
						if (enyy.getKey().equals("SERVER_TYPE_1") && enyy.getValue().equals(k)) {
							temp.put("assign", county.get("assign"));
						}
					}
				}
				for (Map<String, Object> countw : countwList) {
					for (Map.Entry<String, Object> enww : countw.entrySet()) {
						if (enww.getKey().equals("SERVER_TYPE_1") && enww.getValue().equals(k)) {
							temp.put("unassign", countw.get("unassign"));
						}
					}
				}
				returnList.add(temp);
			}
		}
		logger.info("getStorageEstimateData :" + returnList.toString());
		return returnList;
	}
	
	private Map<String, Object> getZLData(String param){
		Map<String, Object> returnData = new HashMap<String, Object>();
		int yfpCount = resourceHighChartMapper.getServerCount("yfpcount", param);
		int syCount = resourceHighChartMapper.getServerCount("sycount", param);
		int buildCount = resourceHighChartMapper.getBuildOverview(param);
		int collectCount = resourceHighChartMapper.getCollectOverview(param);
		logger.info("yfpCount :" + yfpCount + ",syCount :" + syCount + ",buildCount :" + buildCount
				+ ",collectCount" + collectCount);
		returnData.put("yfpCount", yfpCount);
		returnData.put("syCount", syCount);
		returnData.put("buildCount", buildCount);
		returnData.put("collectCount", collectCount);
		return returnData;
	}

}
