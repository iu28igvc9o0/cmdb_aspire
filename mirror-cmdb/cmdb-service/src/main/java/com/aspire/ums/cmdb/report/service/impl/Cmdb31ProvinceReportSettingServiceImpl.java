package com.aspire.ums.cmdb.report.service.impl;

import com.aspire.ums.cmdb.report.mapper.Cmdb31ProvinceReportSettingMapper;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportSettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
@Service
@Slf4j
public class Cmdb31ProvinceReportSettingServiceImpl implements ICmdb31ProvinceReportSettingService {

    @Autowired
    private Cmdb31ProvinceReportSettingMapper mapper;
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<Cmdb31ProvinceReportSetting> list() {
        return mapper.list();
    }

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<Cmdb31ProvinceReportSetting> listByOwner(String resourceOwner) {
        return mapper.listByOwner(resourceOwner);
    }

    @Override
    public List<Cmdb31ProvinceReportSetting> listByTableId(String tableId, String showPage) {
        return mapper.listByTableId(tableId, showPage);
    }

    @Override
    public List<Cmdb31ProvinceReportSetting> listByTableIds(List<String> tableIds, String showPage) {
        return mapper.listByTableIds(tableIds, showPage);
    }

    @Override
    public List<Cmdb31ProvinceReportSetting> listByTableUnique(String tableId) {
        return mapper.listByTableUnique(tableId);
    }

//    @Override
//    public List<Cmdb31ProvinceReportSetting> getListByQueryParam(M<>) {
//        Cmdb31ProvinceReportSetting setting = new Cmdb31ProvinceReportSetting();
//        setting.setResourceOwner(resourceOwner);
//        return mapper.listByEntity(setting);
//    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public Cmdb31ProvinceReportSetting get(Cmdb31ProvinceReportSetting entity) {
        return mapper.get(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param id 实例信息
     * @return 返回指定ID的数据信息
     */
    public Cmdb31ProvinceReportSetting getById(String id) {
        return mapper.getById(id);
    }
    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(Cmdb31ProvinceReportSetting entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(Cmdb31ProvinceReportSetting entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(Cmdb31ProvinceReportSetting entity) {
        mapper.delete(entity);
    }


    /**
     * 获取表头
     */
    @Override
    public Map<String, Object> getSetting(Map<String, String> queryParams) {
        Map<String, Object> returnMap = new LinkedMap();
        return returnMap;
    }

//    @Override
//    public List<Cmdb31ProvinceReportSetting> getSettingByQuery(Map<String, String> queryParams) {
//        return mapper.getByQuery(queryParams);
//    }

    @Override
    public List<Cmdb31ProvinceReportSetting> getSettingByCalc(String resourceOwner, String partOfCalc) {
        return mapper.getSettingByCalc(resourceOwner, partOfCalc);
    }
}