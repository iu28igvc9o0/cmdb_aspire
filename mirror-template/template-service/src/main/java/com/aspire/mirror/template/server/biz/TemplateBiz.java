package com.aspire.mirror.template.server.biz;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.ItemSyncRequest;
import com.aspire.mirror.template.api.dto.model.TemplateDTO;
import com.aspire.mirror.template.api.dto.vo.ZabbixTemplateSyncVo;

/**
 * 模板业务层接口
 * <p>
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   TemplateBiz.java
 * 类描述:   模板业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface TemplateBiz {

    /**
     * 新增数据
     *
     * @param templateDTO 模板DTO对象
     * @return int 新增数据条数
     */
    String insert(TemplateDTO templateDTO);

    /**
     * 批量新增模板数据
     *
     * @param listTemplateDTO 模板DTO集合对象
     * @return int 新增数据条数
     */
//    int insertByBatch(List<TemplateDTO> listTemplateDTO);
    /**
     * 根据主键删除数据
     *
     * @param templateId 主键
     * @return int 删除数据条数
     */
//    int deleteByPrimaryKey(String templateId);

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
     * @param templateDTO  模板DTO对象
     * @return int 删除数据条数
     */
//    int delete(TemplateDTO templateDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param templateDTO 模板DTO对象
     * @return int 数据条数
     */
//    int updateByPrimaryKeySelective(TemplateDTO templateDTO);

    /**
     * 根据主键更新数据
     *
     * @param templateDTO 模板DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(TemplateDTO templateDTO);

    /**
     * 根据主键查询
     *
     * @param templateId 主键
     * @return TemplateDTO 返回对象
     */
    TemplateDTO selectByPrimaryKey(String templateId);

    /**
     * 根据主键数组查询
     *
     * @param templateIdArrays 主键数组
     * @return List<TemplateDTO> 返回集合对象
     */
    List<TemplateDTO> selectByPrimaryKeyArrays(String[] templateIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param templateDTO 模板DTO对象
     * @return List<Template>  返回集合
     */
//    List<TemplateDTO> select(TemplateDTO templateDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param templateDTO 模板DTO对象
     * @return int 数据条数
     */
//    int selectCount(TemplateDTO templateDTO);

    /**
     * 根据page对象查询对象列表
     *
     * @param page page查询对象
     * @return PageResponse<TemplateDTO> page返回对象
     */
    PageResponse<TemplateDTO> pageList(PageRequest page);

    /**
     * 复制模板信息
     *
     * @param templateId
     * @return
     */
    TemplateDTO copy(String templateId);

    TemplateDTO selectByTemplateName(String templateName);

    GeneralResponse zabbixTemplateSync(ZabbixTemplateSyncVo templateSyncVo);

    List<TemplateDTO> selectByPrimaryKeyArraysAndProxyIdentity(String[] templateIdArray, String proxyIdentity);
}
