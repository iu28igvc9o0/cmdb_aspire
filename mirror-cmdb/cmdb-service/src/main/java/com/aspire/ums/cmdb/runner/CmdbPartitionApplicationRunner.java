package com.aspire.ums.cmdb.runner;

import lombok.extern.slf4j.Slf4j;
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
@Order(2)
@Slf4j
public class CmdbPartitionApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) {
        //todo 后面在进行分区处理
    }
}
