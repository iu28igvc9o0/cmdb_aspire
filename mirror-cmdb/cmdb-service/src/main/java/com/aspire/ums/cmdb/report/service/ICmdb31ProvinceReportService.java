package com.aspire.ums.cmdb.report.service;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceInsertReq;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReport;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportReq;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
public interface ICmdb31ProvinceReportService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReport> list();

    /**
     * 获取工单数据-提供给BPM
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> listOrderReportData(String submitMonth);
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
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(Cmdb31ProvinceReport entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(Cmdb31ProvinceReport entity);

    /**
     * 新增上报省份数据
     * @param insertVOListMap
     * @return
     */
    void save(String userName,Map<String,Object> insertVOListMap);

    /**
     * （导入）新增上报省份数据
     *
     * [{
     *   idc_type: 'xx',
     *   settingId01: reportValue01,
     *   settingId02: reportValue02,
     *   settingId03: reportValue03
     * }]
     * @param insertReq
     * @return
     */
    void saveByImport(String userName, Cmdb31ProvinceInsertReq insertReq);

    /**
     * 获取省份上报数据
     * @param req
     * @return
     */
    Map<String, List<Map<String, Object>>> getSettingData(Cmdb31ProvinceReportReq req);


    /**
     * 提交上报数据状态
     * @param queryParams 参数
     * @return
     */
    Map<String, String> submitApprove(Map<String, String> queryParams);

    Map<String, String> submitCheck(Map<String, String> queryParams);

    List<Map<String, String>> getProvinceStatus(Map<String, String> queryParam);
}