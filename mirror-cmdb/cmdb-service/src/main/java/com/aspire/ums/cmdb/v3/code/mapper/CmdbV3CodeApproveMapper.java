package com.aspire.ums.cmdb.v3.code.mapper;
import java.util.List;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
@Mapper
public interface CmdbV3CodeApproveMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeApprove> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeApprove> listByEntity(CmdbV3CodeApprove entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeApprove get(CmdbV3CodeApprove entity);

    /**
     * 根据主键ID 获取数据信息
     * @param codeId 码表ID
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeApprove getByCodeId(@Param("codeId") String codeId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeApprove entity);
    /**
     * 新增实例
     * @param approves 实例数据
     * @return
     */
    void insertByBatch(List<CmdbV3CodeApprove> approves);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeApprove entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeApprove entity);

    /**
     * 根据码表ID 删除实例
     * @param codeId 码表ID
     */
    void deleteByCodeId(String codeId);
}