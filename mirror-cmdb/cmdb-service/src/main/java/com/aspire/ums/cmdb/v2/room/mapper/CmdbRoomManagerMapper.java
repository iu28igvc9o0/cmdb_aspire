package com.aspire.ums.cmdb.v2.room.mapper;
import java.util.List;
import com.aspire.ums.cmdb.v2.room.entity.CmdbRoomManager;
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date 2019-06-17 17:30:16
*/
@Mapper
public interface CmdbRoomManagerMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbRoomManager> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbRoomManager> listByEntity(CmdbRoomManager entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbRoomManager get(CmdbRoomManager entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbRoomManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbRoomManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbRoomManager entity);
}