package com.aspire.cmdb.agent.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ThreadUtil
 * Author:   zhu.juwang
 * Date:     2019/11/28 16:46
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ThreadUtil {
    /**
     * 创建10个线程大小的线程池
     */
    public static ExecutorService FIXED_POOL = Executors.newFixedThreadPool(10);
}
