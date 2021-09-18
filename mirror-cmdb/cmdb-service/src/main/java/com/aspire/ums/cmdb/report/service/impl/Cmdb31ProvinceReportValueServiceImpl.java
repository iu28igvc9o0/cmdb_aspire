package com.aspire.ums.cmdb.report.service.impl;

import com.aspire.ums.cmdb.report.mapper.Cmdb31ProvinceReportValueMapper;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReport;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportValue;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
@Service
public class Cmdb31ProvinceReportValueServiceImpl implements ICmdb31ProvinceReportValueService {

    @Autowired
    private Cmdb31ProvinceReportValueMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<Cmdb31ProvinceReportValue> list() {
        return mapper.list();
    }

    @Override
    public List<String> listUniqueValues(Cmdb31ProvinceReport report, String settingId) {
        return mapper.listUniqueValues(report, settingId);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public Cmdb31ProvinceReportValue get(Cmdb31ProvinceReportValue entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(Cmdb31ProvinceReportValue entity) {
        mapper.insert(entity);
    }
    /**
     * 新增实例
     * @param entities 实例数据
     * @return
     */
    public void insertByBatch(List<Cmdb31ProvinceReportValue> entities) {
        mapper.insertByBatch(entities);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(Cmdb31ProvinceReportValue entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(Cmdb31ProvinceReportValue entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByReportId(String reportId) {
        mapper.deleteByReportId(reportId);
    }

    @Override
    public void deleteByProvinceAndMonth(Cmdb31ProvinceReport provinceReport) {
        mapper.deleteByProvinceAndMonth(provinceReport);
    }

    @Override
    public void deleteByCurrReportIds(Cmdb31ProvinceReport provinceReport, List<String> reportIds) {
        mapper.deleteByCurrReportIds(provinceReport, reportIds);
    }
}