package com.aspire.ums.cmdb.maintenance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.mapper.HardWareUseMapper;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUseRequest;
import com.aspire.ums.cmdb.maintenance.service.IHardWareUseService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 类名称: HardWareUseServiceImpl
 * 类描述: 硬件维保使用Service
 * 创建人: PJX
 * 创建时间: 2019/7/30 16:12
 * 版本: v1.0
 */
@Slf4j
@Service
@Transactional
public class HardWareUseServiceImpl implements IHardWareUseService {

    @Autowired
    private HardWareUseMapper hardWareUseMapper;

    @Override
    public List<LinkedHashMap> queryByProjectId(String projectId) {
        return hardWareUseMapper.queryByProjectId(projectId);
    }

    @Override
    public Map<String, Object> addHardwareUse(HardwareUse hardwareUse) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (null != hardwareUse) {
                if (StringUtils.isEmpty(hardwareUse.getId())) {
                    hardwareUse.setId(UUIDUtil.getUUID());
                }
                hardWareUseMapper.insertHardWareUse(hardwareUse);
            }
            result.put("success", true);
            result.put("message", "新增成功");
        } catch (Exception e) {
            log.error("HardWareUseServiceImpl addHardwareUse() error {}", e);
            result.put("success", false);
            result.put("message", "新增失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> batchInsertHardWareUse(List<HardwareUse> list) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if(!list.isEmpty()) {
                for(HardwareUse item : list) {
                    if (StringUtils.isEmpty(item.getId())) {
                        item.setId(UUIDUtil.getUUID());
                    }
                }
                hardWareUseMapper.batchInsertHardWareUse(list);
            }
            result.put("success", true);
            result.put("message", "新增成功");
        } catch (Exception e) {
            log.error("HardWareUseServiceImpl addHardwareUse() error {}", e);
            result.put("success", false);
            result.put("message", "新增失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> update(HardwareUse hardwareUse) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (null != hardwareUse) {
                hardWareUseMapper.updateHardWareUse(hardwareUse);
            }
            result.put("success", true);
            result.put("message", "修改成功");
        } catch (Exception e) {
            log.error("HardWareUseServiceImpl update() error {}", e);
            result.put("success", false);
            result.put("message", "修改失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> batchUpdate(HardwareUse hardwareUse) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> hashMap = new HashMap<String, Object>();
            String[]  array1 = hardwareUse.getId().split(",");
            if (null != array1 && array1.length > 0) {
                hashMap.put("list", Arrays.asList(array1));
                hashMap.put("serverPerson", hardwareUse.getServerPerson());
                hashMap.put("serverLevel", hardwareUse.getServerLevel());
                hashMap.put("startDate", hardwareUse.getStartDate());
                hashMap.put("endDate", hardwareUse.getEndDate());
                hashMap.put("processTime", hardwareUse.getProcessTime());
                hashMap.put("actualManDay", hardwareUse.getActualManDay());
                hashMap.put("mobileApprover", hardwareUse.getMobileApprover());
                hashMap.put("operateApprover", hardwareUse.getOperateApprover());

                hardWareUseMapper.batchUpdateHardWareUse(hashMap);
            }

            result.put("success", true);
            result.put("message", "批量修改成功");
        } catch (Exception e) {
            log.error("HardWareUseServiceImpl batchUpdate() error {}", e);
            result.put("success", false);
            result.put("message", "批量修改失败");
        }
        return result;
    }

    @Override
    public Result<HardwareUse> selectHardWareUseByPage(HardwareUseRequest request) {
        Result<HardwareUse> response = new Result<HardwareUse>();

        int dataCount = hardWareUseMapper.getHardwareUseCount(request);
        response.setCount(dataCount);
        if (dataCount > 0) {
            List<HardwareUse> data = hardWareUseMapper.getHardwareUseByPage(request);
            response.setData(data);
        }
        return response;
    }



    @Override
    public JSONObject queryExportData(Map<String, Object> sendRequest) {
        JSONObject returnJson = new JSONObject();
        List<LinkedHashMap> list = hardWareUseMapper.queryExportData(sendRequest);
        int dataCount = (null == list ? 0 : list.size());
        returnJson.put("total", dataCount);
        returnJson.put("dataList", list);
        return returnJson;
    }

    @Override
    public List<Map<String, String>> getHardwareTableList() {
        return hardWareUseMapper.getHardwareTableList();
    }

    @Override
    public Map<String, Object> deleteHardwareUse(String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String[]  idArr = id.split(",");
            List<String> list = Arrays.asList(idArr);
            hardWareUseMapper.delete(list);
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            log.error("HardWareUseServiceImpl deleteHardwareUse() error {}", e);
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }

    @Override
    public List<HardwareUse> getHardWareUseByProjectInstanceId(String projectInstanceId) {
        return hardWareUseMapper.getHardWareUseByProjectInstanceId(projectInstanceId);
    }
}
