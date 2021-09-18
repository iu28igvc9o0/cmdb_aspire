package com.aspire.mirror.alert.server.dao.third;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


/**
 * 告警操作记录DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsDao.java
 * 类描述:    告警操作记录DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface AlertStorageUseRateDao {
	

    
    
    /**
     * 查询操作记录
     * @param alerts snmp告警数据
     */

    Map<String,Object> getStorageUserate();
    

}
