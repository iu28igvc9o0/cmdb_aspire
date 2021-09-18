package com.aspire.ums.cmdb.v2.runner;

import com.aspire.ums.cmdb.v2.cache.CodeCache;
import com.aspire.ums.cmdb.v2.cache.InstanceCache;
import com.aspire.ums.cmdb.v2.cache.ModuleCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbPartitionApplicationRunner
 * Author:   zhu.juwang
 * Date:     2019/4/12 16:53
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * 自动分区处理
 */
//@Component
@Order(3)
@Slf4j
public class CmdbCacheApplicationRunner implements ApplicationRunner {

    @Autowired
    private ModuleCache moduleCache;
    @Autowired
    private CodeCache codeCache;
    @Autowired
    private InstanceCache instanceCache;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        // todo 刷新redis缓存
//        //刷新 模型缓存
//        moduleCache.refreshCache();
//        //刷新 码表缓存
//        codeCache.refreshCache();
//        //刷新CI模型 所属列 列表
//        instanceCache.refreshCache();
    }
}
