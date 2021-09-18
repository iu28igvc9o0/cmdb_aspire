package com.aspire.ums.cmdb.maintenance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceProjectMfrsMapper;
import com.aspire.ums.cmdb.maintenance.mapper.HardWareMapper;
import com.aspire.ums.cmdb.maintenance.mapper.ProduceInfoConcatMapper;
import com.aspire.ums.cmdb.maintenance.payload.*;
import com.aspire.ums.cmdb.maintenance.service.IHardWareService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 类名称: HardWareServiceImpl
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/30 16:11
 * 版本: v1.0
 */
@Slf4j
@Service
@Transactional
public class HardWareServiceImpl implements IHardWareService {

    @Autowired
    private HardWareMapper hardWareMapper;

    @Autowired
    private ProduceInfoConcatMapper concatMapper;
    @Autowired
    private CmdbMaintenanceProjectMfrsMapper mfrsMapper;

    @Override
    public List<LinkedHashMap> queryByProjectId(String projectId) {
        return hardWareMapper.queryByProjectId(projectId);
    }

    @Override
    public Map<String, Object> queryIsExist(String deviceSerialNumber, String warrantyDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String projectInstanceId = hardWareMapper.queryIsExist(deviceSerialNumber,warrantyDate);
            if (StringUtils.isNotEmpty(projectInstanceId)) {
                String id = hardWareMapper.queryIdByProjectInstanceId(projectInstanceId);
                if (StringUtils.isNotEmpty(id)) {
                    result.put("success", true);
                    result.put("message", projectInstanceId);
                } else {
                    result.put("success", false);
                    result.put("message", projectInstanceId);
                }
                return result;
            } else {
                result.put("success", false);
                result.put("message", "TheProjectNoExistWithDeviceSN");
            }
        } catch (Exception e) {
            log.error("HardWareServiceImpl queryIsExist() error {}" ,e);
            result.put("success", false);
            result.put("message", "");
        }
        return result;
    }

    @Override
    public String queryIdByProjectInstanceId(String projectInstanceId) {
        return hardWareMapper.queryIdByProjectInstanceId(projectInstanceId);
    }


    @Override
    public Map<String, Object> update(Hardware hardware) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (null != hardware) {
                if (StringUtils.isNotEmpty(hardware.getId())) {
                    hardWareMapper.updateHardware(hardware);
                } else {
                    hardware.setId(UUIDUtil.getUUID());
                    hardWareMapper.insert(hardware);
                }
            }
            result.put("success", true);
            result.put("message", "修改成功");
        } catch (Exception e) {
            log.error("HardWareServiceImpl update() error {}" ,e);
            result.put("success", false);
            result.put("message", "修改失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> batchUpdate(Hardware hardwareList) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> hashMap = new HashMap<String, Object>();
            String[]  array1 = hardwareList.getId().split(",");
            if (null != array1 && array1.length > 0) {
                hashMap.put("list", Arrays.asList(array1));
                hashMap.put("buyMainten", hardwareList.getBuyMainten());
                hashMap.put("originBuy", hardwareList.getOriginBuy());
                hashMap.put("originBuyExplain", hardwareList.getOriginBuyExplain());
                hashMap.put("admin", hardwareList.getAdmin());
                hashMap.put("preTax", hardwareList.getPreTax());
                hashMap.put("taxRate", hardwareList.getTaxRate());
                hashMap.put("afterTax", hardwareList.getAfterTax());
                hashMap.put("unitPrice", hardwareList.getUnitPrice());
                hashMap.put("totalPrice", hardwareList.getTotalPrice());
                hashMap.put("payMethod", hardwareList.getPayMethod());
                hashMap.put("discountAmount", hardwareList.getDiscountAmount());
                hashMap.put("discountRate", hardwareList.getDiscountRate());

                hardWareMapper.batchUpdateHardware(hashMap);
            }
            String[]  array2 = hardwareList.getProjectInstanceId().split(",");
            List<Hardware> addList = new ArrayList<Hardware>();
            for (String arr2 : array2) {
                if (StringUtils.isNotEmpty(arr2)) {
                    Hardware hardware = new Hardware();
                    hardware.setId(UUIDUtil.getUUID());
                    hardware.setProjectInstanceId(arr2);
                    hardware.setBuyMainten(hardwareList.getBuyMainten());
                    hardware.setOriginBuy(hardwareList.getOriginBuy());
                    hardware.setOriginBuyExplain(hardwareList.getOriginBuyExplain());
                    hardware.setAdmin(hardwareList.getAdmin());
                    hardware.setPreTax(hardwareList.getPreTax());
                    hardware.setAfterTax(hardwareList.getAfterTax());
                    hardware.setTaxRate(hardwareList.getTaxRate());
                    hardware.setUnitPrice(hardwareList.getUnitPrice());
                    hardware.setTotalPrice(hardwareList.getTotalPrice());
                    hardware.setPayMethod(hardwareList.getPayMethod());
                    hardware.setDiscountAmount(hardwareList.getDiscountAmount());
                    hardware.setDiscountRate(hardwareList.getDiscountRate());
                    addList.add(hardware);
                }
            }
            if (null != addList && !addList.isEmpty()) {
                hardWareMapper.batchInsert(addList);
            }
            result.put("success", true);
            result.put("message", "批量修改成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量修改失败");
        }
        return result;
    }

    @Override
    public Result<Hardware> selectHardWareByPage(HardwareRequest request) {
        List<Hardware> data = hardWareMapper.getHardwareByPage(request);
        int dataCount = hardWareMapper.getHardwareCount(request);
        return new Result<>(dataCount,data);
    }

    @Override
    public List<Map<String, Object>> queryExportData(HardwareRequest sendRequest) {
        return hardWareMapper.queryExportData(sendRequest);
    }

    @Override
    public Map<String, Object> queryInfoByNameAndDeviceSn(String projectName, String deviceSn) {
        return hardWareMapper.queryInfoByNameAndDeviceSn(projectName,deviceSn);
    }
}
