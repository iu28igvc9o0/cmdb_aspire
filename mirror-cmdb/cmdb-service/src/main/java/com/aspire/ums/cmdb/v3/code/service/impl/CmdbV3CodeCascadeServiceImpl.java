package com.aspire.ums.cmdb.v3.code.service.impl;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbV3CodeCascade;
import com.aspire.ums.cmdb.v3.code.mapper.CmdbV3CodeCascadeMapper;
import com.aspire.ums.cmdb.v3.code.service.ICmdbV3CodeCascadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-02-20 17:49:52
*/
@Service
public class CmdbV3CodeCascadeServiceImpl implements ICmdbV3CodeCascadeService {

    @Autowired
    private CmdbV3CodeCascadeMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3CodeCascade> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3CodeCascade get(CmdbV3CodeCascade entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3CodeCascade entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3CodeCascade entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3CodeCascade entity) {
        mapper.deleteByCodeId(entity.getCodeId());
    }

    @Override
    public void deleteByCodeId(String codeId) {
        mapper.deleteByCodeId(codeId);
    }

    @Override
    public void insertByBatch(List<CmdbV3CodeCascade> cascadeList) {
        cascadeList.forEach((v) -> mapper.insert(v));
    }

    @Override
    public List<CmdbV3CodeCascade> getByCodeId(String codeId) {
        return mapper.getByCodeId(codeId);
    }

    @Override
    public List<CmdbV3CodeCascade> getByChildCodeId(String codeId) {
        return mapper.getByChildCodeId(codeId);
    }
}