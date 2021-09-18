package com.aspire.ums.cmdb.v2.process.service.impl;

import com.aspire.ums.cmdb.process.payload.CmdbProcessHandlerManager;
import com.aspire.ums.cmdb.v2.process.mapper.CmdbProcessHandlerManagerMapper;
import com.aspire.ums.cmdb.v2.process.service.ICmdbProcessHandlerManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-04-14 09:08:55
*/
@Service
public class CmdbProcessHandlerManagerServiceImpl implements ICmdbProcessHandlerManagerService {

    @Autowired
    private CmdbProcessHandlerManagerMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbProcessHandlerManager> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbProcessHandlerManager get(CmdbProcessHandlerManager entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbProcessHandlerManager entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbProcessHandlerManager entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbProcessHandlerManager entity) {
        mapper.delete(entity);
    }
}