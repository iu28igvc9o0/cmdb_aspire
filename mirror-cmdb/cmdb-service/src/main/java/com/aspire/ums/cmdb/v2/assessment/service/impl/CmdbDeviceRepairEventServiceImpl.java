package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.assessment.DeviceConst;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbDeviceRepairEventMapper;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceRepairEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-25 19:56:44
*/
@Service
public class CmdbDeviceRepairEventServiceImpl implements ICmdbDeviceRepairEventService {

    @Autowired
    private CmdbDeviceRepairEventMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbDeviceRepairEvent> listByEntity(Integer pageNum, Integer pageSize, CmdbDeviceRepairEvent deviceRepairEvent) {
        return mapper.listByEntity(pageNum, pageSize, deviceRepairEvent);
    }

    @Override
    public Integer listCount(CmdbDeviceRepairEvent entity) {
        return mapper.listCount(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbDeviceRepairEvent get(CmdbDeviceRepairEvent entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param data 实例数据
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void insert(JSONObject data) {
        List<CmdbDeviceRepairEvent> entities = JSON.parseArray(data.getJSONArray("saveData").toJSONString(), CmdbDeviceRepairEvent.class);
        List<String> deleteIds = JSON.parseArray(data.getJSONArray("deleteIds").toJSONString(), String.class);
        validData(entities);
        for (CmdbDeviceRepairEvent entity : entities) {
            if (StringUtils.isEmpty(entity.getId())) {
                entity.setId(UUIDUtil.getUUID());
                entity.setCreateTime(new Date());
                entity.setStatus(DeviceConst.DEVICE_STATUS_SAVE);
            }
            // 计算问题时长
            if (StringUtils.isNotEmpty(entity.getProblemStartTime()) && StringUtils.isNotEmpty(entity.getProblemEndTime())) {
                Date startTime = entity.getProblemStartTime();
                Date endTime = entity.getProblemEndTime();
                String problemLong = DateUtils.getDatePoor(endTime, startTime);
                entity.setProblemLong(problemLong);
            }
        }
        if(deleteIds.size() > 0) {
            mapper.deleteByBatch(deleteIds);
        }
        if (entities.size() > 0) {
            mapper.insertByBatch(entities);
        }
    }

    private void validData(List<CmdbDeviceRepairEvent> entities) {
        entities.forEach(event -> {
            if (StringUtils.isEmpty(event.getCreateUsername())) {
                throw new RuntimeException("填报人不能为空");
            }
            if (StringUtils.isEmpty(event.getCreateUserPhone())) {
                throw new RuntimeException("填报人电话不能为空");
            }
            if (StringUtils.isEmpty(event.getProvince())) {
                throw new RuntimeException("填报人省份不能为空");
            }
            if (StringUtils.isEmpty(event.getQuarter())) {
                throw new RuntimeException("季度不能为空");
            }
        });
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbDeviceRepairEvent entity) {
        entity.setUpdateTime(new Date());
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbDeviceRepairEvent entity) {
        mapper.delete(entity);
    }
}