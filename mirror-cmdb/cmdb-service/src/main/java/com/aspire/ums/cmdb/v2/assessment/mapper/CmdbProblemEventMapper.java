package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEventReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.problemEvent.mapper
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 20:40
 * 版本: v1.0
 */
@Mapper
public interface CmdbProblemEventMapper {
    
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbProblemEvent> getAllData(CmdbProblemEventReq request);
    
    Integer getAllDataCount(CmdbProblemEventReq request);
    
    /**
     * 新增修改实例
     * @param problemEvent 实例数据
     * @return
     */
    void insertOrUpdate(List<CmdbProblemEvent> problemEvent);
    
    void delete(@Param("id") String id);

    void deleteByBatch(List<String> ids);

    
    void updateStatus(@Param("province") String province,
                      @Param("status") int status,
                      @Param("quarter") String quarter);
}
