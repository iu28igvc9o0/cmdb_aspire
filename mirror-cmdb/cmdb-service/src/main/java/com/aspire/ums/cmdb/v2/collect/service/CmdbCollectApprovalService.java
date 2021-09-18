package com.aspire.ums.cmdb.v2.collect.service;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-18 20:55:56
*/
public interface CmdbCollectApprovalService {
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Map<String,Object>> list(CmdbCollectApprovalQuery approvalQuery);
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbCollectApproval> listSimpleByQuery(CmdbCollectApprovalQuery approvalQuery);
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbCollectApproval> listByIds(List<String> ids);
    /**
     * 获取数量
     * @return 返回数量
     */
    Integer listCount(CmdbCollectApprovalQuery approvalQuery);
    /**
     * 获取数量
     * @return 返回数量
     */
    List<CmdbSimpleCode> getApprovalHeaderCode(String moduleId);
    /**
     * 获取数量
     * @return 返回数量
     */
    CmdbCollectApproval getDetail(String instanceId, String codeId);


    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbCollectApproval entity);

    /**
     * 批量新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<CmdbCollectApproval> entities);

//    /**
//     * 修改实例
//     * @param userName 审核用户名
//     * @param approval 审核信息
//     * @return
//     */
//    void update(String userName, CmdbCollectApproval approval);

//    /**
//     * 批量审批
//     * @param entities 实例数据
//     * @return
//     */
//    JSONObject updateByBatch(String userName, List<CmdbCollectApproval> entities);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbCollectApproval entity);

    /**
     * 删除实例
     * @param approvals 实例数据
     * @return
     */
    void deleteByBatch(List<CmdbCollectApproval> approvals);
    /**
     * 批量审批
     * @param userName 审批用户
     * @param approvalList 审批配置项
     */
    List<Map<String, Object>> approve(String userName, Integer approvalStatus,String approvalDescribe, List<CmdbCollectApproval> approvalList);

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