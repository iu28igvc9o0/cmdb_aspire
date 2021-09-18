package com.aspire.mirror.alert.server.dao.filter;

import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 告警DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsHisDao.java
 * 类描述:    告警DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface AlertFilterOptionDao {
    
    /**
     * 查询所有选项
     * @return
     */
    List<AlertFilterOption> selectAll();

    List<AlertFilterOption> select(@Param("queryType") String queryType);
}
