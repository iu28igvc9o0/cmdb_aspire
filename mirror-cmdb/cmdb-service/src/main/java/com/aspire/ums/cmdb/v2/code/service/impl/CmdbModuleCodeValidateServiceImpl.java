package com.aspire.ums.cmdb.v2.code.service.impl;

import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeValidate;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbModuleCodeValidateMapper;
import com.aspire.ums.cmdb.v2.code.service.ICmdbModuleCodeValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-17 09:51:14
*/
@Service
public class CmdbModuleCodeValidateServiceImpl implements ICmdbModuleCodeValidateService {

    @Autowired
    private CmdbModuleCodeValidateMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbModuleCodeValidate> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbModuleCodeValidate get(CmdbModuleCodeValidate entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbModuleCodeValidate entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbModuleCodeValidate entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbModuleCodeValidate entity) {
        mapper.delete(entity);
    }
}