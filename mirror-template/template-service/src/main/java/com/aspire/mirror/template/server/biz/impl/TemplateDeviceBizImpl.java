package com.aspire.mirror.template.server.biz.impl;

import java.util.Collections;
import java.util.List;

import com.aspire.mirror.template.server.dao.TemplateDeviceDao;
import com.aspire.mirror.template.api.dto.model.TemplateDeviceDTO;
import com.aspire.mirror.template.server.biz.TemplateDeviceBiz;
import com.aspire.mirror.template.server.dao.po.TemplateDevice;
import com.aspire.mirror.template.server.dao.po.transform.TemplateDeviceTransformer;

import org.apache.commons.lang.ArrayUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * monitor_template_device业务层实现类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    TemplateDeviceBizImpl.java
 * 类描述:    monitor_template_device业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
public class TemplateDeviceBizImpl implements TemplateDeviceBiz {

    /**
     * 新增数据
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 新增数据条数
     */
    public int insert(final TemplateDeviceDTO templateDeviceDTO){
        if(null == templateDeviceDTO){
            LOGGER.error("method[insert] param[templateDeviceDTO] is null");
            throw new RuntimeException("param[templateDeviceDTO] is null");
        }
        TemplateDevice templateDevice = TemplateDeviceTransformer.toPo(templateDeviceDTO);
        return templateDeviceDao.insert(templateDevice);
    }

    /**
     * 批量新增monitor_template_device数据
     *
     * @param listTemplateDeviceDTO monitor_template_deviceDTO集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<TemplateDeviceDTO> listTemplateDeviceDTO){
        if (CollectionUtils.isEmpty(listTemplateDeviceDTO)) {
            LOGGER.error("method[insertByBatch] param[listTemplateDeviceDTO] is null");
            throw new RuntimeException("param[listTemplateDeviceDTO] is null");
        }
        List<TemplateDevice> listTemplateDevice = TemplateDeviceTransformer.toPo(listTemplateDeviceDTO);
        return templateDeviceDao.insertByBatch(listTemplateDevice);
    }
    /**
     * 根据主键删除数据
     *
     * @param templateDeviceId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String templateDeviceId){
        if (StringUtils.isEmpty(templateDeviceId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[templateDeviceId] is null");
            throw new RuntimeException("param[templateDeviceId] is null");
        }
        return templateDeviceDao.deleteByPrimaryKey(templateDeviceId);
    }
    /**
    * 根据主键数组批量删除数据
    *
    * @param templateDeviceIdArrays 主键数组
    * @return int 删除数据条数
    */
    public int deleteByPrimaryKeyArrays(final String[] templateDeviceIdArrays){
        if (ArrayUtils.isEmpty(templateDeviceIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[templateDeviceIdArrays] is null");
            throw new RuntimeException("param[templateDeviceIdArrays] is null");
        }
        return templateDeviceDao.deleteByPrimaryKeyArrays(templateDeviceIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param templateDeviceDTO  monitor_template_deviceDTO对象
     * @return int 删除数据条数
     */
    public int delete(final TemplateDeviceDTO templateDeviceDTO){
        if(null == templateDeviceDTO){
            LOGGER.error("method[delete] param[templateDeviceDTO] is null");
            throw new RuntimeException("param[templateDeviceDTO] is null");
        }
        TemplateDevice templateDevice = TemplateDeviceTransformer.toPo(templateDeviceDTO);
        return templateDeviceDao.delete(templateDevice);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final TemplateDeviceDTO templateDeviceDTO){
        if(null == templateDeviceDTO){
            LOGGER.error("method[updateByPrimaryKey] param[templateDeviceDTO] is null");
            throw new RuntimeException("param[templateDeviceDTO] is null");
        }
        TemplateDevice templateDevice = TemplateDeviceTransformer.toPo(templateDeviceDTO);
        return templateDeviceDao.updateByPrimaryKeySelective(templateDevice);
    }

    /**
     * 根据主键更新数据
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final TemplateDeviceDTO templateDeviceDTO){
        if(null == templateDeviceDTO){
            LOGGER.error("method[updateByPrimaryKey] param[templateDeviceDTO] is null");
            throw new RuntimeException("param[templateDeviceDTO] is null");
        }
        if (StringUtils.isEmpty(templateDeviceDTO.getTemplateDeviceId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[templateDeviceId] is null");
            throw new RuntimeException("param[templateDeviceId] is null");
        }
        TemplateDevice templateDevice = TemplateDeviceTransformer.toPo(templateDeviceDTO);
        return templateDeviceDao.updateByPrimaryKey(templateDevice);
    }

    /**
     * 根据主键查询
     *
     * @param templateDeviceId 主键
     * @return TemplateDeviceDTO 返回对象
     */
    public TemplateDeviceDTO selectByPrimaryKey(final String templateDeviceId){
        TemplateDevice templateDevice = templateDeviceDao.selectByPrimaryKey(templateDeviceId);
        if (StringUtils.isEmpty(templateDeviceId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[templateDeviceId] is null");
            return null;
        }
        return TemplateDeviceTransformer.fromPo(templateDevice);
    }
    /**
     * 根据主键数组查询
     *
     * @param templateDeviceIdArrays 主键数组
     * @return List<TemplateDeviceDTO> 返回集合对象
     */
    public List<TemplateDeviceDTO> selectByPrimaryKeyArrays(final String[] templateDeviceIdArrays){
        if (ArrayUtils.isEmpty(templateDeviceIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[templateDeviceIdArrays] is null");
            return Collections.emptyList();
        }
        List<TemplateDevice> listTemplateDevice = templateDeviceDao.selectByPrimaryKeyArrays(templateDeviceIdArrays);
        return TemplateDeviceTransformer.fromPo(listTemplateDevice);
    }
    /**
     * 根据dto实体查询列表
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return List<TemplateDevice>  返回集合
     */
    public List<TemplateDeviceDTO> select(final TemplateDeviceDTO templateDeviceDTO){
        if(null == templateDeviceDTO){
            LOGGER.warn("select Object templateDeviceDTO is null");
            return Collections.emptyList();
        }
        TemplateDevice templateDevice = TemplateDeviceTransformer.toPo(templateDeviceDTO);
        List<TemplateDevice> listTemplateDevice = templateDeviceDao.select(templateDevice);
        return TemplateDeviceTransformer.fromPo(listTemplateDevice);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param templateDeviceDTO monitor_template_deviceDTO对象
     * @return int 数据条数
     */
    public int selectCount(final TemplateDeviceDTO templateDeviceDTO){
        if(null == templateDeviceDTO){
            LOGGER.warn("selectCount Object templateDeviceDTO is null");
        }
        TemplateDevice templateDevice = TemplateDeviceTransformer.toPo(templateDeviceDTO);
        return templateDeviceDao.selectCount(templateDevice);
    }

    /** slf4j*/
    private static final Logger   LOGGER = LoggerFactory.getLogger(TemplateDeviceBizImpl.class);

    /** 数据访问层接口*/
    @Autowired
    private TemplateDeviceDao templateDeviceDao;

} 
