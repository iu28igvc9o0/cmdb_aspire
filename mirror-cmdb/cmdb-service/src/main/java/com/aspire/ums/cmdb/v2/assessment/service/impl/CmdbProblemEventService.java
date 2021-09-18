package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEventReq;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.assessment.DeviceConst;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbProblemEventMapper;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbProblemEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.*;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.problemEvent.service.impl
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 20:35
 * 版本: v1.0
 */
@Slf4j
@Service
@Transactional
public class CmdbProblemEventService implements ICmdbProblemEventService {
    @Autowired
    private CmdbProblemEventMapper problemEventMapper;
    
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    @Override
    public Result<CmdbProblemEvent> getAllData(int pageNum, int pageSize, String province,String quarter, String createUsername) {
        CmdbProblemEventReq request = new CmdbProblemEventReq();
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        request.setStartPageNum();
        request.setProvince(province);
        request.setQuarter(quarter);
        request.setCreateUsername(createUsername);
        Result<CmdbProblemEvent> response = new Result<CmdbProblemEvent>();
        int dataCount = problemEventMapper.getAllDataCount(request);
        response.setCount(dataCount);
        if (dataCount > 0) {
            List<CmdbProblemEvent> dataList = problemEventMapper.getAllData(request);
            response.setData(dataList);
        }
        return response;
    }
    
    /**
     * 新增更新实例
     * @param data 实例数据
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public Map<String, Object> insertOrUpdate(JSONObject data) {
        List<CmdbProblemEvent> entity = JSON.parseArray(data.getJSONArray("saveData").toJSONString(), CmdbProblemEvent.class);
        List<String> deleteIds = JSON.parseArray(data.getJSONArray("deleteIds").toJSONString(), String.class);
        Map<String, Object> result = new HashMap<>();
        try {
            if (null != entity && !entity.isEmpty()) {
                List<CmdbProblemEvent> list = new ArrayList<>();
                for (CmdbProblemEvent pro : entity) {
                    if (StringUtils.isNotEmpty(pro.getProblemStartTime()) && StringUtils.isNotEmpty(pro.getBizRegainTime())) {
                        Date startTime = pro.getProblemStartTime();
                        Date bizRegainTime = pro.getBizRegainTime();
                        String problemLong = DateUtils.getDatePoor(bizRegainTime, startTime);
                        pro.setBizRegainLong(problemLong);
                  
                    }
                     // hangfang 2020.07.30 null引用//                    else {
//                        pro.setBizRegainLong(null);
//                    }
                    if (StringUtils.isNotEmpty(pro.getProblemStartTime()) && StringUtils.isNotEmpty(pro.getProblemRemoveTime())) {
                        Date startTime = pro.getProblemStartTime();
                        Date removeTime = pro.getProblemRemoveTime();
                        String problemLong = DateUtils.getDatePoor(removeTime, startTime);
                        pro.setProblemHandleLong(problemLong);
                    } else {
                        pro.setProblemHandleLong(null);
                    }
                    //剔除审核通的数据(剩余新增和修改数据)
                    if (StringUtils.isEmpty(pro.getId())) {
                        pro.setStatus(DeviceConst.DEVICE_STATUS_SAVE);
                        pro.setId(UUIDUtil.getUUID());
                        list.add(pro);
                    } else if (1 != pro.getStatus()) {
                        list.add(pro);
                    }
                }
                if (list.size() > 0) {
                    problemEventMapper.insertOrUpdate(list);
                }
            }
            if (deleteIds.size() > 0) {
                problemEventMapper.deleteByBatch(deleteIds);
            }
            result.put("success", true);
            result.put("message", "保存成功");
        } catch (Exception e) {
            log.error("insertOrUpdate error {}",e);
            result.put("success", false);
            result.put("message", "保存失败" + e.getMessage());
        }
        return result;
    }
    
    public Map<String, Object> delete(@PathVariable("id") String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StringUtils.isNotEmpty(id)) {
                problemEventMapper.delete(id);
            }
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            log.error("delete error {}", e);
            result.put("success", false);
            result.put("message", "删除失败");
        }
        return result;
    }
    
    /**
     * 审批
     * @param province
     * @param status -1：待审核 0：驳回 1：通过
     * @return
     */
    public Map<String, Object> updateStatus(String province,int status, String quarter) {
        Map<String, Object> result = new HashMap<>();
        try {
            problemEventMapper.updateStatus(province, status, quarter);
            result.put("success", true);
            result.put("message", "审批成功");
        } catch (Exception e) {
            log.error("updateStatus error {}", e);
            result.put("success", false);
            result.put("message", "审批失败");
        }
        return result;
    }
}
