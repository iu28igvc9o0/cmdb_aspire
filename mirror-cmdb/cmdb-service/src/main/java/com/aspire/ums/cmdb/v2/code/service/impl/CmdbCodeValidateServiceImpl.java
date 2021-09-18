package com.aspire.ums.cmdb.v2.code.service.impl;

import com.aspire.ums.cmdb.code.payload.CmdbCodeValidate;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbCodeValidateMapper;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Service
public class CmdbCodeValidateServiceImpl implements ICmdbCodeValidateService {

    @Autowired
    private CmdbCodeValidateMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbCodeValidate> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbCodeValidate get(CmdbCodeValidate entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbCodeValidate entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbCodeValidate entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbCodeValidate entity) {
        mapper.delete(entity);
    }

    @Override
    public List<CmdbCodeValidate> getValidateListByCodeId(String codeId) {
        return mapper.getValidateListByCodeId(codeId);
    }
}