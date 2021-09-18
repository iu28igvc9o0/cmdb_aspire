package com.aspire.ums.cmdb.v2.collect.mapper;

import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-18 20:55:56
*/
@Mapper
public interface CmdbCollectApprovalMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
     List<CmdbCollectApproval> list(CmdbCollectApprovalQuery approvalQuery);

    /**
     * 查询数量
     * @return 返回数量
     */
    Integer listCount(CmdbCollectApprovalQuery approvalQuery);

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> listByQuery(CmdbCollectApprovalQuery approvalQuery);

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbCollectApproval> listSimpleByQuery(CmdbCollectApprovalQuery approvalQuery);
    /**
     * 根据id获取实例
     * @return 返回所有实例数据
     */
    List<CmdbCollectApproval> listByIds(List<String> ids);

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbCollectApproval> listByEntity(CmdbCollectApproval entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCollectApproval get(CmdbCollectApproval entity);
    /**
     * 根据主键ID 获取数据信息
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCollectApproval getById(@Param("id") String id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbCollectApproval entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbCollectApproval entity);

    /**
     * 批量修改实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<CmdbCollectApproval> entities);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbCollectApproval entity);

    /**
     * 批量删除实例
     * @param entities 实例数据
     * @return
     */
    void deleteByBatch(List<CmdbCollectApproval> entities);

    /**
     * 获取待审核的配合项列表
     * @return
     */
    List<Map<String, String>> getFiledNameList();

    /**
     * 获取变更方式列表
     * @return
     */
    List<Map<String, String>> getOperatorTypeList();
}