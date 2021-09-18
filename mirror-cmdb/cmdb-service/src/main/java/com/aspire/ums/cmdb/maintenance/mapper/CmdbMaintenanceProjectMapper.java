package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProject;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:46
*/
@Mapper
public interface CmdbMaintenanceProjectMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProject> list();

    /**
     * 获取所有实例,部分列表属性
     * @return 返回所有实例数据
     */
    List<Map<String,Object>> getSimpleList(CmdbMaintenanceProjectQuery entity);

    Integer getSimpleListCount(CmdbMaintenanceProjectQuery entity);

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbMaintenanceProject> listByEntity(CmdbMaintenanceProjectQuery entity);


    /**
     * 返回查询总记录数
     * @param entity
     * @return
     */
    Integer listByEntityCount(CmdbMaintenanceProjectQuery entity);

    /**
     * 获取所有实例(不包括与金额相关的字段)
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<Map<String,Object>> listNotMoney(CmdbMaintenanceProjectQuery entity);

    /**
     * 获取列表数量(不包括与金额相关的字段)
     * @param entity 实例信息
     * @return 返回所有数据的数量
     */
    Integer listNotMoneyCount(CmdbMaintenanceProjectQuery entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbMaintenanceProject get(CmdbMaintenanceProject entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbMaintenanceProject entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbMaintenanceProject entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbMaintenanceProject entity);

    /**
     *  导出维保项目数据源
     * @return
     */
    List<Map<String, Object>> exportProjectList(CmdbMaintenanceProjectQuery query);

    /**
     * 根据设备序列号查询有效的维保项目
     * @param deviceSn 设备序列号
     * @return
     */
    List<CmdbMaintenanceProject> getValidProjectByDeviceSn(@Param("deviceSn") String deviceSn);

    /**
     * 查询维保项目厂家及联系人
     * @param projectId
     * @return
     */
    List<Map<String, String>> getProjectProduceConcat(@Param("projectId") String projectId,@Param("instanceList") List<String> list);

    /*
    *  依据服务时间和设备序列号反查维保项目
    *  @param time 服务时间
    *  @param sn 序列号
    *  @return
    * */
    List<Map<String, Object>> getMaintenObjByTimeAndSn(Map<String,String> mpValue);
}