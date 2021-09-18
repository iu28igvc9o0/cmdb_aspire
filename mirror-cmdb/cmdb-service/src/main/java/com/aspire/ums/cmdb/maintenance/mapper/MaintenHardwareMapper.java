package com.aspire.ums.cmdb.maintenance.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.maintenance.entity.MaintenHardware;

 
/**
 * 硬件维保 
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.mapper
 * 类名称:    MaintenHardwareMapper.java
 * 类描述:    硬件维保
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface MaintenHardwareMapper {
	
	
    /**
     * 新增硬件维保
     * @param maintenHardware 硬件维保
     */
    int insertMaintenHardware(MaintenHardware  maintenHardware);
    
    
    /**
     * 批量新增硬件维保
     * @param maintenHardwareList 硬件维保
     */
    int saveMaintenHardwareList( List<MaintenHardware>  maintenHardwareList);
    
    
    /**
     * 修改硬件维保
     * @param maintenHardware 硬件维保
     */
    int updateMaintenHardware(MaintenHardware  maintenHardware);
    
    
    /**
     * 批量修改硬件维保
     * @param maintenHardware 硬件维保
     */
    int batchUpdateMaintenHardware(Map<String, Object> hashmap); 
    
    
    /**
     * 查询硬件维保根据id
     * @param id 硬件维保id 
     */
     MaintenHardware  selectMaintenHardwareById (String id);
    
    
     
     /**
      * 查询硬件维保根据软件名称
      * @param id   
      */
     List<MaintenHardware>  selectMaintenHardwareByDeviceModel (Map<String, Object> hashmap);
     
     
    /**
     * 删除多条数据
     * @param alerts  
     */
    int deleteMaintenHardwareIds(List<String>  idList);
    
    
    
    /**
     * 查询分页数量
     * @param   
     */
    int getMaintenHardwareCount(Map<String, Object> hashmap);
    
   
    /**
     * 查询分页数据
     * @param   
     */
    List<MaintenHardware> getMaintenHardwareByPage(Map<String, Object> hashmap);
    
    
    /**
     * 导出数据
     * @param   
     */
    List<MaintenHardware> getMaintenHardwareList(Map<String, Object> hashmap);
    
    

}
