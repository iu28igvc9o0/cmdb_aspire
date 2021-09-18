package com.aspire.ums.cmdb.systemManager.mapper;

import com.aspire.ums.cmdb.systemManager.entity.OrgManager;
import com.aspire.ums.cmdb.systemManager.entity.OrgManagerReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.mapper
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/21 18:41
 * 版本: v1.0
 */
@Mapper
public interface OrgManagerMapper {
    int getOrgManagerDataCount(@RequestBody OrgManagerReq request);
    /**
     * 分页按条件查询
     * @param request
     * @return
     */
    List<OrgManager> getOrgManagerData(@RequestBody OrgManagerReq request);
    
    List<OrgManager> getOrgManagerByPid (@Param("pid") String pid);
    
    /**
     * 新增数据
     * @param orgManager 动作PO对象
     * @return int 新增数据条数
     */
    int insert(OrgManager orgManager);
    
    OrgManager getOrgManagerById(@Param("id") String id);
    
    /**
     * 根据主键更新数据
     *
     * @param orgManager 动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(OrgManager orgManager);
    
    int delete(String id);
    
    List<OrgManager> getParentOrg(@Param("id") String id);
    
}
