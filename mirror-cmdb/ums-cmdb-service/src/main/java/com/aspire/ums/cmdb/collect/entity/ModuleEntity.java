package com.aspire.ums.cmdb.collect.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ModuleEntity
 * Author:   zhu.juwang
 * Date:     2019/3/12 19:05
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
public class ModuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String code;
    private String parentid;
    private String iconurl;
    private Integer sortindex;
    private String disabled;
    private String builtin;
    private Date inserttime;
    private Date updatetime;
    private Integer isdelete;
}
