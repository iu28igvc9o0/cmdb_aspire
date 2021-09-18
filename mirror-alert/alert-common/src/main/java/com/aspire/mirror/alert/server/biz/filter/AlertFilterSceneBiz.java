package com.aspire.mirror.alert.server.biz.filter;

import java.util.List;

import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterOption;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterScene;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

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
public interface AlertFilterSceneBiz {
    /**
     * 创建告警
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
	void insert(AlertFilterScene filter);

    /**
     * 根据主键查询
     *
     * @param alertId 告警ID
     * @return AlertsHisDTO 告警对象
     */
	AlertFilterScene selectByPrimaryKey(String id);
 
      
    
    
    
    /**
     * 告警列表
     *
     * @param page 告警查询page对象
     * @return
     */
    PageResponse<AlertFilterScene> pageList(PageRequest page);

    
    
    /**
     * 告警修改备注. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @return void 告警修改备注
     */
    void  update(AlertFilterScene filter );
    
    
    void  delete(String id );
    
    AlertFilterScene getByName(String name,String filterId);

	List<AlertFilterOption> getAllOptions();

    public List<AlertFilterOption> getOptionsByQueryType(String queryType);
}
