package com.aspire.ums.cmdb.service;

import net.sf.json.JSONArray;

import java.io.IOException;

/**
 * 同步接口信息表(OrgSyncInterface)表服务接口
 *
 * @author makejava
 * @since 2020-12-03 15:44:10
 */

public interface OrgSyncInterfaceService {

     void syncOrgData(String date) throws Exception;


    com.alibaba.fastjson.JSONObject getSuZhouOautToken() throws IOException;

    JSONArray getSuYanData(String s) throws Exception;

    void syncUserData(String syncData) throws Exception;

    void syncAddUserData(String syncDate) throws Exception;

    void syncAddOrgData(String syncTime) throws Exception;



}