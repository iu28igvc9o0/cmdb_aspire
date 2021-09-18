package com.aspire.ums.cmdb.IpResource.mapper;


import com.aspire.ums.cmdb.dict.payload.ConfigDict;

import java.util.List;

/**
 * 数据字典
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/18 10:22
 */
public interface CmdbConfigDictMapper {
    List<ConfigDict> listByEntity(ConfigDict configDict);
}
