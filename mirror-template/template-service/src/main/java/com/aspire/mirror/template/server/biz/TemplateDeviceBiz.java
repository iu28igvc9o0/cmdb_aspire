package com.aspire.mirror.template.server.biz;

import java.util.List;

import com.aspire.mirror.template.server.dao.po.TemplateDevice;
import com.aspire.mirror.template.api.dto.model.TemplateDeviceDTO;

/**
 * monitor_template_device业务层接口
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   TemplateDeviceBiz.java
 * 类描述:   monitor_template_device业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface TemplateDeviceBiz {

    /**
     * 新增数据
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 新增数据条数
     */
    int insert(TemplateDeviceDTO templateDeviceDTO);

    /**
     * 批量新增monitor_template_device数据
     *
     * @param listTemplateDeviceDTO monitor_template_deviceDTO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<TemplateDeviceDTO> listTemplateDeviceDTO);
    /**
     * 根据主键删除数据
     *
     * @param templateDeviceId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String templateDeviceId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param templateDeviceIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] templateDeviceIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param templateDeviceDTO  monitor_template_deviceDTO对象
     * @return int 删除数据条数
     */
    int delete(TemplateDeviceDTO templateDeviceDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(TemplateDeviceDTO templateDeviceDTO);

    /**
     * 根据主键更新数据
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(TemplateDeviceDTO templateDeviceDTO);

    /**
     * 根据主键查询
     *
     * @param templateDeviceId 主键
     * @return TemplateDeviceDTO 返回对象
     */
    TemplateDeviceDTO selectByPrimaryKey(String templateDeviceId);

    /**
     * 根据主键数组查询
     *
     * @param templateDeviceIdArrays 主键数组
     * @return List<TemplateDeviceDTO> 返回集合对象
     */
    List<TemplateDeviceDTO> selectByPrimaryKeyArrays(String[] templateDeviceIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return List<TemplateDevice>  返回集合
     */
    List<TemplateDeviceDTO> select(TemplateDeviceDTO templateDeviceDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 数据条数
     */
    int selectCount(TemplateDeviceDTO templateDeviceDTO);

} 
