package com.aspire.ums.cmdb.report.mapper;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReport;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceOverviewMapper
 * Author:   hangfang
 * Date:     2020/5/7
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface Cmdb31ProvinceOverviewMapper {
    /**
     * 资源数据汇总
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> getResourceView(@Param("tableIds") List<String> tableIds,
                                              @Param("submitMonth") String submitMonth,
                                         @Param("settingList") List<Cmdb31ProvinceReportSetting> settingList);
    /**
     * 各省平台可用性汇总
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> getUsabilityView(@Param("tableIds") List<String> tableIds,
                                               @Param("submitMonth") String submitMonth,
                                         @Param("settingList") List<Cmdb31ProvinceReportSetting> settingList);

    /**
     * 各省工单处理及时率汇总
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> getHandleTimeView(@Param("tableIds") List<String> tableIds,
                                                @Param("submitMonth") String submitMonth,
                                               @Param("settingList") List<Cmdb31ProvinceReportSetting> settingList);

    /**
     * 各表按各省按行分组统计数据
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> getDefaultView(@Param("tableIds") List<String> tableIds,
                                                @Param("submitMonth") String submitMonth,
                                                @Param("settingList") List<Cmdb31ProvinceReportSetting> settingList);

}
