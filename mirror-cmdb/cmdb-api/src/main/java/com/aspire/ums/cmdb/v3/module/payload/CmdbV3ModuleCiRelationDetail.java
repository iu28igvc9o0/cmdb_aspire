package com.aspire.ums.cmdb.v3.module.payload;

import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.module.payload.Module;
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
public class CmdbV3ModuleCiRelationDetail {

    /**
     * 关系id
     */
    private String id;
    /**
     * 当前模型id
     */
    private SimpleModule curModule;
    /**
     * 关联类型
     */
    private Dict relationTypeDict;
    /**
     * 关联关系字典id
     */
    private Dict relationDict;
    /**
     * 关联模型id
     */
    private SimpleModule relationModule;
    /**
     * 关联资源名称(例：宿主机)
     */
    private String resourceName;
    /**
     * 关联sql
     */
    private String relationSql;
    /**
     * 字段关联
     */
    private List<CmdbV3ModuleCiCodeRelationDetail> codeRelationList;
}
