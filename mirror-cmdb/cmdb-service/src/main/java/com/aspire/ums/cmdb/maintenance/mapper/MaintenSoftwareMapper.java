package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 *  软件维保 
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.mapper
 * 类名称:    MaintenSoftwareMapper.java
 * 类描述:    软件维保
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface MaintenSoftwareMapper {
	
	
    /**
     * 新增软件维保
     * @param maintenSoftware 软件维保
     */
    int insertMaintenSoftware(MaintenSoftware  maintenSoftware);
    
    
//    /**
//     * 修改软件维保
//     * @param maintenSoftware 软件维保
//     */
//    int updateMaintenSoftware(MaintenSoftware  maintenSoftware);
//
//
//    /**
//     * 批量修改软件维保
//     * @param maintenSoftware 软件维保
//     */
//    int batchUpdateMaintenSoftware(Map<String, Object> hashmap);
    
    
    /**
     * 查询软件维保根据id
     * @param id 软件维保id 
     */
     MaintenSoftware  selectMaintenSoftwareById (String id);
   
     /**
      * 查询软件维保根据软件名称
      * @param id   
      */
     List<MaintenSoftware>  selectMaintenSoftwareBySoftNmae (Map<String, Object> hashmap);
        
     
    /**
     * 删除多条数据
     * @param alerts  
     */
    void deleteMaintenSoftwareIds(List<String>  idList);
    
    
    
    /**
     * 查询分页数量
     * @param   
     */
    int getMaintenSoftwareCount(Map<String, Object> query);
    
   
    /**
     * 查询分页数据
     * @param   
     */
    List<MaintenSoftware> getMaintenSoftwareByPage(Map<String, Object> query);
    
    
    
    /**
     * 新增软件维保列表
     * @param maintenSoftwareList 软件维保
     */
    int insertMaintenSoftwareList( List<MaintenSoftware>  maintenSoftwareList);
    
    
//    /**
//     * 导出数据
//     * @param
//     */
//    List<MaintenSoftware> getMaintenSoftwareList(Map<String, Object> hashmap);
//
       

}
