package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.common.util.PageUtil;
import com.migu.tsg.microservice.atomicservice.rbac.biz.ModuleCustomizedBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ModuleCustomizedDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleCustomized;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.transform.ModuleCustomizedTransformer;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedDTO;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:     com.aspire.mirror.configManagement.biz.impl  
 * 类名称:     ModuleCustomizedBizImpl
 * 类描述:     业务层实现类
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
@Service("moduleCustomizedBiz")
public class ModuleCustomizedBizImpl implements ModuleCustomizedBiz {
	/**
	 * 系统日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCustomizedBizImpl.class);
	
	/**
	 * dao
	 */
	@Autowired
	private ModuleCustomizedDao moduleCustomizedDao;
	
	/**
     * 新增数据
     *
     * @param moduleCustomizedDTO 动作DTO对象
     * @return String 数据ID
     */
    public String insert(final ModuleCustomizedDTO moduleCustomizedDTO) {
        if (null == moduleCustomizedDTO) {
            LOGGER.error("method[insert] param[moduleCustomizedDTO] is null");
            throw new RuntimeException("param[moduleCustomizedDTO] is null");
        }
        ModuleCustomized moduleCustomized = ModuleCustomizedTransformer.toPo(moduleCustomizedDTO);
        moduleCustomizedDao.insert(moduleCustomized);
        return moduleCustomized.getUserId();
    }
    
    /**
     * 根据主键删除数据
     *
     * @param moduleCustomizedId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String moduleCustomizedId) {
        if (StringUtils.isEmpty(moduleCustomizedId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[moduleCustomizedId] is null");
            throw new RuntimeException("param[moduleCustomizedId] is null");
        }
        return moduleCustomizedDao.deleteByPrimaryKey(moduleCustomizedId);
    }
	
	/**
     * 根据主键更新数据
     *
     * @param moduleCustomizedDTO 动作DTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final ModuleCustomizedDTO moduleCustomizedDTO) {
        if (null == moduleCustomizedDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[moduleCustomizedDTO] is null");
            throw new RuntimeException("param[moduleCustomizedDTO] is null");
        }
        if (StringUtils.isEmpty(moduleCustomizedDTO.getUserId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[moduleCustomizedId] is null");
            throw new RuntimeException("param[moduleCustomizedId] is null");
        }
        ModuleCustomized moduleCustomized = ModuleCustomizedTransformer.toPo(moduleCustomizedDTO);
        return moduleCustomizedDao.updateByPrimaryKey(moduleCustomized);
    }
    
     /**
     * 根据主键查询
     *
     * @param moduleCustomizedId 主键
     * @return ModuleCustomizedDTO 返回对象
     */
    public ModuleCustomizedDTO selectByPrimaryKey(final String moduleCustomizedId) {
        ModuleCustomized moduleCustomized = moduleCustomizedDao.selectByPrimaryKey(moduleCustomizedId);
        if (StringUtils.isEmpty(moduleCustomizedId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[moduleCustomizedId] is null");
            return null;
        }
        return ModuleCustomizedTransformer.fromPo(moduleCustomized);
    }
    
    /**
     * 根据dto实体查询列表
     *
     * @param moduleCustomizedDTO 动作DTO对象
     * @return List<ModuleCustomized>  返回集合
     */
    public List<ModuleCustomizedDTO> select(final ModuleCustomizedDTO moduleCustomizedDTO) {
        if (null == moduleCustomizedDTO) {
            LOGGER.warn("select Object moduleCustomizedDTO is null");
            return Collections.emptyList();
        }
        ModuleCustomized moduleCustomized = ModuleCustomizedTransformer.toPo(moduleCustomizedDTO);
        List<ModuleCustomized> listModuleCustomized = moduleCustomizedDao.select(moduleCustomized);
        return ModuleCustomizedTransformer.fromPo(listModuleCustomized);
    }
    
    /**
     * 根据主键数组查询
     *
     * @param moduleCustomizedIdArrays 主键数组
     * @return List<ModuleCustomizedDTO> 返回集合对象
     */
    public List<ModuleCustomizedDTO> selectByPrimaryKeyArrays(final String[] moduleCustomizedIdArrays) {
        if (ArrayUtils.isEmpty(moduleCustomizedIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[moduleCustomizedIdArrays] is null");
            return Collections.emptyList();
        }
        List<ModuleCustomized> listModuleCustomized = moduleCustomizedDao.selectByPrimaryKeyArrays(moduleCustomizedIdArrays);
        return ModuleCustomizedTransformer.fromPo(listModuleCustomized);
    }

    /**
    * 获取分页列表数据
    * @param pageRequest
    * @return
    */
    @Override
    public PageResult<ModuleCustomizedDTO> pageList(PageRequest pageRequest) {
          Page page = PageUtil.convert(pageRequest);
          int count = moduleCustomizedDao.pageListCount(page);
          PageResult<ModuleCustomizedDTO> pageResponse = new PageResult<ModuleCustomizedDTO>();
          pageResponse.setCount(count);
          int pageCount = (count -1) / page.getPageSize() + 1;
          pageResponse.setCurPage(page.getPageNo());
          pageResponse.setPageSize(page.getPageSize());
          pageResponse.setPageCount(pageCount);
          if (count > 0) {
               List<ModuleCustomized> listItem = moduleCustomizedDao.pageList(page);
               List<ModuleCustomizedDTO> listDTO = ModuleCustomizedTransformer.fromPo(listItem);
               pageResponse.setResult(listDTO);
               }
               return pageResponse;
          }
}
