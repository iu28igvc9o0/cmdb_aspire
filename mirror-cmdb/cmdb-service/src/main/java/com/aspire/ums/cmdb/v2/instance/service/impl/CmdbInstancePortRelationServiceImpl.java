package com.aspire.ums.cmdb.v2.instance.service.impl;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortQuery;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstancePortRelationMapper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstancePortRelationService;
import com.aspire.ums.cmdb.v2.instance.util.ConcatTypeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-09-05 10:26:54
*/
@Service
@Transactional
public class CmdbInstancePortRelationServiceImpl implements ICmdbInstancePortRelationService {

    @Autowired
    private CmdbInstancePortRelationMapper mapper;

    /**
     * 根据资源池获取ci信息
     */
    @Override
    public List<Map<String, String>> getInstanceIpByPool(String pool) {
        return mapper.getInstanceIpByPool(pool);
    }

    @Override
    public List<CmdbInstancePortRelation> selectByPortAndId(CmdbInstancePortRelation portRelation) {
        return mapper.selectByPortAndId(portRelation);
    }

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    @Override
    public Result<CmdbInstancePortRelation> list(CmdbInstancePortQuery instancePortQuery) {
        Result<CmdbInstancePortRelation> res = new Result<>();
        List<CmdbInstancePortRelation> dataList = mapper.listByEntity(instancePortQuery);
        Integer total = mapper.listCount(instancePortQuery);
        res.setTotalSize(total);
        res.setData(dataList);
        return res;
    }


    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbInstancePortRelation get(CmdbInstancePortRelation entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void insert(CmdbInstancePortRelation entity) {
        // 添加本端数据
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(UUIDUtil.getUUID());
        }
        entity.setCreateTime(new Date());
        mapper.insert(entity);
        // 添加对端数据
        CmdbInstancePortRelation zRelation = saveZPort(entity);
        mapper.insert(zRelation);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbInstancePortRelation entity) {
        // 更新本端数据
        mapper.update(entity);
        // 删除对端数据
        mapper.delete(entity);
        // 添加对端数据
        CmdbInstancePortRelation zRelation = saveZPort(entity);
        mapper.insert(zRelation);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbInstancePortRelation entity) {
        // 如果从界面上删除
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = mapper.get(entity);
        }
        mapper.delete(entity);
    }

    @Override
    public void insertByBatch(List<CmdbInstancePortRelation> entitys) {
        List<CmdbInstancePortRelation> relations = new ArrayList<>();
        for(CmdbInstancePortRelation item : entitys) {
            if (StringUtils.isEmpty(item.getId())) {
                item.setId(UUIDUtil.getUUID());
                item.setCreateTime(new Date());
            }
            // 添加对端数据
            CmdbInstancePortRelation zRelation = saveZPort(item);
            relations.add(zRelation);
        }
        entitys.addAll(relations);
        mapper.insertByBatch(entitys);
    }

    private CmdbInstancePortRelation saveZPort(CmdbInstancePortRelation item) {
        CmdbInstancePortRelation zRelation = new CmdbInstancePortRelation();
        zRelation.setAInstanceId(item.getZInstanceId());
        zRelation.setAPortId(item.getZPortId());
        zRelation.setId(UUIDUtil.getUUID());
        zRelation.setZInstanceId(item.getAInstanceId());
        zRelation.setZPortId(item.getAPortId());
        zRelation.setConnectType(ConcatTypeConvert.convertType(item.getConnectType()));
        zRelation.setCreateTime(new Date());
        zRelation.setRemark(item.getRemark());
        return zRelation;
    }
}