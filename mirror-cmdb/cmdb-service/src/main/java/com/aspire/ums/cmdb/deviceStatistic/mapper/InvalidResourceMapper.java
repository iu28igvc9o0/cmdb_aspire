package com.aspire.ums.cmdb.deviceStatistic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceResp;

 
/**
 * 低效无效资源统计
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.deviceStatistic.mapper
 * 类名称:    InvalidResourceMapper.java
 * 类描述:    设备实例
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface InvalidResourceMapper {

    
    /**
     * 查询低效无效资源统计
     * @param   
     */
    List<Map<String, Object>> selectInvalidResource(Map<String, Object> hashMap);
    
    
    /**
     * 保存低效无效资源统计
     * @param   
     */
    int insertInvalidResource( List<InvalidResourceResp> invalidResourceList);
    
         
    
}
