package com.aspire.ums.cmdb.v2.code.service.impl;

import com.aspire.ums.cmdb.code.payload.CmdbControlType;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbControlTypeMapper;
import com.aspire.ums.cmdb.v2.code.service.ICmdbControlTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Service
public class CmdbControlTypeServiceImpl implements ICmdbControlTypeService {

    @Autowired
    private CmdbControlTypeMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbControlType> list(Map<String, Object> queryParams) {
        return mapper.list(queryParams);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbControlType get(CmdbControlType entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbControlType getById(String controlId) {
        return mapper.getById(controlId);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Transactional (rollbackFor = {Exception.class, RuntimeException.class})
    public void insert(CmdbControlType entity) {
        entity.setControlId(UUIDUtil.getUUID());
        entity.setIsDelete("否");
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    @Transactional (rollbackFor = {Exception.class, RuntimeException.class})
    public void update(CmdbControlType entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param controlTypeList 实例数据
     * @return
     */
    @Transactional (rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(List<CmdbControlType> controlTypeList) {
        if (controlTypeList != null && controlTypeList.size() > 0) {
            for (CmdbControlType controlType : controlTypeList) {
                controlType.setIsDelete("是");
                mapper.update(controlType);
            }
        }
    }

    @Override
    public List<CmdbControlType> listByEntity(CmdbControlType controlType) {
        return mapper.listByEntity(controlType);
    }
}