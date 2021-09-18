package com.aspire.ums.cmdb.v3.module.payload;

import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CmdbV3ModuleCiCodeRelationDetail {

    /**
     * 字段关联关系id
     */
    private String id;

    /**
     * 关系id
     */
    private String relationId;

    /**
     * 当前模型码表id
     */
    private CmdbSimpleCode currCode;

    /**
     * 关联模型码表id
     */
    private CmdbSimpleCode relationCode;

    /**
     * 排序
     */
    private Integer sortIndex;

}
