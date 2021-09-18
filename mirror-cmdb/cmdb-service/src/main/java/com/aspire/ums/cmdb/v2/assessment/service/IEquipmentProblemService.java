package com.aspire.ums.cmdb.v2.assessment.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemPageRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemResp;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.v2.assessment.entity.EquipmentProblem;
 
 

/**
 * 设备问题
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.service
 * 类名称:    MaintenManageService.java
 * 类描述:    设备问题业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface IEquipmentProblemService {
	
	
    /**
     * 创建设备问题
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int insertEquipmentProblem(EquipmentProblem equipmentProblem );
   
    
    /**
     * 查询设备问题根据id
     *
     * @param id 域名id
     * @return  
     */
    public  EquipmentProblem  selectEquipmentProblemById(String id);
    
   
    /**
     * 修改设备问题
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int updateEquipmentProblem( EquipmentProblem equipmentProblem );
   
    
    
    /**
     * 删除设备问题
     * @param alerts  
     */
    int deleteEquipmentProblem(String id); 
    
    
    
    /**
     * 分页查询设备问题
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    PageBean<EquipmentProblemResp> seleteEquipmentProblemByPage( EquipmentProblemPageRequest equipmentProblemPageRequest);
    
   
    
    /**
     * 审批设备问题
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int approveEquipmentProblem( EquipmentProblem equipmentProblem );
    
 
    /**
     * 插入设备问题列表
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int saveEquipmentProblemList(JSONObject data);
    
    
    
     
    /**
     * 查询设备问题数据
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
   List<EquipmentProblemResp> getEquipmentProblemList( EquipmentProblemRequest  equipmentProblemRequest);
    
  
 
    
}
