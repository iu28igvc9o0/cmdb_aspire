package com.aspire.mirror.alert.server.biz.dashboard;

import java.util.List;

import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanel;

/**
 * 告警业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.biz.impl
 * 类名称:    AlertsHisBiz.java
 * 类描述:    告警业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface AlertRepPanelBiz {
    /**
     * 创建告警
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
	AlertRepPanel insert(AlertRepPanel panel) throws Exception;

    void  update(AlertRepPanel panel ) throws Exception;
    
    
    void  delete(String id )throws Exception;
    
    AlertRepPanel getByName(String name);

	List<AlertRepPanel> getAllPanel();
	
	AlertRepPanel selectByPrimaryKey(String id);

}
