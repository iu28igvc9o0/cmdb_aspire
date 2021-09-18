package com.aspire.ums.cmdb.v3.module.event;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: AbstractModuleEvent
 * Author:   zhu.juwang
 * Date:     2020/5/12 15:29
 * Description: 模型的回调事件类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public abstract class AbstractModuleEvent {

    /**
     * 初始化Spring Autowired的类
     */
    public abstract void initSpringBeans();

    /**
     * 执行事件方法
     * @param moduleId 模型ID
     * @return 根据实际返回数据信息
     */
    public abstract Map<String, Object> event(String moduleId, String instanceId, Map<String ,Object> handleData);

    /**
     * 执行事件方法
     * @param moduleId 模型ID
     * @param instanceId 实例ID
     * @return 根据实际返回数据信息
     */
    public Map<String, Object> run(String moduleId, String instanceId, Map<String ,Object> handleData) {
        this.initSpringBeans();
        return this.event(moduleId, instanceId, handleData);
    }
}
