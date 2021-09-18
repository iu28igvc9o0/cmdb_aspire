package com.aspire.ums.cmdb.v3.code.service.impl;
import com.aspire.ums.cmdb.v3.code.service.ICmdbV3CodeApproveService;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;
import com.aspire.ums.cmdb.v3.code.mapper.CmdbV3CodeApproveMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
@Service
public class CmdbV3CodeApproveServiceImpl implements ICmdbV3CodeApproveService {

    @Autowired
    private CmdbV3CodeApproveMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3CodeApprove> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3CodeApprove get(CmdbV3CodeApprove entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbV3CodeApprove getByCodeId(String codeId) {
        return mapper.getByCodeId(codeId);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3CodeApprove entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3CodeApprove entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3CodeApprove entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByCodeId(String codeId) {
        mapper.deleteByCodeId(codeId);
    }
}