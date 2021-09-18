package com.aspire.ums.cmdb.module.payload;

import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeGroup;
import com.aspire.ums.cmdb.code.payload.FullCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: FullModule
 * Author:   zhu.juwang
 * Date:     2019/5/20 21:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullModule {
    /*
     *  该类赋值过程在 启动类 CmdbCacheApplicationRunner.run() 中
     *  在启动项目时, 系统会自动加载所有模型的所有数据, 并将其返回一个完整的Module对象
     *  使用者需在修改、新增、删除模型结构时, 调用ModuleCache.refreshCache()方法 刷新缓存
     *  通过Cache.CACHE_MODULE_LIST可获取到缓存值
     */
    private Module module;
    private List<FullModule> childModules = new LinkedList<>();
    private List<CmdbGroup> groupList = new LinkedList<>();
    @Data
    public static class CmdbGroup {
        private CmdbModuleCodeGroup codeGroup;
        private List<CmdbCode> codeList = new LinkedList<>();
    }
    @Data
    public static class CmdbCode {
        private FullCode.CmdbCode cmdbCode;
        private String defaultValue;
        private String colSpan;
        private Integer sortIndex;
    }
}
