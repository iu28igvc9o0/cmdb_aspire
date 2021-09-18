package com.aspire.mirror.template.server.dao;

import java.util.List;

import com.aspire.mirror.template.server.dao.po.TemplateObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * monitor_template_device数据访问层接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    TemplateObjectDao.java
 * 类描述:    monitor_template_device数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface TemplateObjectDao {

    /**
     * 新增数据
     *
     * @param templateObject monitor_template_devicePO对象
     * @return int 新增数据条数
     */
    int insert(TemplateObject templateObject);

    /**
     * 批量新增monitor_template_device数据
     *
     * @param listTemplateObject monitor_template_devicePO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<TemplateObject> listTemplateObject);
    /**
     * 根据主键删除数据
     *
     * @param templateObjectId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "templateObjectId") String templateObjectId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param templateObjectIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] templateObjectIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param templateObject  monitor_template_devicePO对象
     * @return int 删除数据条数
     */
    int delete(TemplateObject templateObject);

    /**
     * 根据参数选择性更新数据
     *
     * @param templateObject monitor_template_devicePO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(TemplateObject templateObject);

    /**
     * 根据主键更新数据
     *
     * @param templateObject monitor_template_devicePO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(TemplateObject templateObject);

    /**
     * 根据主键查询
     *
     * @param templateObjectId 主键
     * @return TemplateObject 返回对象
     */
    TemplateObject selectByPrimaryKey(@Param(value = "templateObjectId") String templateObjectId);

    /**
     * 根据主键数组查询
     *
     * @param templateObjectIdArrays 主键数组
     * @return List<TemplateObject> 返回集合对象
     */
    List<TemplateObject> selectByPrimaryKeyArrays(String[] templateObjectIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param templateObject monitor_template_devicePO对象
     * @return List<TemplateObject>  返回集合
     */
    List<TemplateObject> select(TemplateObject templateObject);

    /**
     * 根据po实体查询条数
     *
     * @param templateObject monitor_template_devicePO对象
     * @return int 数据条数
     */
    int selectCount(TemplateObject templateObject);

    /**
     * 根据模板ID删除主机列表
     * @param templateIds
     * @return
     */
    int deleteByTemplateIds(String[] templateIds);

    List<TemplateObject> selectByTemplateId(@Param("templateId") String templateId);
}
