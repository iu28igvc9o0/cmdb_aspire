package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.biz.DepartmentBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentCreateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentUpdateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentUserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.DepartmentVO;
import com.migu.tsg.microservice.atomicservice.rbac.service.DepartmentService;

/**
 * 控制层实现类
 * <p>
 * 项目名称: mirror平台 包: com.migu.tsg.microservice.atomicservice.rbac.controller 类名称: DepartmentController 类描述: 控制层实现类 创建人: 曾祥华 创建时间:
 * 2019-03-04 16:04:48
 */
@RestController
// @CacheConfig(cacheNames = "DepartmentCache")
public class DepartmentController implements DepartmentService {

    /**
     * 系统日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    /**
     * service
     */
    @Autowired
    private DepartmentBiz departmentBiz;

    /**
     * 创建动作信息
     *
     * @param DepartmentCreateRequest
     *            动作创建请求对象
     * @return DepartmentCreateResponse 动作创建响应对象
     */

    @Override
    public DepartmentCreateResponse createdDepartment(@RequestBody final DepartmentCreateRequest departmentCreateRequest) {
        if (null == departmentCreateRequest) {
            LOGGER.error("created param departmentCreateRequest is null");
            throw new RuntimeException("departmentCreateRequest is null");
        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(departmentCreateRequest, departmentDTO);
        if(!StringUtils.isEmpty(departmentCreateRequest.getDepartmentId())){
        	departmentDTO.setUuid(departmentCreateRequest.getDepartmentId());
        }
        String departmentId = departmentBiz.insert(departmentDTO);
        DepartmentCreateResponse departmentCreateResponse = new DepartmentCreateResponse();
        BeanUtils.copyProperties(departmentDTO, departmentCreateResponse);
        departmentCreateResponse.setUuid(departmentId);
        return departmentCreateResponse;
    }

    /**
     * 根据主键删除单条动作信息
     *
     * @param departmentId
     *            主键
     * @@return Result 返回结果
     */
    @Override
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("department_id") final String departmentId) {
        try {
            departmentBiz.deleteByPrimaryKey(departmentId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键修改信息
     *
     * @param DepartmentCreateRequest
     *            动作创建请求对象
     * @return DepartmentCreateResponse 动作创建响应对象
     */
    @Override
    public DepartmentUpdateResponse modifyByPrimaryKey(@PathVariable("department_id") final String departmentId,
            @RequestBody final DepartmentUpdateRequest departmentUpdateRequest) {
        DepartmentDTO departmentdTO = new DepartmentDTO();
        BeanUtils.copyProperties(departmentUpdateRequest, departmentdTO);
        departmentdTO.setUuid(departmentId);
        departmentBiz.updateByPrimaryKey(departmentdTO);
        DepartmentDTO findDepartmentDTO = departmentBiz.selectByPrimaryKey(departmentId);
        DepartmentUpdateResponse departmentUpdateResponse = new DepartmentUpdateResponse();
        BeanUtils.copyProperties(findDepartmentDTO, departmentUpdateResponse);

        return departmentUpdateResponse;
    }

    /**
     * 根据主键查找动作详情信息
     *
     * @param departmentId
     *            主键
     * @return DepartmentVO 详情响应对象
     */
    @Override
    public DepartmentVO findByPrimaryKey(@PathVariable("department_id") final String departmentId) {
        if (StringUtils.isEmpty(departmentId)) {
            LOGGER.warn("findByPrimaryKey param departmentId is null");
            return null;
        }
        DepartmentDTO departmentDTO = departmentBiz.selectByPrimaryKey(departmentId);
        DepartmentVO departmentVO = new DepartmentVO();
        if (null != departmentDTO) {
            BeanUtils.copyProperties(departmentDTO, departmentVO);
        }

        return departmentVO;
    }

    @Override
    public DepartmentVO findByName(@PathVariable("name") String name) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.warn("findByName param name is null");
            return null;
        }
        DepartmentDTO departmentDTO = departmentBiz.selectByName(name);
        if (null == departmentDTO) {
            return null;
        }
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(departmentDTO, departmentVO);
        return departmentVO;
    }

    @Override
    public List<DepartmentDTO> queryByDeptId(String deptId) {
        return departmentBiz.queryByDeptId(deptId);
    }

    /**
     * 根据主键查询动作集合信息
     *
     * @param departmentIds
     *            动作主键
     * @return DepartmentVO 动作查询响应对象
     */
    @Override
    public List<DepartmentVO> listByPrimaryKeyArrays(@PathVariable("department_id") final String departmentIds) {
        if (StringUtils.isEmpty(departmentIds)) {
            LOGGER.error("listByPrimaryKeyArrays param adepartmentIds is null");
            return Lists.newArrayList();
        }
        String[] departmentIdArrays = departmentIds.split(",");
        List<DepartmentDTO> listDepartmentDTO = departmentBiz.selectByPrimaryKeyArrays(departmentIdArrays);
        List<DepartmentVO> listDepartmentVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listDepartmentDTO)) {
            for (DepartmentDTO departmentDTO : listDepartmentDTO) {
                DepartmentVO departmentVO = new DepartmentVO();
                BeanUtils.copyProperties(departmentDTO, departmentVO);
                listDepartmentVO.add(departmentVO);
            }
        }
        return listDepartmentVO;
    }

    @Override
    public List<DepartmentDTO> queryList(@RequestBody DepartmentCreateRequest departmentCreateRequest) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(departmentCreateRequest, departmentDTO);
        return departmentBiz.select(departmentDTO);
    }

    /**
     * 根据查询条件获取分页列表获取分页列表
     * 
     * @param request
     *            分页查询监控对象
     * @return
     */
    @Override
    public PageResult<DepartmentDTO> pageList(@RequestBody DepartmentPageRequest request) {
        if (null == request) {
            LOGGER.warn("pageList param templatePageRequest is null");
            return null;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setPageNo(request.getPageNo());
        // 查询条件，可自己增加PageRequest中参数
        try {
            pageRequest.getDynamicQueryFields().putAll(org.apache.commons.beanutils.BeanUtils.describe(request));
        } catch (Exception e) {
        }
        /**
         * pageRequest.addFields("precinctId", request.getPrecinctId()); pageRequest.addFields("precinctName",
         * request.getPrecinctName()); pageRequest.addFields("precinctKind", request.getPrecinctKind());
         * pageRequest.addFields("lscId", request.getLscId()); pageRequest.addFields("areaCode", request.getAreaCode());
         */
        PageResult<DepartmentDTO> DepartmentDTOPageResponse = departmentBiz.pageList(pageRequest);
        return DepartmentDTOPageResponse;
    }

    @Override
    public ResponseEntity<String> batchCreatedDepartment(@RequestBody List<DepartmentCreateRequest> departmentCreateRequestList) {
        if (CollectionUtils.isEmpty(departmentCreateRequestList)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<DepartmentDTO> departmentDTOList = Lists.newArrayList();
        try {
            departmentCreateRequestList.forEach(e -> {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setUuid(e.getDepartmentId());
                BeanUtils.copyProperties(e, departmentDTO);
                departmentDTOList.add(departmentDTO);
            });
            departmentBiz.batchInsertOrUpdate(departmentDTOList);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("批量新增或更新失败!", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> batchDeleteByPrimaryKey(@RequestBody List<String> departmentIdList) {
        if (CollectionUtils.isEmpty(departmentIdList)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        try {
            departmentBiz.batchDeleteByPrimaryKey(departmentIdList);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("批量删除失败!", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@Override
	public List<DepartmentDTO> getAll() {
		return departmentBiz.getAll();
	}

    @Override
    public List<DepartmentUserDTO> queryDepartAndUser(String deptId) {
        return departmentBiz.queryDepartAndUser(deptId);
    }
}
