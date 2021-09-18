package com.aspire.ums.cmdb.v3.module.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbV3ModuleBusinessQuotaMapper
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-04 15:49
 **/
@Mapper
public interface CmdbV3ModuleBusinessQuotaMapper {

    List<Map<String,Object>> getAllBusinessQuotaInfo();

    List<Map<String,Object>> getNeedChargeBusinessQuotaInfo();
}
