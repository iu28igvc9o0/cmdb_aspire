package com.aspire.ums.cmdb.report.mapper;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReport;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportReq;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
@Mapper
public interface Cmdb31ProvinceReportMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReport> list();


    /**
     * 获取工单数据-提供给BPM
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> listOrderReportData(@Param("submitMonth") String submitMonth);
    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReport> listByEntity(Cmdb31ProvinceReport entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceReport get(Cmdb31ProvinceReport entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(Cmdb31ProvinceReport entity);
    /**
     * 新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<Cmdb31ProvinceReport> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(Cmdb31ProvinceReport entity);

    /**
     * 修改审核结果
     * @param entity 实例数据
     */
    void updateApproveStatus(Cmdb31ProvinceReport entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(Cmdb31ProvinceReport entity);
    /**
     * 根据当前id对比删除不存在的report
     * @param ids 实例数据
     * @return
     */
    void deleteByCurrIds(@Param("reportInfo") Cmdb31ProvinceReport reportInfo,
                         @Param("ids") List<String> ids);


    /**
     * 查询上报数据
     * @param req
     * @return
     */
    List<Map<String, Object>> getSettingData(@Param("query") Cmdb31ProvinceReportReq req,@Param("settingList") List<Cmdb31ProvinceReportSetting> settingList);
    /**
     * 按资源池去查询上报数据
     * @param queryParams
     * @return
     */
    List<Map<String, Object>> getSettingDataByIdcType(Map<String, Object> queryParams);

    /**
     * 按表去查询上报数据
     * @param queryParams
     * @return
     */
    List<Map<String, Object>> getSettingDataByResourceClass(Map<String, Object> queryParams);

    /**
     * 按表去查询上报数据
     * @param queryParams
     * @return
     */
    List<Map<String, String>> executeToCountTotal(Map<String, Object> queryParams);

    /**
     * 获取表名
     * @param resourceOwner
     * @return
     */
    List<String> getResourceClass(@Param("resourceOwner") String resourceOwner);
    /**
     * 根据省份和月份删除
     * @param provinceReport
     */
    void deleteByProvinceAndMonth(Cmdb31ProvinceReport provinceReport);

    /**
     * 根据每行id删除
     * @param reportId
     */
    void deleteByReportId(@Param("reportId") String reportId);

    /**
     * 检查省份上报数据是否完整
     * @param queryParams
     * @return
     */
    List<Map<String, String>> submitCheck(Map<String, String> queryParams);


    /**
     * 获取省份上报状态
     * @ param queryParams
     * @return
     */
    List<Map<String, String>> getProvinceStatus(Map<String, String> queryParams);
}