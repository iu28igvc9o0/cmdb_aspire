package com.aspire.ums.cmdb.v2.assessment.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.maintenance.entity.MaintenHardware;
import com.aspire.ums.cmdb.v2.assessment.entity.EquipmentProblem;

 
 
@Mapper
public interface EquipmentProblemMapper {
	
	
    /**
     * 新增设备问题
     */
    int insertEquipmentProblem(List<EquipmentProblem>  equipmentProblems);
   
    
    /**
     * 查询设备问题根据id
      
     */
     EquipmentProblem  selectEquipmentProblemById (String id);
    
    
     /**
      * 修改设备问题
      */
     int updateEquipmentProblem(EquipmentProblem  equipmentProblem);
     
     
     /**
      * 删除设备问题
      * @param alerts  
      */
     int deleteEquipmentProblem (String id);

     /**
      * 删除设备问题
      * @param ids
      */
     int deleteByBatch (List<String> ids);
     
     
     /**
      * 审批设备问题
      */
     int approveEquipmentProblem(Map<String, Object> hashmap);
     
     
     /**
      * 查询分页数量
      * @param   
      */
     int getEquipmentProblemCount(Map<String, Object> hashmap);
     
    
     /**
      * 查询分页数据
      * @param   
      */
     List<EquipmentProblem> selectEquipmentProblemByPage(Map<String, Object> hashmap);
     
     
     /**
      * 批量新增设备问题
      * @param  
      */
     int saveEquipmentProblemList( List<EquipmentProblem>  equipmentProblemList);
     
     
     /**
      * 导出设备问题
      * @param   
      */
     List<EquipmentProblem> getEquipmentProblemList(Map<String, Object> hashmap);   
    
    
    

}
