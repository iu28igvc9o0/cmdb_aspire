package com.aspire.ums.cmdb.v3.module.payload;

import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.module.payload.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCiRelation
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbV3ModuleCiRelationDetailResponse {

    /**
     * 关系id
     */
    private String id;
    /**
     * 关联关系
     */
    private String relation;
    /**
     * 关联关系类型
     */
    private String relationType;
    /**
     * 关联资源名称(例：宿主机)
     */
    private String resourceName;
    /**
     * 字段关联
     */
    private List<CmdbV3ModuleCiCodeRelation> codeRelationList;

    private Object relationCiList;

    private List<Object> headerList;

}
