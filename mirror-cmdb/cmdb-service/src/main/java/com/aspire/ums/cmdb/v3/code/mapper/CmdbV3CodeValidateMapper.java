package com.aspire.ums.cmdb.v3.code.mapper;
import java.util.List;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
@Mapper
public interface CmdbV3CodeValidateMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeValidate> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeValidate> listByEntity(CmdbV3CodeValidate entity);

    /**
     * 获取所有实例
     * @param codeId 码表ID
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeValidate> getByCodeId(@Param("codeId") String codeId);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeValidate get(CmdbV3CodeValidate entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeValidate entity);

    /**
     * 新增实例
     * @param validates 实例数据
     * @return
     */
    void insertByBatch(@Param("validates") List<CmdbV3CodeValidate> validates);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeValidate entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeValidate entity);

    /**
     * 根据码表ID 删除验证信息
     * @param codeId 码表ID
     */
    void deleteByCodeId(@Param("codeId") String codeId);
}