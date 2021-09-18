package com.aspire.ums.cmdb.maintenance.mapper;


import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfoQueryRequest;

import java.util.List;

/**
 * 描述： 部件信息数据层接口
 * @author
 * @date 2020-02-06 22:31:46
 */
public interface CmdbComponentInfoMapper {

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
    List<CmdbComponentInfo> list(CmdbComponentInfoQueryRequest request);

    /*
    *  查询部件列表的数量
    * */
    Integer listByCount(CmdbComponentInfoQueryRequest request);
}
