package com.migu.tsg.microservice.atomicservice.rbac.biz;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedDTO;

import java.util.List;

/**
 * 业务逻辑层接口
 * <p>
 * 项目名称: 微服务运维平台
 * 包:     com.aspire.mirror.configManagement.biz   
 * 类名称:     ModuleCustomizedBiz
 * 类描述:     业务逻辑层接口
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
public interface ModuleCustomizedBiz {
	/**
     * 新增数据
     *
     * @param moduleCustomizedDTO 动作DTO对象
     * @return String 数据ID
     */
    String insert(ModuleCustomizedDTO moduleCustomizedDTO);
	
	 /**
     * 根据主键删除数据
     *
     * @param moduleCustomizedId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String moduleCustomizedId);
    
    /**
     * 根据主键更新数据
     *
     * @param moduleCustomizedDTO 动作DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ModuleCustomizedDTO moduleCustomizedDTO);
    
    /**
     * 根据主键查询
     *
     * @param moduleCustomizedId 主键
     * @return ModuleCustomizedDTO 返回对象
     */
    ModuleCustomizedDTO selectByPrimaryKey(String moduleCustomizedId);

    /**
     * 根据dto实体查询列表
     *
     * @param moduleCustomizedDTO 动作DTO对象
     * @return List<ModuleCustomized>  返回集合
     */
    List<ModuleCustomizedDTO> select(ModuleCustomizedDTO moduleCustomizedDTO);
    
    /**
     * 根据主键数组查询
     *
     * @param moduleCustomizedIdArrays 主键数组
     * @return List<ModuleCustomizedDTO> 返回集合对象
     */
    List<ModuleCustomizedDTO> selectByPrimaryKeyArrays(String[] moduleCustomizedIdArrays);


    /**
    * 分页列表查询
    * @param pageRequest
    * @return
    */
    PageResult<ModuleCustomizedDTO> pageList(PageRequest pageRequest);
}
