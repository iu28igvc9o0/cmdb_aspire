package com.aspire.mirror.alert.server.biz.alert;

import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsHis;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;
import java.util.Map;

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
public interface AlertsHisBiz {
    /**
     * 创建告警
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    String insert(AlertsHisVo alertsDTO);

    /**
     * 根据主键查询
     *
     * @param alertId 告警ID
     * @return AlertsHisDTO 告警对象
     */
    AlertsHisVo selectByPrimaryKey(String alertId);
 
   
    
    /**
     * 告警上报分页
     *
     * @param alertId 告警Id
     * @return
     */
    public PageResponse<AlertsDetail> alertGenerateListByPage(String alertId, String pageNo, String pageSize);



    /**
     * 历史告警相关告警
     *
     * @param alertId 告警ID集合
     * @return List<AlertsDTO> 告警列表
     */
    List<AlertsHisVo> selectAlertGenerateList(String alertId);


    PageResponse<AlertsHisVo> getAlertHisList(PageRequest page);

    /**
     * 批量创建历史告警
     *
     * @param alertsDTOList 需新增历史告警列表数据
     * @return
     */
    List<AlertsHisVo> insertByBatch(List<AlertsHisVo> alertsDTOList);


    /**
     * 告警修改备注. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @return void 告警修改备注
     */
    void  updateNote(String alertId, String note);

    /**
     * 根据条件查询
     * @param alertHisQuery
     * @return
     */
    List<AlertsHisVo> select(AlertsHis alertHisQuery);
    /**
     * @param alertsHisVo 告警修改对象
     * @return
     */
    int updateByPrimaryKey(AlertsHisVo alertsHisVo);

    PageResponse<Map<String, Object>> alertRelateData(int alertType,String alertId,Integer pageSize,Integer pageNo);
}
