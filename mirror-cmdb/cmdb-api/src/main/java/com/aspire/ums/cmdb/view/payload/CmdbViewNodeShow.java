package com.aspire.ums.cmdb.view.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbViewNodeShow
 * Author:   hangfang
 * Date:     2020/5/18
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CmdbViewNodeShow {
    /**
     * 显示子节点信息id
     */
    private String id;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 显示前缀
     */
    private String showPrefix;
    /**
     * SQL配置
     */
    private String showSql;

}
