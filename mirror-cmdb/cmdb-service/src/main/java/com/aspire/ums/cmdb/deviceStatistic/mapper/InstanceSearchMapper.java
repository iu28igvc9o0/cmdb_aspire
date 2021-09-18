package com.aspire.ums.cmdb.deviceStatistic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

 
 
@Mapper
public interface InstanceSearchMapper {

    
	 /**
     * 查询分页数量
     * @param   
     */
    int getInstanceSearchCount(Map<String, Object> hashmap);
    
   
    /**
     * 查询分页数据
     * @param   
     */
    List<Map<String, Object>> getInstanceSearchByPage(Map<String, Object> hashmap);


    List<Map<String,String>> selectInstanceByAuth(Map<String, List<String>> queryMap);
     
    
    Map<String, Object> selectDepartment(@Param("ip") String ip,@Param("bizSystem") String bizSystem);
    
    
    List<Map<String,Object>> selectbizSystemConcatList( @Param("bizSystem") String bizSystem );
    
}
