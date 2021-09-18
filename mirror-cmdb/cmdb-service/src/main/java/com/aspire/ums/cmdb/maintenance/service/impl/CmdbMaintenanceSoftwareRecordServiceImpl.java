package com.aspire.ums.cmdb.maintenance.service.impl;

import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceSoftwareRecordMapper;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceSoftwareRecordService;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-08-04 18:44:09
*/
@Service
public class CmdbMaintenanceSoftwareRecordServiceImpl implements ICmdbMaintenanceSoftwareRecordService {

    @Autowired
    private CmdbMaintenanceSoftwareRecordMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbMaintenanceSoftwareRecord> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbMaintenanceSoftwareRecord> listByEntity(CmdbMaintenanceSoftwareRecordQuery query) {
        return mapper.listByEntity(query);
    }

    @Override
    public int listCount(CmdbMaintenanceSoftwareRecordQuery query) {
        return mapper.listCount(query);
    }


    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(List<CmdbMaintenanceSoftwareRecord> entity) {
        entity.forEach(record -> {
            if (StringUtils.isEmpty(record.getId())) {
                record.setId(UUIDUtil.getUUID());
            }
//            record.setHandleLong(DateUtils.getDatePoor(record.getServerEndTime(), record.getServerStartTime()));
        });
        mapper.insert(entity);
    }

//    /**
//     * 修改实例
//     * @param entity 实例数据
//     * @return
//     */
//    public void update(CmdbMaintenanceSoftwareRecord entity) {
//        mapper.update(entity);
//    }

    /**
     * 删除实例
     * @param ids 实例数据
     * @return
     */
    public void delete(List<String> ids) {
        mapper.deleteByBatch(ids);
    }
}