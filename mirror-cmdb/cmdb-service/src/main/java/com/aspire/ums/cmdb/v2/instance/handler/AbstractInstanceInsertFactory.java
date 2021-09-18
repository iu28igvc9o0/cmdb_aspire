package com.aspire.ums.cmdb.v2.instance.handler;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: AbstarctInstanceInsertFactory
 * Author:   zhu.juwang
 * Date:     2020/7/6 10:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public abstract class AbstractInstanceInsertFactory {

    /**
     * 初始化类
     */
    public abstract void initSpring();

    /**
     *  网段新增逻辑
     * @param userName 新增用户
     * @param instanceData 实例数据
     * @param operateType 操作方式
     * @return 返回状态信息
     */
    public abstract Map<String, Object> handler(String userName, Map<String, Object> instanceData, String operateType);

    /**
     *  网段新增逻辑
     * @param userName 新增用户
     * @param instanceData 实例数据
     * @param operateType 操作方式
     * @return 返回状态信息
     */
    public Map<String, Object> execute(String userName, Map<String, Object> instanceData, String operateType) {
        this.initSpring();
        return this.handler(userName, instanceData, operateType);
    }
}
