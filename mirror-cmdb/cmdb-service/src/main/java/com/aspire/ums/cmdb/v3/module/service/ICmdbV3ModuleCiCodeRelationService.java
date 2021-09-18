package com.aspire.ums.cmdb.v3.module.service;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiCodeRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCiCodeRelationService
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbV3ModuleCiCodeRelationService {

    void save(List<CmdbV3ModuleCiCodeRelation> ciCodeRelation);

    List<CmdbV3ModuleCiCodeRelation> listByRelationId(String relationId);

    void deleteByRelationId(String relationId);

    void deleteByModuleId(String moduleId);
}
