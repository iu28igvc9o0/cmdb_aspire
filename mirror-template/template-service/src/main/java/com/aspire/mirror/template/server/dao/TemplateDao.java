package com.aspire.mirror.template.server.dao;

import java.util.List;

import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.server.dao.po.Template;

/**
 * 模板数据访问层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    TemplateDao.java
 * 类描述:    模板数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface TemplateDao {

    /**
     * 新增数据
     *
     * @param template 模板PO对象
     * @return int 新增数据条数
     */
    int insert(Template template);

    /**
     * 批量新增模板数据
     *
     * @param listTemplate 模板PO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Template> listTemplate);

    /**
     * 根据主键删除数据
     *
     * @param templateId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "templateId") String templateId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param templateIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] templateIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param template 模板PO对象
     * @return int 删除数据条数
     */
    int delete(Template template);

    /**
     * 根据参数选择性更新数据
     *
     * @param template 模板PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Template template);

    /**
     * 根据主键更新数据
     *
     * @param template 模板PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Template template);

    /**
     * 根据主键查询
     *
     * @param templateId 主键
     * @return Template 返回对象
     */
    Template selectByPrimaryKey(@Param(value = "templateId") String templateId);

    /**
     * 根据主键数组查询
     *
     * @param templateIdArrays 主键数组
     * @return List<Template> 返回集合对象
     */
    List<Template> selectByPrimaryKeyArrays(String[] templateIdArrays);

    /**
     * 根据po实体查询列表
     *
     * @param template 模板PO对象
     * @return List<Template>  返回集合
     */
    List<Template> select(Template template);

    /**
     * 根据po实体查询条数
     *
     * @param template 模板PO对象
     * @return int 数据条数
     */
    int selectCount(Template template);

    /**
     * 根据page查count
     *
     * @param page
     * @return
     */
    int pageListCount(Page page);

    /**
     * 根据page对象查模板列表
     *
     * @param page
     * @return
     */
    List<Template> pageList(Page page);

    Template selectByName(@Param("name") String name);

    Template selectByZabbixTemplateIdAndProxyIdentity(@Param("zabbixTemplateId") String zabbixTemplateId, @Param("proxyIdentity") String proxyIdentity);

    Template selectByNameAndZabbixTemplateId(@Param("name") String name, @Param("zabbixTemplateId") String zabbixTemplateId);

    List<Template> selectByPrimaryKeyArraysAndProxyIdentity(@Param("templateIdArray") String[] templateIdArray, @Param("proxyIdentity") String proxyIdentity);

    List<Template> selectZabbixSyncTemplate();
}
