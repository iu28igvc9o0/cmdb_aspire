package com.migu.tsg.microservice.atomicservice.rbac.biz;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleInfoDTO;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.biz
 * 类名称:    ModuleInfoBiz.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:10
 * 版本:      v1.0
 */
public interface ModuleInfoBiz {
    /**
     * 创建模块信息
     * @param moduleInfoDTO 模块信息
     * @return 影响数据条数
     */
    ModuleInfoDTO insert(ModuleInfoDTO moduleInfoDTO);

    /**
     * 根据编码查找模块信息
     * @param moduleCode
     * @return
     */
    ModuleInfoDTO findByCode(String moduleCode);
}
