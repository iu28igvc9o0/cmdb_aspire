package com.aspire.ums.cmdb.systemManager.mapper;

import com.aspire.ums.cmdb.systemManager.payload.BizSystem;
import com.aspire.ums.cmdb.systemManager.payload.BizSystemReq;
import com.aspire.ums.cmdb.systemManager.payload.Concat;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.mapper
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/21 15:33
 * 版本: v1.0
 */
@Mapper
public interface BizSystemMapper {
    int getBizSystemDataCount(@RequestBody BizSystemReq request);
    /**
     * 分页按条件查询
     * @param request
     * @return
     */
    List<BizSystem> getBizSystemData(@RequestBody BizSystemReq request);
    
    List<BizSystem> getBizSystemByPid (@Param("pid") String pid);
    
    /**
     * 新增数据
     * @param bizSystem 动作PO对象
     * @return int 新增数据条数
     */
    int insert(@RequestBody BizSystem bizSystem);
    
    /**
     * 新增联系人信息
     * @param concat
     * @return
     */
    int insertConcat(Concat concat);
    
    
    BizSystem getBizSystemById(@Param("id") String id);
    List<Concat> getConcatById(@Param("id") String id);
    
    void delConcatById(@Param("bizId") String bizId);
    
    /**
     * 根据主键更新数据
     *
     * @param BizSystem 动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(@RequestBody BizSystem BizSystem);
    
    void updateSubBizOrgId(@Param("orgId") String orgId,@Param("pid") String pid);
    
    int delete(@Param("id") String id);
    
    OrgManager getOrgById(@Param("orgId") String orgId);

    /**
     * 根据业务系统名称获取部门信息
     * @param bizSystem 业务系统名称
     */
    Map<String, String> getDepartmentInfoByBizSystem(@Param("bizSystem") String bizSystem);
}
