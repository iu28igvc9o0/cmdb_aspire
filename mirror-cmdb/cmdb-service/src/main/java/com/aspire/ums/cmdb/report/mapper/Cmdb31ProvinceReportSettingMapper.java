package com.aspire.ums.cmdb.report.mapper;

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
public interface Cmdb31ProvinceReportSettingMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> list();

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByOwner(@Param("resourceOwner") String resourceOwner);

    /**
     * 获取所有实例
     * @param tableId 实例信息
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByTableId(@Param("tableId") String tableId,
                                                    @Param("showPage") String showPage);
    /**
     * 获取所有实例
     * @param tableIds 实例信息
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByTableIds(@Param("tableIds") List<String> tableIds,
                                                    @Param("showPage") String showPage);

    /**
     * 获取表中唯一实例
     * @param tableId 实例信息
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByTableUnique(@Param("tableId") String tableId);

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceReportSetting> listByEntity(Cmdb31ProvinceReportSetting entity);

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
     * 根据查询条件获取数据
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    List<Cmdb31ProvinceReportSetting> getByQuery(Map<String, String> entity);

    /**
     * 根据calc获取配置数据
     * @param partOfCalc 实例信息
     * @return 返回实例信息的数据信息
     */
    List<Cmdb31ProvinceReportSetting> getSettingByCalc(@Param("resourceOwner") String resourceOwner, @Param("partOfCalc") String partOfCalc);

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
}