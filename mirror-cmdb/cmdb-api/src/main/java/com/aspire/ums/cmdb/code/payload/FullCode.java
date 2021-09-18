package com.aspire.ums.cmdb.code.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: FullCode
 * Author:   zhu.juwang
 * Date:     2019/5/20 22:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullCode {
    /*
     *  该类赋值过程在 启动类 CmdbCacheApplicationRunner.run() 中
     *  在启动项目时, 系统会自动加载所有码表数据, 并将其按照分组的方式返回码表完整数据
     *  使用者需在修改、新增、删除码表时, 调用CodeCache.refreshCache()方法 刷新缓存
     *  通过Cache.CODE_MODULE_LIST可获取到缓存值
     */
    private String groupName;
    private List<CmdbCode> codeList;
    @Data
    public static class CmdbCode {
        private com.aspire.ums.cmdb.code.payload.CmdbCode cmdbCode;
        private CmdbControlType controlType;
        private List<CmdbCodeValidate> validateList;
    }
}
