package com.aspire.ums.cmdb.maintenance.service;

import java.util.List;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageResp;
import org.springframework.web.bind.annotation.RequestParam;


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
     * @param maintenSoftware 软件维保
     * @return String 告警ID
     */
    void insertMaintenSoftware(MaintenSoftware maintenSoftware );

     /**
     * 查询软件维保根据软件名称
     * @param
     * @param project
     * @param softwareName
     * @return
     */
    MaintenSoftware selectMaintenSoftwareBySoftNmae(String project ,String softwareName);

//    /**
//     * 修改软件维保
//     *
//     * @param maintenSoftware 告警对象
//     * @return String 告警ID
//     */
//    int updateMaintenSoftware( MaintenSoftware maintenSoftware );
    
    
//    /**
//     * 批量修改软件维保
//     *
//     * @param maintenSoftware 告警对象
//     * @return String 告警ID
//     */
//    int batchUpdateMaintenSoftware( MaintenSoftware maintenSoftware );
    
    
    
    /**
     * 查询软件维保根据id
     *
     * @param id 域名id
     * @return  
     */
    MaintenSoftware  selectMaintenSoftwareById(String id);
    
    
    

    /**
     * 删除软件维保
     * @param ids
     */
    void deleteMaintenSoftwareById(String ids);
    
    
    
    /**
     * 分页查询软件维保
     *
     * @param maintenSoftPageRequest 告警对象
     * @return String 告警ID
     */
    PageBean<MaintenSoftPageResp> selectMaintenSoftByPage( MaintenSoftPageRequest maintenSoftPageRequest);
    
    
//    /**
//     * 导出软件维保数据
//     *
//     * @param maintenSoftPageRequest 告警对象
//     * @return String 告警ID
//     */
//   List<MaintenSoftPageResp> getMaintenSoftwareList( MaintenSoftPageRequest maintenSoftPageRequest);
//
//
   
   /**
    * 插入软件维保
    *
    * @param maintenSoftwareList 告警对象
    * @return String 告警ID
    */
   void insertMaintenSoftwareList( List<MaintenSoftware>  maintenSoftwareList );
   
   
    
    
}
