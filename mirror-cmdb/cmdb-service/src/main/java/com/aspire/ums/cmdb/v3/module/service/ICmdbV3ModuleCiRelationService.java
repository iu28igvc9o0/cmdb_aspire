package com.aspire.ums.cmdb.v3.module.service;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetail;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetailResponse;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCiRelationService
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbV3ModuleCiRelationService {

    List<CmdbV3ModuleCiRelation> listByModuleId(String moduleId);

    List<CmdbV3ModuleCiRelationDetail> listRDetailByModuleId(String moduleId);

    void save(String userName, CmdbV3ModuleCiRelation relation);

    void update(String userName, CmdbV3ModuleCiRelation relation);

    void deleteById(String id);

    void deleteByModuleId(String moduleId);

    CmdbV3ModuleCiRelationDetailResponse getCiRelationDetail(String relationId, String instanceId);
}
