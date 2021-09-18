package com.aspire.ums.cmdb.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DictEntity
 * Author:   zhu.juwang
 * Date:     2019/3/12 8:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
public class DictEntity {
    /*
    ID字段
     */
    private String id;
    /*
    字典编码
     */
    private String code;
    /*
    字典名称
     */
    private String name;
    /*
    字典值
     */
    private String value;
    /*
    字典值显示顺序
     */
    private Integer displayOrder;
}
