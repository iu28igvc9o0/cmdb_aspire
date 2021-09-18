package com.aspire.ums.cmdb.demand.mapper;

import com.aspire.ums.cmdb.demand.entity.CmdbDemandChanged;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.demand.mapper
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/4 15:08
 * 版本: v1.0
 */
public interface CmdbDemandChangedMapper {
    
    void deleteChanged(@Param("demandId") String demandId);
    
    void insertChanged(CmdbDemandChanged cmdbDemandChanged);
    
    List<CmdbDemandChanged> listByDemandId(@Param("demandId") String demandId);
}
