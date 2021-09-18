package com.aspire.cmdb.agent.collect.entity;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncSuyanRelation
 * Author:   hangfang
 * Date:     2020/9/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class CmdbSyncSuyanRelation {

    private String suyanPodId;
    private String suyanPodName;
    private String suyanProjectName;
    private String suyanIdcName;
    private String umsPodId;
    private String umsIdcId;
    private String umsProjectId;

}
