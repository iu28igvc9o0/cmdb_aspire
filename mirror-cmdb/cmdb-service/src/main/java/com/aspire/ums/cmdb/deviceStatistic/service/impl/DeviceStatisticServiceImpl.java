package com.aspire.ums.cmdb.deviceStatistic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.deviceStatistic.mapper.DeviceStatisticMapper;
import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticResp;
import com.aspire.ums.cmdb.deviceStatistic.service.DeviceStatisticService;

import lombok.extern.slf4j.Slf4j;

/**
 * 设备统计业务层
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.deviceStatistic.service.impl
 * 类名称:    DeviceStatisticServiceImpl.java
 * 类描述:    设备统计业务层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
@Slf4j
@Service
@Transactional
public class DeviceStatisticServiceImpl implements DeviceStatisticService {

    @Autowired
    private  DeviceStatisticMapper deviceStatisticMapper;

    //各类型设备的数量统计
	@Override
	public List<DeviceStatisticResp> selectDeviceBydeviceType( DeviceStatisticRequest deviceStatisticRequest) {
        log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
		Map<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("idcType", deviceStatisticRequest.getIdcType());
		hashMap.put("podName", deviceStatisticRequest.getPodName());
        List<Map<String, Object>> deviceStatisticList=deviceStatisticMapper.selectDeviceBydeviceType(hashMap);
		List<DeviceStatisticResp> deviceStatisticRespList= new ArrayList<DeviceStatisticResp>();
        for ( Map<String, Object> map : deviceStatisticList ) {
			DeviceStatisticResp  deviceStatisticResp=new DeviceStatisticResp();
			deviceStatisticResp.setIdcType((String) map.get("idcType"));
			deviceStatisticResp.setPodName((String) map.get("podName"));
			deviceStatisticResp.setNumber(  map.get("number").toString());
			deviceStatisticResp.setServerNumber(  map.get("serverNumber").toString());
			deviceStatisticResp.setNetworkNumber(  map.get("networkNumber").toString());
			deviceStatisticResp.setStorageNumber(  map.get("storageNumber").toString());
			deviceStatisticResp.setSafetyNumber (  map.get("safetyNumber").toString()); 
			deviceStatisticResp.setOtherNumber (  map.get("otherNumber").toString()); 
			deviceStatisticRespList.add(deviceStatisticResp);
		}
		return deviceStatisticRespList;
	}

    //各类型的各品牌设备数量统计
	@Override
	public List<DeviceStatisticResp> selectDeviceBydeviceTypeDeviceMfrs( DeviceStatisticRequest deviceStatisticRequest) {
        log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
		Map<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("idcType", deviceStatisticRequest.getIdcType());
		hashMap.put("podName", deviceStatisticRequest.getPodName());
		hashMap.put("deviceClass", deviceStatisticRequest.getDeviceClass());
		hashMap.put("deviceType", deviceStatisticRequest.getDeviceType());
		hashMap.put("deviceMfrs", deviceStatisticRequest.getDeviceMfrs());
        List<Map<String, Object>> deviceStatisticList=deviceStatisticMapper.selectDeviceBydeviceTypeDeviceMfrs (hashMap);
		List<DeviceStatisticResp> deviceStatisticRespList= new ArrayList<DeviceStatisticResp>();
        for ( Map<String, Object> map : deviceStatisticList ) {
			DeviceStatisticResp  deviceStatisticResp=new DeviceStatisticResp();
			deviceStatisticResp.setIdcType((String) map.get("idcType"));
			deviceStatisticResp.setPodName((String) map.get("podName"));
			deviceStatisticResp.setDeviceClass((String) map.get("deviceClass"));
			deviceStatisticResp.setDeviceType((String) map.get("deviceType"));
			deviceStatisticResp.setDeviceMfrs((String) map.get("deviceMfrs"));
			deviceStatisticResp.setNumber( map.get("number").toString());
			deviceStatisticRespList.add(deviceStatisticResp);
		}
		return deviceStatisticRespList;
	}

	@Override
	public List<DeviceStatisticResp> selectDeviceByidcTypeDeviceType(DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
		Map<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("idcType", deviceStatisticRequest.getIdcType());
		hashMap.put("podName", deviceStatisticRequest.getPodName());
		hashMap.put("deviceClass", deviceStatisticRequest.getDeviceClass());
		hashMap.put("deviceType", deviceStatisticRequest.getDeviceType());
		hashMap.put("deviceMfrs", deviceStatisticRequest.getDeviceMfrs());
        List<Map<String, Object>> deviceStatisticList=deviceStatisticMapper.selectDeviceByidcTypeDeviceType (hashMap);
		List<DeviceStatisticResp> deviceStatisticRespList= new ArrayList<DeviceStatisticResp>();
        for ( Map<String, Object> map : deviceStatisticList ) {
			DeviceStatisticResp  deviceStatisticResp=new DeviceStatisticResp();
			deviceStatisticResp.setIdcType((String) map.get("idcType"));
			deviceStatisticResp.setPodName((String) map.get("podName"));
			deviceStatisticResp.setDeviceClass((String) map.get("deviceClass"));
			deviceStatisticResp.setDeviceMfrs((String) map.get("deviceMfrs"));
			deviceStatisticResp.setNumber(  map.get("number").toString());
			deviceStatisticRespList.add(deviceStatisticResp);
		}
		return deviceStatisticRespList;
	}

	
	@Override
	public List<DeviceStatisticResp> selectDeviceBybizSystem(DeviceStatisticRequest deviceStatisticRequest) {
       log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
		Map<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("bizSystem", deviceStatisticRequest.getBizSystem());
		hashMap.put("idcType", deviceStatisticRequest.getIdcType());
		hashMap.put("podName", deviceStatisticRequest.getPodName());
        List<Map<String, Object>> deviceStatisticList=deviceStatisticMapper.selectDeviceBybizSystem(hashMap);
		List<DeviceStatisticResp> deviceStatisticRespList= new ArrayList<DeviceStatisticResp>();
        for ( Map<String, Object> map : deviceStatisticList ) {
			DeviceStatisticResp  deviceStatisticResp=new DeviceStatisticResp();
			deviceStatisticResp.setBizSystem((String) map.get("bizSystem"));
			deviceStatisticResp.setIdcType((String) map.get("idcType"));
			deviceStatisticResp.setPodName((String) map.get("podName"));
			deviceStatisticResp.setNumber(  map.get("number").toString());
			deviceStatisticResp.setServerNumber(  map.get("serverNumber").toString());
			deviceStatisticResp.setNetworkNumber(  map.get("networkNumber").toString());
			deviceStatisticResp.setStorageNumber(  map.get("storageNumber").toString());
			deviceStatisticResp.setSafetyNumber (  map.get("safetyNumber").toString()); 
			deviceStatisticResp.setOtherNumber (  map.get("otherNumber").toString());
			deviceStatisticRespList.add(deviceStatisticResp);
		}
		return deviceStatisticRespList;
	}

	@Override
	public List<DeviceStatisticResp> selectDeviceByDepartment(DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
		Map<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("idcType", deviceStatisticRequest.getIdcType());
		hashMap.put("department1", deviceStatisticRequest.getDepartment1());
		hashMap.put("department2", deviceStatisticRequest.getDepartment2());
		hashMap.put("bizSystem", deviceStatisticRequest.getBizSystem());
		hashMap.put("deviceClass", deviceStatisticRequest.getDeviceClass());
		hashMap.put("deviceType", deviceStatisticRequest.getDeviceType());
		hashMap.put("deviceMfrs", deviceStatisticRequest.getDeviceMfrs());
        List<Map<String, Object>> deviceStatisticList=deviceStatisticMapper.selectDeviceByDepartment (hashMap);
		List<DeviceStatisticResp> deviceStatisticRespList= new ArrayList<DeviceStatisticResp>();
        for ( Map<String, Object> map : deviceStatisticList ) {
			DeviceStatisticResp  deviceStatisticResp=new DeviceStatisticResp();
			deviceStatisticResp.setIdcType((String) map.get("idcType"));
			deviceStatisticResp.setDepartment1((String) map.get("department1"));
			deviceStatisticResp.setDepartment2((String) map.get("department2"));
			deviceStatisticResp.setBizSystem ((String) map.get("bizSystem"));
			deviceStatisticResp.setDeviceClass((String) map.get("deviceClass"));
			deviceStatisticResp.setDeviceType((String) map.get("deviceType"));
			deviceStatisticResp.setDeviceMfrs((String) map.get("deviceMfrs"));
			deviceStatisticResp.setNumber(  map.get("number").toString());
			deviceStatisticRespList.add(deviceStatisticResp);
		}
		return deviceStatisticRespList;
	}

	@Override
	public List<Map<String, Object>> selectIdcTypeStatistic() {
		return deviceStatisticMapper.selectIdcTypeStatistic();
	}

	@Override
	public List<Map<String, Object>> selectProjectStatistic(String idcType) {
		return deviceStatisticMapper.selectProjectStatistic(idcType);
	}

	@Override
	public List<Map<String, Object>> selectPodStatistic(String idcType, String projectName) {
		return deviceStatisticMapper.selectPodStatistic(idcType, projectName);
	}

	@Override
	public List<Map<String, Object>> selectMultiProjectStatistic(List<String> idcTypes) {
		return deviceStatisticMapper.selectMultiProjectStatistic(idcTypes);
	}
}
