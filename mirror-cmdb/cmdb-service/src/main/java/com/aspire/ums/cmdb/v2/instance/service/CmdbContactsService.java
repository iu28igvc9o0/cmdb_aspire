package com.aspire.ums.cmdb.v2.instance.service;

import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;

import java.util.List;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 15:35
 */
public interface CmdbContactsService {

    /**
     * 根据实例ID查询接口人信息
     * @param param
     * @return
     */
    List<CmdbContactsResponse> findContactsById(Map<String, Object> param);
}
