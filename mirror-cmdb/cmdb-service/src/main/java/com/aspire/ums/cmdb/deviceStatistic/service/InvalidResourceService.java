package com.aspire.ums.cmdb.deviceStatistic.service;

import java.util.List;

import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceResp;
 


/**
 * 设备统计
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.deviceStatistic.service
 * 类名称:    DeviceStatisticService.java
 * 类描述:    设备统计业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface InvalidResourceService {
	
    
    /**
     * 低效无效资源统计
     */
	public List<InvalidResourceResp> selectInvalidResource(InvalidResourceRequest invalidResourceRequest) ;
   
   
    /**
    * 保存低效无效资源统计
    */
	public int insertInvalidResource(List<InvalidResourceResp> invalidResourceList);
    
 
    
    
}
