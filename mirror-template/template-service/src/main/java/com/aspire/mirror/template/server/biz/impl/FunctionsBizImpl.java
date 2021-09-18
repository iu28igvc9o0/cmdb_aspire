package com.aspire.mirror.template.server.biz.impl;

import java.util.Collections;
import java.util.List;

import com.aspire.mirror.template.server.dao.FunctionsDao;
import com.aspire.mirror.template.api.dto.model.FunctionsDTO;
import com.aspire.mirror.template.server.biz.FunctionsBiz;
import com.aspire.mirror.template.server.dao.po.Functions;
import com.aspire.mirror.template.server.dao.po.transform.FunctionsTransformer;

import org.apache.commons.lang.ArrayUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * monitor_functions业务层实现类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    FunctionsBizImpl.java
 * 类描述:    monitor_functions业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
public class FunctionsBizImpl implements FunctionsBiz {

    /**
     * 新增数据
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 新增数据条数
     */
    public int insert(final FunctionsDTO functionsDTO){
        if(null == functionsDTO){
            LOGGER.error("method[insert] param[functionsDTO] is null");
            throw new RuntimeException("param[functionsDTO] is null");
        }
        Functions functions = FunctionsTransformer.toPo(functionsDTO);
        return functionsDao.insert(functions);
    }

    /**
     * 批量新增monitor_functions数据
     *
     * @param listFunctionsDTO monitor_functionsDTO集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<FunctionsDTO> listFunctionsDTO){
        if (CollectionUtils.isEmpty(listFunctionsDTO)) {
            LOGGER.error("method[insertByBatch] param[listFunctionsDTO] is null");
            throw new RuntimeException("param[listFunctionsDTO] is null");
        }
        List<Functions> listFunctions = FunctionsTransformer.toPo(listFunctionsDTO);
        return functionsDao.insertByBatch(listFunctions);
    }
    /**
     * 根据主键删除数据
     *
     * @param functionId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String functionId){
        if (StringUtils.isEmpty(functionId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[functionId] is null");
            throw new RuntimeException("param[functionId] is null");
        }
        return functionsDao.deleteByPrimaryKey(functionId);
    }
    /**
    * 根据主键数组批量删除数据
    *
    * @param functionIdArrays 主键数组
    * @return int 删除数据条数
    */
    public int deleteByPrimaryKeyArrays(final String[] functionIdArrays){
        if (ArrayUtils.isEmpty(functionIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[functionIdArrays] is null");
            throw new RuntimeException("param[functionIdArrays] is null");
        }
        return functionsDao.deleteByPrimaryKeyArrays(functionIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param functionsDTO  monitor_functionsDTO对象
     * @return int 删除数据条数
     */
    public int delete(final FunctionsDTO functionsDTO){
        if(null == functionsDTO){
            LOGGER.error("method[delete] param[functionsDTO] is null");
            throw new RuntimeException("param[functionsDTO] is null");
        }
        Functions functions = FunctionsTransformer.toPo(functionsDTO);
        return functionsDao.delete(functions);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final FunctionsDTO functionsDTO){
        if(null == functionsDTO){
            LOGGER.error("method[updateByPrimaryKey] param[functionsDTO] is null");
            throw new RuntimeException("param[functionsDTO] is null");
        }
        Functions functions = FunctionsTransformer.toPo(functionsDTO);
        return functionsDao.updateByPrimaryKeySelective(functions);
    }

    /**
     * 根据主键更新数据
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final FunctionsDTO functionsDTO){
        if(null == functionsDTO){
            LOGGER.error("method[updateByPrimaryKey] param[functionsDTO] is null");
            throw new RuntimeException("param[functionsDTO] is null");
        }
        if (StringUtils.isEmpty(functionsDTO.getFunctionId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[functionId] is null");
            throw new RuntimeException("param[functionId] is null");
        }
        Functions functions = FunctionsTransformer.toPo(functionsDTO);
        return functionsDao.updateByPrimaryKey(functions);
    }

    /**
     * 根据主键查询
     *
     * @param functionId 主键
     * @return FunctionsDTO 返回对象
     */
    public FunctionsDTO selectByPrimaryKey(final String functionId){
        Functions functions = functionsDao.selectByPrimaryKey(functionId);
        if (StringUtils.isEmpty(functionId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[functionId] is null");
            return null;
        }
        return FunctionsTransformer.fromPo(functions);
    }
    /**
     * 根据主键数组查询
     *
     * @param functionIdArrays 主键数组
     * @return List<FunctionsDTO> 返回集合对象
     */
    public List<FunctionsDTO> selectByPrimaryKeyArrays(final String[] functionIdArrays){
        if (ArrayUtils.isEmpty(functionIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[functionIdArrays] is null");
            return Collections.emptyList();
        }
        List<Functions> listFunctions = functionsDao.selectByPrimaryKeyArrays(functionIdArrays);
        return FunctionsTransformer.fromPo(listFunctions);
    }
    /**
     * 根据dto实体查询列表
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return List<Functions>  返回集合
     */
    public List<FunctionsDTO> select(final FunctionsDTO functionsDTO){
        if(null == functionsDTO){
            LOGGER.warn("select Object functionsDTO is null");
            return Collections.emptyList();
        }
        Functions functions = FunctionsTransformer.toPo(functionsDTO);
        List<Functions> listFunctions = functionsDao.select(functions);
        return FunctionsTransformer.fromPo(listFunctions);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param functionsDTO monitor_functionsDTO对象
     * @return int 数据条数
     */
    public int selectCount(final FunctionsDTO functionsDTO){
        if(null == functionsDTO){
            LOGGER.warn("selectCount Object functionsDTO is null");
        }
        Functions functions = FunctionsTransformer.toPo(functionsDTO);
        return functionsDao.selectCount(functions);
    }

    /** slf4j*/
    private static final Logger   LOGGER = LoggerFactory.getLogger(FunctionsBizImpl.class);

    /** 数据访问层接口*/
    @Autowired
    private FunctionsDao functionsDao;

} 
