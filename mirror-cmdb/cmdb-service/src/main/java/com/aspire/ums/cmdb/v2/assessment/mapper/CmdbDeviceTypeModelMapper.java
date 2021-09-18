package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceType;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceTypeModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDeviceTypeModelMapper
 * Author:   hangfang
 * Date:     2019/12/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbDeviceTypeModelMapper {

    /**
     * 根据设备型号获取实例
     * @return 返回所有实例数据
     */
    CmdbDeviceTypeModel getDeviceTypeByModel(@Param("deviceModel") String deviceModel);

    /**
     * 根据设备类型获取设备型号
     * @return 返回所有实例数据
     */
    List<CmdbDeviceTypeModel> getModelByDeviceType(@Param("deviceType") String deviceType);
}
