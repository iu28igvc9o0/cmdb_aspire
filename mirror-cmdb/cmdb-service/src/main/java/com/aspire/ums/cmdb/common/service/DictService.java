package com.aspire.ums.cmdb.common.service;

import com.aspire.ums.cmdb.common.entity.DictEntity;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DictService
 * Author:   zhu.juwang
 * Date:     2019/3/12 9:09
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface DictService {
    /**
     * 根据字典值编码，查询字典值列表
     * @param code 字典编码
     * @return 字典值列表
     */
    List<DictEntity> getDictByCode(String code);
}
