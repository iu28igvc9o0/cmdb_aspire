package com.migu.tsg.microservice.atomicservice.rbac.controller;


import com.migu.tsg.microservice.atomicservice.rbac.biz.ModuleCustomizedViewBiz;
import com.migu.tsg.microservice.atomicservice.rbac.biz.UserClassifyBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.UserClassifyPageConfigDao;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewDesignRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedViewDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.ModuleCustomizedViewService;

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

import javax.annotation.Resource;
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
@CacheConfig(cacheNames = "ModuleCustomizedViewCache")
public class ModuleCustomizedViewController implements ModuleCustomizedViewService {
	/**
	 * 系统日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCustomizedViewController.class);
	
	 
	 
	 @Autowired
	 private ModuleCustomizedViewBiz moduleCustomizedViewBiz;

	@Resource
	private UserClassifyPageConfigDao userClassifyPageConfigDao;

    /**
    * 根据查询条件获取分页列表获取分页列表
    * @param request 分页查询监控对象
    * @return
    */
//    @Override
//    public PageResult<ModuleCustomizedDTO> pageList(ModuleCustomizedPageRequest request) {
//        if (null == request) {
//            LOGGER.warn("pageList param templatePageRequest is null");
//            return null;
//        }
//        PageRequest pageRequest = new PageRequest();
//        pageRequest.setPageSize(request.getPageSize());
//        pageRequest.setPageNo(request.getPageNo());
//        //查询条件，可自己增加PageRequest中参数
//        /** pageRequest.addFields("precinctId", request.getPrecinctId());
//        pageRequest.addFields("precinctName", request.getPrecinctName());
//        pageRequest.addFields("precinctKind", request.getPrecinctKind());
//        pageRequest.addFields("lscId", request.getLscId());
//        pageRequest.addFields("areaCode", request.getAreaCode());*/
//        PageResult<ModuleCustomizedDTO> ModuleCustomizedDTOPageResponse = moduleCustomizedBiz.pageList(pageRequest);
//        return ModuleCustomizedDTOPageResponse;
//     }


	@Override
	public ResponseEntity<String> createdModuleCustomizedView(@RequestBody ModuleCustomizedViewRequest moduleCustomizedViewRequest) {
        if (null == moduleCustomizedViewRequest) {
            LOGGER.error("created param moduleCustomizedViewRequest is null");
            throw new RuntimeException("moduleCustomizedViewRequest is null");
        }
        ModuleCustomizedViewDTO moduleCustomizedDTO = new ModuleCustomizedViewDTO();
        BeanUtils.copyProperties(moduleCustomizedViewRequest, moduleCustomizedDTO);
        try {
        	moduleCustomizedViewBiz.insert(moduleCustomizedDTO);
        	  return new ResponseEntity<String>("success",HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("insert error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	public ResponseEntity<String> modifyByPrimaryKey(@RequestBody ModuleCustomizedViewUpdateRequest moduleCustomizedViewRequest) {
		 if (null == moduleCustomizedViewRequest) {
	            LOGGER.error("created param ModuleCustomizedViewUpdateRequest is null");
	            throw new RuntimeException("ModuleCustomizedViewUpdateRequest is null");
	        }
		  ModuleCustomizedViewDTO moduleCustomizedDTO = new ModuleCustomizedViewDTO();
	        BeanUtils.copyProperties(moduleCustomizedViewRequest, moduleCustomizedDTO);
	        try {
	        	moduleCustomizedViewBiz.update(moduleCustomizedDTO);
	        	//同时更新t_user_classify_page_config
				userClassifyPageConfigDao.updatePageConfig(moduleCustomizedDTO.getId(),moduleCustomizedDTO.getContent());
	        	return new ResponseEntity<String>("success",HttpStatus.OK);
	        } catch (Exception e) {
	            LOGGER.error("update error:" + e.getMessage(), e);
	            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	}
	
	 /**
        * 根据主键删除单条动作信息
     *
     * @param moduleCustomizedId 主键
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("id") final String id) {
        try {
        	moduleCustomizedViewBiz.deleteByPrimaryKey(id);
        	return new ResponseEntity<String>("success",HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	

	@Override
	public List<ModuleCustomizedViewDTO> select(@RequestBody ModuleCustomizedViewRequest moduleCustomizedViewRequest) {
		ModuleCustomizedViewDTO dto=new ModuleCustomizedViewDTO();
		BeanUtils.copyProperties(moduleCustomizedViewRequest, dto);
		return moduleCustomizedViewBiz.select(dto);
	}

	@Override
	public ResponseEntity<String> designView(@RequestBody ModuleCustomizedViewDesignRequest m) {
		// TODO Auto-generated method stub
		return null;
	}
}
