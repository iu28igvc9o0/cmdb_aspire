package com.aspire.ums.cmdb.report.service;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
public interface ICmdb31ProvinceReportSettingService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> list();

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByOwner(String resourceOwner);

    /**
     * 获取表下所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByTableId(String tableId, String showPage);

    /**
     * 获取表下所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByTableIds(List<String> tableIds, String showPage);


    /**
     * 获取表下所有唯一列
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByTableUnique(String tableId);
//    /**
//     * 根据条件获取列
//     * {resourceOwner: resource
//     * type: update/query
//     * }
//     * @return 返回所有实例数据
//     */
//    List<Cmdb31ProvinceReportSetting> getListByQueryParam(Map<String, String> queryParams);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceReportSetting get(Cmdb31ProvinceReportSetting entity);

    /**
     * 根据主键ID 获取数据信息
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceReportSetting getById(String id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(Cmdb31ProvinceReportSetting entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(Cmdb31ProvinceReportSetting entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(Cmdb31ProvinceReportSetting entity);

    /**
     * 获取上报数据配置
     * @param queryParams
     * @return
     */
    Map<String, Object> getSetting(Map<String, String> queryParams);
//    /**
//     * 根据资源所属和表名获取上报数据配置
//     * @param queryParams
//     * @return
//     */
//    List<Cmdb31ProvinceReportSetting> getSettingByQuery(Map<String, String> queryParams);

    /**
     * 获取需要计算的列配置
     * @param partOfCalc
     * @return
     */
    List<Cmdb31ProvinceReportSetting> getSettingByCalc(String resourceOwner, String partOfCalc);
}