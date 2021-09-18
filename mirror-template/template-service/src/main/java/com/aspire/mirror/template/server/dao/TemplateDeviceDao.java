package com.aspire.mirror.template.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.server.dao.po.TemplateDevice;

/**
 * monitor_template_device数据访问层接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao
 * 类名称:    TemplateDeviceDao.java
 * 类描述:    monitor_template_device数据访问层接口
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Mapper
public interface TemplateDeviceDao {

    /**
     * 新增数据
     *
     * @param templateDevice monitor_template_devicePO对象
     * @return int 新增数据条数
     */
    int insert(TemplateDevice templateDevice);

    /**
     * 批量新增monitor_template_device数据
     *
     * @param listTemplateDevice monitor_template_devicePO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<TemplateDevice> listTemplateDevice);
    /**
     * 根据主键删除数据
     *
     * @param templateDeviceId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "templateDeviceId") String templateDeviceId);

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
     * @param templateDevice  monitor_template_devicePO对象
     * @return int 删除数据条数
     */
    int delete(TemplateDevice templateDevice);

    /**
     * 根据参数选择性更新数据
     *
     * @param templateDevice monitor_template_devicePO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(TemplateDevice templateDevice);

    /**
     * 根据主键更新数据
     *
     * @param templateDevice monitor_template_devicePO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(TemplateDevice templateDevice);

    /**
     * 根据主键查询
     *
     * @param templateDeviceId 主键
     * @return TemplateDevice 返回对象
     */
    TemplateDevice selectByPrimaryKey(@Param(value = "templateDeviceId") String templateDeviceId);

    /**
     * 根据主键数组查询
     *
     * @param templateDeviceIdArrays 主键数组
     * @return List<TemplateDevice> 返回集合对象
     */
    List<TemplateDevice> selectByPrimaryKeyArrays(String[] templateDeviceIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param templateDevice monitor_template_devicePO对象
     * @return List<TemplateDevice>  返回集合
     */
    List<TemplateDevice> select(TemplateDevice templateDevice);

    /**
     * 根据po实体查询条数
     *
     * @param templateDevice monitor_template_devicePO对象
     * @return int 数据条数
     */
    int selectCount(TemplateDevice templateDevice);

    /**
     * 根据模板ID删除主机列表
     * @param templateId
     * @return
     */
    int deleteByTemplateId(String templateId);
}
