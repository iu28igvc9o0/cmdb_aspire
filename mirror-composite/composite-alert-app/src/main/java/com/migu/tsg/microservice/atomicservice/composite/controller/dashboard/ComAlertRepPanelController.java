package com.migu.tsg.microservice.atomicservice.composite.controller.dashboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.aspire.mirror.alert.api.dto.dashboard.AlertDeviceItemInfo;
import com.aspire.mirror.alert.api.dto.dashboard.AlertRepPanelDTO;
import com.aspire.mirror.composite.payload.alert.ComAlertDeviceItemInfo;
import com.aspire.mirror.composite.payload.dashboard.*;
import com.aspire.mirror.composite.service.dashboard.IComAlertRepPanelService;
import com.aspire.mirror.elasticsearch.api.dto.*;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.dashboard.AlertRepPanelClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.dashboard.DashboardEsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.zabbix.ZabbixItemServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import net.sf.json.util.JSONUtils;

/**
 * 历史告警服务
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RestController
public class ComAlertRepPanelController  implements IComAlertRepPanelService {

	 private Logger logger = LoggerFactory.getLogger(ComAlertRepPanelController.class);

	    @Autowired
	    private AlertRepPanelClient AlertRepPanelClient;
	    
	    @Autowired
	    private DashboardEsServiceClient dashboardEsServiceClient;
	    
	    @Autowired
	    private ZabbixItemServiceClient zabbixItemServiceClient;

		@Override
		public ComAlertRepPanelDTO findByPrimaryKey(@PathVariable String id) {
			 logger.info("method[findByPrimaryKey] id is {}", id);
			 if (StringUtils.isEmpty(id)) {
		            logger.warn("ComAlertRepPanelController method[findByPrimaryKey] param id is empty");
		            return null;
		        }
			 AlertRepPanelDTO p = AlertRepPanelClient.findByPrimaryKey(id);
			 ComAlertRepPanelDTO panel = PayloadParseUtil.jacksonBaseParse(ComAlertRepPanelDTO.class,p);
			return panel;
		}

		@Override
		public ComAlertRepPanelDTO findByName(@PathVariable String name) {
			 logger.info("method[findByName] id is {}", name);
			 if (StringUtils.isEmpty(name)) {
		            logger.warn("ComAlertRepPanelController method[findByName] param name is empty");
		            return null;
		        }
			 AlertRepPanelDTO p = AlertRepPanelClient.findByName(name);
			 ComAlertRepPanelDTO panel = PayloadParseUtil.jacksonBaseParse(ComAlertRepPanelDTO.class,p);
			return panel;
		}

		@Override
		public ComAlertRepPanelDTO create(@RequestBody ComAlertRepPanelDTO panel) throws Exception {
			RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
			if (panel == null) {
				logger.error("create panel is null");
				throw new RuntimeException("create panel is null");
			}
			panel.setCreater(authCtx.getUser().getUsername());
			logger.info("[create]panel is {}.", JSONUtils.valueToString(panel));
			
			AlertRepPanelDTO p = PayloadParseUtil.jacksonBaseParse(AlertRepPanelDTO.class, panel);
			AlertRepPanelDTO rs = AlertRepPanelClient.create(p);
			return PayloadParseUtil.jacksonBaseParse(ComAlertRepPanelDTO.class, rs);
		}

		@Override
		public ResponseEntity<String> update(@RequestBody ComAlertRepPanelDTO panel) throws Exception {
			RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
			if (panel == null) {
				logger.error("create panel is null");
				throw new RuntimeException("update panel is null");
			}
			panel.setEditer(authCtx.getUser().getUsername());
			logger.info("[update]panel is {}.", JSONUtils.valueToString(panel));
			
			AlertRepPanelDTO p = PayloadParseUtil.jacksonBaseParse(AlertRepPanelDTO.class, panel);
			return AlertRepPanelClient.update(p);
		}

		@Override
		public ResponseEntity<String> delete(@PathVariable String id) throws Exception {
			 logger.info("method[delete] id is {}", id);
			 if (StringUtils.isEmpty(id)) {
		            logger.warn("ComAlertRepPanelController method[delete] param id is empty");
		            return null;
		        }
			return AlertRepPanelClient.delete(id);
		}

		@Override
		public List<ComAlertRepPanelDTO> getAllPanel() {
			List<AlertRepPanelDTO> rs = AlertRepPanelClient.getAllPanel();
			return  PayloadParseUtil.jacksonBaseParse(ComAlertRepPanelDTO.class, rs);
		}

		@Override
		public ComAlertDashboardResponse getEsData(@RequestBody ComAlertDataSetsDto dataSetsDto) throws Exception {
			if (dataSetsDto == null) {
				logger.error("getEsData dataSetsDto is null");
				throw new RuntimeException("getEsData dataSetsDto is null");
			}
			logger.info("[getEsData]dataSetsDto is {}.", JSONUtils.valueToString(dataSetsDto));
			
			DataSetsDto p = PayloadParseUtil.jacksonBaseParse(DataSetsDto.class, dataSetsDto);
			DashboardResponse rs = dashboardEsServiceClient.getDashboardJsonList(p);
			
			return PayloadParseUtil.jacksonBaseParse(ComAlertDashboardResponse.class, rs);
		}

	@Override
	public PageResponse<ComAlertMoniterItemsResponse> getMoniterItems(@RequestBody ComAlertDeviceIpRequst comAlertDeviceIpRequst)  {
		if (comAlertDeviceIpRequst == null) {
			logger.error("getMoniterItem comAlertDeviceIpRequst is null");
			throw new RuntimeException("getMoniterItem comAlertDeviceIpRequst is null");
		}
		logger.info("[getMoniterItem]comAlertDeviceIpRequst is {}.", JSONUtils.valueToString(comAlertDeviceIpRequst));
		Map<String, Set<ItemIndexDto>> moniterItems = null;
		ItemRequest req = new ItemRequest();
		if(comAlertDeviceIpRequst.getReType() != 6) {
			if(comAlertDeviceIpRequst.getReType()==1) {
				req.setBizSystem(comAlertDeviceIpRequst.getRs());
			} else if(comAlertDeviceIpRequst.getReType()==2) {
				req.setIdcType(comAlertDeviceIpRequst.getRs());
			}else if(comAlertDeviceIpRequst.getReType()==3) {
				req.setRoomId(comAlertDeviceIpRequst.getRs());
			}else if(comAlertDeviceIpRequst.getReType()==4) {
				req.setDeviceClass(comAlertDeviceIpRequst.getRs());
			}else if(comAlertDeviceIpRequst.getReType()==5) {
				req.setDeviceType(comAlertDeviceIpRequst.getRs());
			}
			moniterItems = zabbixItemServiceClient.queryObject(req);
		}
		else {
			String[] strings = new String[comAlertDeviceIpRequst.getIp().size()];
			comAlertDeviceIpRequst.getIp().toArray(strings);
			req.setIps(strings);
			moniterItems = zabbixItemServiceClient.queryObject(req);
		}
		PageResponse<ComAlertMoniterItemsResponse> result = new PageResponse<ComAlertMoniterItemsResponse>();
		if(null != moniterItems) {
			List<ComAlertMoniterItemsResponse> moniterList = Lists.newArrayList();
			for(Map.Entry<String, Set<ItemIndexDto>> entry: moniterItems.entrySet())
			{
				ComAlertMoniterItemsResponse mo = new ComAlertMoniterItemsResponse();
				mo.setName(entry.getKey());
				Set<ItemIndexDto> itemSet = entry.getValue();
				Set<ComItemIndexDto> ci = new HashSet<>();
				for(ItemIndexDto i:itemSet) {
					ci.add(PayloadParseUtil.jacksonBaseParse(ComItemIndexDto.class, i));
				}
				mo.setMoniterItem(ci);
				moniterList.add(mo);

			}
			result.setResult(moniterList);
		}
		return result;
	}


    @Override
    public List<String> getMoniObject(@RequestBody ComAlertDeviceIpRequst comAlertDeviceIpRequst) {
        if (comAlertDeviceIpRequst == null) {
            logger.error("getMoniObject comAlertDeviceIpRequst is null");
            throw new RuntimeException("getMoniObject comAlertDeviceIpRequst is null");
        }
        logger.info("[getMoniObject]comAlertDeviceIpRequst is {}.", JSONUtils.valueToString(comAlertDeviceIpRequst));
        ItemRequest req = new ItemRequest();
        if(comAlertDeviceIpRequst.getReType() != 6) {
            if(comAlertDeviceIpRequst.getReType()==1) {
                req.setBizSystem(comAlertDeviceIpRequst.getRs());
            } else if(comAlertDeviceIpRequst.getReType()==2) {
                req.setIdcType(comAlertDeviceIpRequst.getRs());
            }else if(comAlertDeviceIpRequst.getReType()==3) {
                req.setRoomId(comAlertDeviceIpRequst.getRs());
            }else if(comAlertDeviceIpRequst.getReType()==4) {
                req.setDeviceClass(comAlertDeviceIpRequst.getRs());
            }else if(comAlertDeviceIpRequst.getReType()==5) {
                req.setDeviceType(comAlertDeviceIpRequst.getRs());
            }
        } else {
            String[] strings = new String[comAlertDeviceIpRequst.getIp().size()];
            comAlertDeviceIpRequst.getIp().toArray(strings);
            req.setIps(strings);
        }
        List<String> result = zabbixItemServiceClient.queryMoniObjects(req);
        return result;
    }
    
    @Override
    public ComMonitorKeyDto getMoniObjectList(@RequestBody ComAlertDeviceIpRequst comAlertDeviceIpRequst) {
        if (comAlertDeviceIpRequst == null) {
            logger.error("getMoniObjectList comAlertDeviceIpRequst is null");
            throw new RuntimeException("getMoniObject comAlertDeviceIpRequst is null");
        }
        logger.info("[getMoniObjectList]comAlertDeviceIpRequst is {}.", JSONUtils.valueToString(comAlertDeviceIpRequst));
        ItemRequest req = new ItemRequest();
        if(comAlertDeviceIpRequst.getReType() != 6) {
            if(comAlertDeviceIpRequst.getReType()==1) {
                req.setBizSystem(comAlertDeviceIpRequst.getRs());
            } else if(comAlertDeviceIpRequst.getReType()==2) {
                req.setIdcType(comAlertDeviceIpRequst.getRs());
            }else if(comAlertDeviceIpRequst.getReType()==3) {
                req.setRoomId(comAlertDeviceIpRequst.getRs());
            }else if(comAlertDeviceIpRequst.getReType()==4) {
                req.setDeviceClass(comAlertDeviceIpRequst.getRs());
            }else if(comAlertDeviceIpRequst.getReType()==5) {
                req.setDeviceType(comAlertDeviceIpRequst.getRs());
            }
        } else {
            String[] strings = new String[comAlertDeviceIpRequst.getIp().size()];
            comAlertDeviceIpRequst.getIp().toArray(strings);
            req.setIps(strings);
        }
        req.setItem(comAlertDeviceIpRequst.getItem());
        req.setMultiQuery(comAlertDeviceIpRequst.isMultiQuery());
        
        MonitorKeyDto result = zabbixItemServiceClient.queryMonitorObjectList(req);
		ComMonitorKeyDto response = PayloadParseUtil.jacksonBaseParse(ComMonitorKeyDto.class, result);
		if (result != null && result.getMoniList() != null) {
			List<ComMonitorKeyDto> subResponse = PayloadParseUtil.jacksonBaseParse(ComMonitorKeyDto.class, result.getMoniList());
			response.setMoniList(subResponse);
		}
        return response;
    }

	@Override
	public List<String> getDeviceClass() {
		return AlertRepPanelClient.getDeviceClass();
	}

	@Override
	public List<String> getDeviceType(@RequestParam("deviceClass")String deviceClass) {
		if (deviceClass == null) {
            logger.error("getDeviceType deviceClass is null");
            throw new RuntimeException("getDeviceType deviceClass is null");
        }
		return AlertRepPanelClient.getDeviceType(deviceClass);
	}

	@Override
	public List<String> getSubclass(@RequestParam("deviceClass")String deviceClass, @RequestParam("deviceType")String deviceType) {
		if (deviceClass == null || deviceType == null) {
            logger.error("getDeviceType deviceClass or deviceType is null");
            throw new RuntimeException("getDeviceType deviceClass  or deviceType is null");
        }
		return AlertRepPanelClient.getSubclass(deviceClass, deviceType);
	}

	@Override
	public List<ComAlertDeviceItemInfo> getMonitor(@RequestParam("deviceClass")String deviceClass, @RequestParam("deviceType")String deviceType
			, @RequestParam("subclass")String subclass) {
		List<AlertDeviceItemInfo> values = AlertRepPanelClient.getMonitor(deviceClass, deviceType, subclass);
		return PayloadParseUtil.jacksonBaseParse(ComAlertDeviceItemInfo.class, values);
	}
	
	@Override
	public Map<String, List<ComAlertDeviceItemInfo>> getSubMonitors(@RequestParam("deviceClass") String deviceClass,
			@RequestParam("deviceType") String deviceType){
		
		List<AlertDeviceItemInfo> list = AlertRepPanelClient.getSubMonitorList(deviceClass, deviceType);
		List<ComAlertDeviceItemInfo> toList = PayloadParseUtil.jacksonBaseParse(ComAlertDeviceItemInfo.class, list);
		Map<String, List<ComAlertDeviceItemInfo>> map = new HashMap<>();
		for(ComAlertDeviceItemInfo d:toList) {
			
			if (StringUtils.isEmpty(d.getSubclass())) {
				continue;
			}
			String subclass = d.getSubclass();

			if (map.containsKey(d.getSubclass())) {
				map.get(subclass).add(d);
			} else {
				List<ComAlertDeviceItemInfo> temp = Lists.newArrayList();
				temp.add(d);
				map.put(subclass, temp);
			}
			 
		}
		return map;
	}
	
}
