package com.aspire.ums.cmdb.report.service;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReport;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportValue;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
public interface ICmdb31ProvinceReportValueService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportValue> list();

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<String> listUniqueValues(Cmdb31ProvinceReport report, String settingId);


    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceReportValue get(Cmdb31ProvinceReportValue entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(Cmdb31ProvinceReportValue entity);
    /**
     * 新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<Cmdb31ProvinceReportValue> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(Cmdb31ProvinceReportValue entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(Cmdb31ProvinceReportValue entity);

    /**
     * 根据报告ID, 删除上报数据
     * @param reportId 报告ID
     */
    void deleteByReportId(String reportId);

    /**
     * 根据资源归属、省份、月份删除数据
     * @param provinceReport
     */
    void deleteByProvinceAndMonth(Cmdb31ProvinceReport provinceReport);

    /**
     * 删除从界面删除数据
     * @param provinceReport
     */
    void deleteByCurrReportIds(Cmdb31ProvinceReport provinceReport, List<String> reportIdS);
}