package com.aspire.ums.cmdb.maintenance.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfoQueryRequest;

import java.util.List;

/**
 * 描述： 部件信息服务层接口
 * @author
 * @date 2020-02-07 15:31
 */
public interface ICmdbComponentInfoService {

    /*
     *  增加部件信息
     * */
    void save(CmdbComponentInfo obj);
    /*
     *  删除部件信息
     * */
    void delete(CmdbComponentInfo obj);
    /*
     *  修改部件信息
     * */
    void update(CmdbComponentInfo obj);
    /*
     *  查询部件信息
     * */
    Result<CmdbComponentInfo> list(CmdbComponentInfoQueryRequest request);
}
