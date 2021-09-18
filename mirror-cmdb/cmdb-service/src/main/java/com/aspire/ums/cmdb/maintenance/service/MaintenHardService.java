package com.aspire.ums.cmdb.maintenance.service;

import java.util.List;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.entity.MaintenHardware;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardPageRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardPageResp;
 
 

/**
 * 硬件维保
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.service
 * 类名称:    MaintenManageService.java
 * 类描述:    硬件维保业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface MaintenHardService {
	
	
    /**
     * 创建硬件维保
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int insertMaintenHardware(MaintenHardware maintenHardware );
    
    
    /**
     * 修改硬件维保
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int updateMaintenHardware( MaintenHardware maintenHardware );
    
    
    /**
     * 批量修改硬件维保
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int batchUpdateMaintenHardware( MaintenHardware maintenHardware );
    
    
    
    /**
     * 查询硬件维保根据id
     *
     * @param id 域名id
     * @return  
     */
    public  MaintenHardware  selectMaintenHardwareById(String id);
    
   
    /**
     * 查询硬件维保根据设备型号
     *
     * @param 软件名称 
     * @return  
     */
    public  MaintenHardware  selectMaintenHardwareByDeviceModel(  String maintenFactory, String deviceModel,
             String warrantyDate );
    
    
    
    
    /**
     * 删除硬件维保
     * @param alerts  
     */
    int deleteMaintenHardwareById(String ids); 
    
    
    
    /**
     * 分页查询硬件维保
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    PageBean<MaintenHardPageResp> selectMaintenHardByPage( MaintenHardPageRequest maintenHardPageRequest);
    
    
    /**
     * 导出硬件维保数据
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
   List<MaintenHardPageResp> getMaintenHardwareList( MaintenHardPageRequest maintenHardPageRequest);
    
  
   
   /**
    * 插入硬件维保
    *
    * @param alertsDTO 告警对象
    * @return String 告警ID
    */
   int insertMaintenHardwareList( List<MaintenHardware>  maintenHardwareList );
   
   
    
    
}
