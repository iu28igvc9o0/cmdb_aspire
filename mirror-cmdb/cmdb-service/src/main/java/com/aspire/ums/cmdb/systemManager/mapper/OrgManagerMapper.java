package com.aspire.ums.cmdb.systemManager.mapper;

import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.aspire.ums.cmdb.systemManager.payload.OrgManagerReq;
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
 * 创建时间: 2019/5/21 18:41
 * 版本: v1.0
 */
@Mapper
public interface OrgManagerMapper {
    int getOrgManagerDataCount(@RequestBody OrgManagerReq request);

    /**
     * 分页按条件查询
     *
     * @param request
     * @return
     */
    List<OrgManager> getOrgManagerData(@RequestBody OrgManagerReq request);

    List<OrgManager> getOrgManagerByPid(@Param("pid") String pid);

    /**
     * 新增数据
     *
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
    
//    List<Map> getBizSystemByOrgId(@Param("orgId") String orgId);

    List<OrgManager> getAllOrg();

    List<OrgManager> getAllEipOrg();

    List<Map> getBizSystemByDepartment1(@Param("department1") String orgName);

    List<Map> getBizSystemByDepartment2(@Param("department2") String orgName);

    OrgManager getWithDepInfo(@Param("department1") String dep1,
                              @Param("department2") String dep2);
}
