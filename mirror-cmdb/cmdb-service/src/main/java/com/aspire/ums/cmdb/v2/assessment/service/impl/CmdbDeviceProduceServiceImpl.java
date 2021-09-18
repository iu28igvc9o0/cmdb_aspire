package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbDeviceProduceMapper;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbDeviceProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-26 10:48:11
*/
@Service
public class CmdbDeviceProduceServiceImpl implements ICmdbDeviceProduceService {

    @Autowired
    private CmdbDeviceProduceMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbDeviceProduce> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbDeviceProduce get(CmdbDeviceProduce entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbDeviceProduce entity) {
        mapper.insert(entity);
    }

    @Override
    public void insertByBatch(List<CmdbDeviceProduce> entities) {
        List<String> produceNames = new ArrayList<>();
        List<CmdbDeviceProduce> addProduces = new ArrayList<>();
        List<CmdbDeviceProduce> sourceProduces = mapper.list();
        if (sourceProduces.size() > 0) {
            for (CmdbDeviceProduce produce : sourceProduces) {
                produceNames.add(produce.getName());
            }
        }
        for (CmdbDeviceProduce produce : entities) {
            if (StringUtils.isEmpty(produce.getName())) {
                throw new RuntimeException("厂家不能为空");
            }
            if (StringUtils.isEmpty(produce.getId())) {
                if (produceNames.contains(produce.getName())) {
                    throw new RuntimeException(produce.getName() + " 厂家已存在，请重新输入");
                }
                produce.setId(UUIDUtil.getUUID());
                addProduces.add(produce);
            }
        }
        if (addProduces.size() > 0) {
            mapper.insertByBatch(addProduces);
        }
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbDeviceProduce entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbDeviceProduce entity) {
        mapper.delete(entity);
    }
}