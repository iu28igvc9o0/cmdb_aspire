package com.aspire.ums.cmdb.demand.mapper;
import java.util.List;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandResourceTypeValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:20
*/
@Mapper
public interface CmdbDemandResourceTypeValueMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDemandResourceTypeValue> list();

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDemandResourceTypeValue> listByDemandId(@Param("demandId") String demandId);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDemandResourceTypeValue get(CmdbDemandResourceTypeValue entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbDemandResourceTypeValue entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDemandResourceTypeValue entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDemandResourceTypeValue entity);
}