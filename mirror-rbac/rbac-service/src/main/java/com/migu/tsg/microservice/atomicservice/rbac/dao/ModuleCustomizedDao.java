package com.migu.tsg.microservice.atomicservice.rbac.dao;

import com.aspire.mirror.common.entity.Page;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleCustomized;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据访问层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.configManagement.dao   
 * 类名称:     ModuleCustomizedDao 
 * 类描述:     数据访问层接口
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
 @Mapper
public interface ModuleCustomizedDao {

	/**
     * 新增数据
     *
     * @param moduleCustomized 动作PO对象
     * @return int 新增数据条数
     */
    int insert(ModuleCustomized moduleCustomized);
    
    /**
     * 根据主键删除数据
     *
     * @param moduleCustomizedId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "userId") String moduleCustomizedId);
    
    /**
     * 根据主键更新数据
     *
     * @param moduleCustomized 动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ModuleCustomized moduleCustomized);
    
    /**
     * 根据主键查询
     *
     * @param moduleCustomizedId 主键
     * @return ModuleCustomized 返回对象
     */
    ModuleCustomized selectByPrimaryKey(@Param(value = "userId") String moduleCustomizedId);

    /**
     * 根据主键数组查询
     *
     * @param moduleCustomizedIdArrays 主键数组
     * @return List<ModuleCustomized> 返回集合对象
     */
    List<ModuleCustomized> selectByPrimaryKeyArrays(String[] moduleCustomizedIdArrays);
    
	/**
     * 根据po实体查询列表
     *
     * @param moduleCustomized 动作PO对象
     * @return List<ModuleCustomized>  返回集合
     */
    List<ModuleCustomized> select(ModuleCustomized moduleCustomized);

    /**
    * 根据page对象查询数量
    * @param page
    * @return 条数
    */
    int pageListCount(Page page);

    /**
    * 根据page对象查询监控项列表
    * @param page
    * @return 监控项列表
    */
    List<ModuleCustomized> pageList(Page page);
}
