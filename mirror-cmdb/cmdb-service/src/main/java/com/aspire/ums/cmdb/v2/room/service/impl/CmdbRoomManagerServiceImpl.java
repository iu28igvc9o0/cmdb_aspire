package com.aspire.ums.cmdb.v2.room.service.impl;
import com.aspire.ums.cmdb.v2.room.service.ICmdbRoomManagerService;
import com.aspire.ums.cmdb.v2.room.entity.CmdbRoomManager;
import com.aspire.ums.cmdb.v2.room.mapper.CmdbRoomManagerMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-17 17:30:16
*/
@Service
public class CmdbRoomManagerServiceImpl implements ICmdbRoomManagerService {

    @Autowired
    private CmdbRoomManagerMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbRoomManager> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbRoomManager> listByEntity(CmdbRoomManager entity) {
        return mapper.listByEntity(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbRoomManager get(CmdbRoomManager entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbRoomManager entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbRoomManager entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbRoomManager entity) {
        mapper.delete(entity);
    }
}