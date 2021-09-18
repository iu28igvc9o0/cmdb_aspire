package com.aspire.ums.cmdb.v2.idc.service.impl;
import com.aspire.ums.cmdb.v2.idc.service.ICmdbIdcManagerService;
import com.aspire.ums.cmdb.v2.idc.entity.CmdbIdcManager;
import com.aspire.ums.cmdb.v2.idc.mapper.CmdbIdcManagerMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-17 17:29:30
*/
@Service
public class CmdbIdcManagerServiceImpl implements ICmdbIdcManagerService {

    @Autowired
    private CmdbIdcManagerMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbIdcManager> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbIdcManager get(CmdbIdcManager entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbIdcManager entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbIdcManager entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbIdcManager entity) {
        mapper.delete(entity);
    }
}