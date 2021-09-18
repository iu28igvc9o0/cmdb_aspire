package com.aspire.ums.cmdb.maintenance.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.allocate.util.PageBean;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftPageRequest;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftPageResp;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftwareResp;
 


/**
 * 软件维保
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.service
 * 类名称:    MaintenSoftService.java
 * 类描述:    软件维保业务层接口
 */
public interface MaintenSoftService {
	
	
    /**
     * 创建软件维保
     *
     * @param alertsDTO 软件维保
     * @return String 告警ID
     */
    int insertMaintenSoftware(MaintenSoftware maintenSoftware );
    
    
    /**
     * 修改软件维保
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int updateMaintenSoftware( MaintenSoftware maintenSoftware );
    
    
    /**
     * 批量修改软件维保
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    int batchUpdateMaintenSoftware( MaintenSoftware maintenSoftware );
    
    
    
    /**
     * 查询软件维保根据id
     *
     * @param id 域名id
     * @return  
     */
    public  MaintenSoftware  selectMaintenSoftwareById(String id);
    
    
    
    /**
     * 查询软件维保根据软件名称
     *
     * @param 软件名称 
     * @return  
     */
    public  MaintenSoftware  selectMaintenSoftwareBySoftNmae( String company ,String project ,String softwareName);
    
    
    /**
     * 删除软件维保
     * @param alerts  
     */
    int deleteMaintenSoftwareById(String ids); 
    
    
    
    /**
     * 分页查询软件维保
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
    PageBean<MaintenSoftPageResp> selectMaintenSoftByPage( MaintenSoftPageRequest maintenSoftPageRequest);
    
    
    /**
     * 导出软件维保数据
     *
     * @param alertsDTO 告警对象
     * @return String 告警ID
     */
   List<MaintenSoftPageResp> getMaintenSoftwareExcelData( MaintenSoftPageRequest maintenSoftPageRequest);
    
  
   
   /**
    * 插入软件维保
    *
    * @param alertsDTO 告警对象
    * @return String 告警ID
    */
   int insertMaintenSoftwareList( List<MaintenSoftware>  maintenSoftwareList );
   
   
    
    
}
