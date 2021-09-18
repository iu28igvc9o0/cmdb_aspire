package com.migu.tsg.microservice.atomicservice.rbac.controller;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.biz.ModuleCustomizedBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedCreateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedUpdateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.ModuleCustomizedVO;
import com.migu.tsg.microservice.atomicservice.rbac.service.ModuleCustomizedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:     com.aspire.mirror.configManagement.controller   
 * 类名称:     ModuleCustomizedController
 * 类描述:     控制层实现类
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
@RestController
@CacheConfig(cacheNames = "ModuleCustomizedCache")
public class ModuleCustomizedController implements ModuleCustomizedService {
	/**
	 * 系统日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCustomizedController.class);
	
	/**
	 * service
	 */
	 @Autowired
    private ModuleCustomizedBiz moduleCustomizedBiz;

	/**
     * 创建动作信息
     *
  	 * @param ModuleCustomizedCreateRequest 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
     
    public ModuleCustomizedCreateResponse createdModuleCustomized(@RequestBody final ModuleCustomizedCreateRequest moduleCustomizedCreateRequest) {
        if (null == moduleCustomizedCreateRequest) {
            LOGGER.error("created param moduleCustomizedCreateRequest is null");
            throw new RuntimeException("moduleCustomizedCreateRequest is null");
        }
        ModuleCustomizedDTO moduleCustomizedDTO = new ModuleCustomizedDTO();
        BeanUtils.copyProperties(moduleCustomizedCreateRequest, moduleCustomizedDTO);
        moduleCustomizedBiz.insert(moduleCustomizedDTO);
        ModuleCustomizedCreateResponse moduleCustomizedCreateResponse = new ModuleCustomizedCreateResponse();
        BeanUtils.copyProperties(moduleCustomizedDTO, moduleCustomizedCreateResponse);
//        moduleCustomizedCreateResponse.setUserId(moduleCustomizedId);
        return moduleCustomizedCreateResponse;
    }
    
   /**
     * 根据主键删除单条动作信息
     *
     * @param moduleCustomizedId 主键
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("user_id") final String moduleCustomizedId) {
        try {
            moduleCustomizedBiz.deleteByPrimaryKey(moduleCustomizedId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	/**
     * 根据主键修改信息
     *
     * @param ModuleCustomizedCreateRequest 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    public ModuleCustomizedUpdateResponse modifyByPrimaryKey(@RequestBody final ModuleCustomizedUpdateRequest moduleCustomizedUpdateRequest) {
        ModuleCustomizedDTO moduleCustomizeddTO = new ModuleCustomizedDTO();
        BeanUtils.copyProperties(moduleCustomizedUpdateRequest, moduleCustomizeddTO);
        moduleCustomizedBiz.updateByPrimaryKey(moduleCustomizeddTO);
        ModuleCustomizedDTO queryParam=new ModuleCustomizedDTO();
        queryParam.setUserId(moduleCustomizedUpdateRequest.getUserId());
        queryParam.setModuleId(moduleCustomizedUpdateRequest.getModuleId());
        List<ModuleCustomizedDTO> findModuleCustomizedDTO = moduleCustomizedBiz.select(queryParam);
        ModuleCustomizedUpdateResponse moduleCustomizedUpdateResponse = new ModuleCustomizedUpdateResponse();
        BeanUtils.copyProperties(findModuleCustomizedDTO.get(0), moduleCustomizedUpdateResponse);

        return moduleCustomizedUpdateResponse;
    }
	
	/**
     * 根据主键查找动作详情信息
     *
     * @param moduleCustomizedId 主键
     * @return ModuleCustomizedVO 详情响应对象
     */
    public ModuleCustomizedVO findByPrimaryKey(@PathVariable("user_id") final String moduleCustomizedId) {
        if (StringUtils.isEmpty(moduleCustomizedId)) {
            LOGGER.warn("findByPrimaryKey param moduleCustomizedId is null");
            return null;
        }
        ModuleCustomizedDTO moduleCustomizedDTO = moduleCustomizedBiz.selectByPrimaryKey(moduleCustomizedId);
        ModuleCustomizedVO moduleCustomizedVO = new ModuleCustomizedVO();
        if (null != moduleCustomizedDTO) {
            BeanUtils.copyProperties(moduleCustomizedDTO, moduleCustomizedVO);
        }

        return moduleCustomizedVO;
    }

    /**
     * 根据主键查询动作集合信息
     *
     * @param moduleCustomizedIds 动作主键
     * @return ModuleCustomizedVO 动作查询响应对象
     */
    public List<ModuleCustomizedVO> listByPrimaryKeyArrays(@PathVariable("user_ids") final String moduleCustomizedIds) {
        if (StringUtils.isEmpty(moduleCustomizedIds)) {
            LOGGER.error("listByPrimaryKeyArrays param amoduleCustomizedIds is null");
            return Lists.newArrayList();
        }
        String[] moduleCustomizedIdArrays = moduleCustomizedIds.split(",");
        List<ModuleCustomizedDTO> listModuleCustomizedDTO = moduleCustomizedBiz.selectByPrimaryKeyArrays(moduleCustomizedIdArrays);
        List<ModuleCustomizedVO> listModuleCustomizedVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listModuleCustomizedDTO)) {
            for (ModuleCustomizedDTO moduleCustomizedDTO : listModuleCustomizedDTO) {
                ModuleCustomizedVO moduleCustomizedVO = new ModuleCustomizedVO();
                BeanUtils.copyProperties(moduleCustomizedDTO, moduleCustomizedVO);
                listModuleCustomizedVO.add(moduleCustomizedVO);
            }
        }
        return listModuleCustomizedVO;
    }

    /**
    * 根据查询条件获取分页列表获取分页列表
    * @param request 分页查询监控对象
    * @return
    */
    @Override
    public PageResult<ModuleCustomizedDTO> pageList(ModuleCustomizedPageRequest request) {
        if (null == request) {
            LOGGER.warn("pageList param templatePageRequest is null");
            return null;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setPageNo(request.getPageNo());
        //查询条件，可自己增加PageRequest中参数
        /** pageRequest.addFields("precinctId", request.getPrecinctId());
        pageRequest.addFields("precinctName", request.getPrecinctName());
        pageRequest.addFields("precinctKind", request.getPrecinctKind());
        pageRequest.addFields("lscId", request.getLscId());
        pageRequest.addFields("areaCode", request.getAreaCode());*/
        PageResult<ModuleCustomizedDTO> ModuleCustomizedDTOPageResponse = moduleCustomizedBiz.pageList(pageRequest);
        return ModuleCustomizedDTOPageResponse;
     }

	@Override
	public List<ModuleCustomizedDTO> select( @RequestBody ModuleCustomizedCreateRequest moduleCustomized) {
		ModuleCustomizedDTO dto=new ModuleCustomizedDTO();
		BeanUtils.copyProperties(moduleCustomized, dto);
		return moduleCustomizedBiz.select(dto);
	}
}
