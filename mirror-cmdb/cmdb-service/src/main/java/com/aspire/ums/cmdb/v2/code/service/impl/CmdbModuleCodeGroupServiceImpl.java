package com.aspire.ums.cmdb.v2.code.service.impl;

import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeGroup;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbCodeMapper;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbModuleCodeGroupMapper;
import com.aspire.ums.cmdb.v2.code.service.ICmdbModuleCodeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Service
public class CmdbModuleCodeGroupServiceImpl implements ICmdbModuleCodeGroupService {

    @Autowired
    private CmdbModuleCodeGroupMapper mapper;
    @Autowired
    private CmdbCodeMapper codeMapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbModuleCodeGroup> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbModuleCodeGroup get(CmdbModuleCodeGroup entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbModuleCodeGroup entity) {
        mapper.insert(entity);
    }

    /**
     * 批量新增实例
     * @param groups 实例数据
     * @return
     */
    @Override
    public void insertByBatch(List<CmdbModuleCodeGroup> groups) {
        mapper.insertByBatch(groups);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbModuleCodeGroup entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbModuleCodeGroup entity) {
        mapper.delete(entity);
    }

    @Override
    public List<CmdbModuleCodeGroup> getGroupListByModuleId(String moduleId) {
        CmdbModuleCodeGroup entity = new CmdbModuleCodeGroup();
        entity.setModuleId(moduleId);
        List<CmdbModuleCodeGroup> groups = mapper.listByEntity(entity);
        for (CmdbModuleCodeGroup group : groups) {
            group.setCodeList(codeMapper.getCodeListByGroupId(group.getGroupId(), group.getModuleId()));
        }
        return groups;
    }
}