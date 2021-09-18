package com.aspire.ums.cmdb.v2.collectUnknown.mapper;

import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCollectUnknownMapper
 * Author:   hangfang
 * Date:     2019/10/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbCollectUnknownMapper {

    /**
     * 根据条件筛选未知采集设备
     */
    List<CmdbCollectUnknown> list(CmdbCollectUnknownQuery collectUnknownQuery);

    /**
     * 根据条件筛选未知采集设备数量
     */
    Integer listCount(CmdbCollectUnknownQuery collectUnknownQuery);

    /**
     * 查询是否存在未处理或被屏蔽数据
     */
    Integer countShieldAndUnhand(@Param(value = "ip") String ip,
                                 @Param(value = "idcType") String idcType);

    /**
     * 查询某条数据
     */
    CmdbCollectUnknown get(@Param(value = "id") String id);


    /**
     * 新增未知采集设备
     */
    void insert(CmdbCollectUnknown collectUnknown);

    /**
     * 更新未知采集设备
     */
    void update(CmdbCollectUnknown collectUnknown);
}
