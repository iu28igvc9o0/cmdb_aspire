package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.biz.DepartmentBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.DepartmentDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Department;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.DepartmentUserTree;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.transform.DepartmentTransformer;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentUserDTO;

/**
 * 业务层实现类
 * <p>
 * 项目名称: 微服务运维平台 包: com.migu.tsg.microservice.atomicservice.rbac.biz.impl 类名称: DepartmentBizImpl 类描述: 业务层实现类 创建人: 曾祥华 创建时间:
 * 2019-03-04 16:04:48
 */
@Service("departmentBiz")
public class DepartmentBizImpl implements DepartmentBiz {

    /**
     * 系统日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentBizImpl.class);

    /**
     * dao
     */
    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 新增数据
     *
     * @param departmentDTO
     *            动作DTO对象
     * @return String 数据ID
     */
    @Override
    public String insert(final DepartmentDTO departmentDTO) {
        if (null == departmentDTO) {
            LOGGER.error("method[insert] param[departmentDTO] is null");
            throw new RuntimeException("param[departmentDTO] is null");
        }
        Department department = DepartmentTransformer.toPo(departmentDTO);
        String departmentId = department.getUuid();
        if(StringUtils.isEmpty(department.getUuid())){
        	departmentId = UUID.randomUUID().toString();
        	department.setUuid(departmentId);
        }
        departmentDao.insert(department);
        return departmentId;
    }

    @Override
    public void batchInsertOrUpdate(List<DepartmentDTO> departmentDTOList) {
        if (CollectionUtils.isEmpty(departmentDTOList)) {
            return;
        }
        List<Department> departmentList = Lists.newArrayList();
        departmentDTOList.forEach(e -> {
            Department department = DepartmentTransformer.toPo(e);
            String departmentId = e.getUuid();
            if (!StringUtils.hasText(departmentId)) {
                departmentId = UUID.randomUUID().toString();
            }
            department.setUuid(departmentId);
            departmentList.add(department);
        });
        List<List<Department>> partitionList = Lists.partition(departmentList, 200);
        partitionList.forEach(e -> departmentDao.insertOrUpdateBatchParam(e));
    }

    @Override
    public void batchDeleteByPrimaryKey(List<String> departmentIdList) {
        departmentDao.batchDeleteByPrimaryKey(departmentIdList);
    }

    /**
     * 根据主键删除数据
     *
     * @param departmentId
     *            主键
     * @return int 删除数据条数
     */
    @Override
    public int deleteByPrimaryKey(final String departmentId) {
        if (StringUtils.isEmpty(departmentId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[departmentId] is null");
            throw new RuntimeException("param[departmentId] is null");
        }
        return departmentDao.deleteByPrimaryKey(departmentId);
    }

    /**
     * 根据主键更新数据
     *
     * @param departmentDTO
     *            动作DTO对象
     * @return int 数据条数
     */
    @Override
    public int updateByPrimaryKey(final DepartmentDTO departmentDTO) {
        if (null == departmentDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[departmentDTO] is null");
            throw new RuntimeException("param[departmentDTO] is null");
        }
        if (StringUtils.isEmpty(departmentDTO.getUuid())) {
            LOGGER.warn("method[updateByPrimaryKey] param[departmentId] is null");
            throw new RuntimeException("param[departmentId] is null");
        }
        Department department = DepartmentTransformer.toPo(departmentDTO);
        return departmentDao.updateByPrimaryKey(department);
    }

    /**
     * 根据主键查询
     *
     * @param departmentId
     *            主键
     * @return DepartmentDTO 返回对象
     */
    @Override
    public DepartmentDTO selectByPrimaryKey(final String departmentId) {
        Department department = departmentDao.selectByPrimaryKey(departmentId);
        if (StringUtils.isEmpty(departmentId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[departmentId] is null");
            return null;
        }
        return DepartmentTransformer.fromPo(department);
    }

    /**
     * 根据dto实体查询列表
     *
     * @param departmentDTO
     *            动作DTO对象
     * @return List<Department> 返回集合
     */
    @Override
    public List<DepartmentDTO> select(final DepartmentDTO departmentDTO) {
        if (null == departmentDTO) {
            LOGGER.warn("select Object departmentDTO is null");
            return Collections.emptyList();
        }
        Department department = DepartmentTransformer.toPo(departmentDTO);
        List<Department> listDepartment = departmentDao.queryList(department);
        return DepartmentTransformer.fromPo(listDepartment);
    }

    /**
     * 根据主键数组查询
     *
     * @param departmentIdArrays
     *            主键数组
     * @return List<DepartmentDTO> 返回集合对象
     */
    @Override
    public List<DepartmentDTO> selectByPrimaryKeyArrays(final String[] departmentIdArrays) {
        if (ArrayUtils.isEmpty(departmentIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[departmentIdArrays] is null");
            return Collections.emptyList();
        }
        List<Department> listDepartment = departmentDao.selectByPrimaryKeyArrays(departmentIdArrays);
        return DepartmentTransformer.fromPo(listDepartment);
    }

    /**
     * 获取分页列表数据
     * 
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult<DepartmentDTO> pageList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = departmentDao.pageListCount(page);
        PageResult<DepartmentDTO> pageResponse = new PageResult<DepartmentDTO>();
        pageResponse.setCount(count);
        int pageCount = (count - 1) / page.getPageSize() + 1;
        pageResponse.setCurPage(page.getPageNo());
        pageResponse.setPageSize(page.getPageSize());
        pageResponse.setPageCount(pageCount);
        if (count > 0) {
            List<Department> listItem = departmentDao.pageList(page);
            List<DepartmentDTO> listDTO = DepartmentTransformer.fromPo(listItem);
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }

    @Override
    public DepartmentDTO selectByName(String name) {
        Department department = new Department();
        department.setName(name);
        List<Department> listDepartment = departmentDao.queryList(department);

        return CollectionUtils.isEmpty(listDepartment) ? null : DepartmentTransformer.fromPo(listDepartment.get(0));
    }

    @Override
    public List<DepartmentDTO> queryByDeptId(String deptId) {
        List<Department> listDepartment = departmentDao.queryByDeptId(deptId);
        return DepartmentTransformer.fromPo(listDepartment);
    }

	@Override
	public List<DepartmentDTO> getAll() {
		List<Department> listDepartment = departmentDao.getAll();
		return DepartmentTransformer.fromPo(listDepartment);
	}

    @Override
    public List<DepartmentUserDTO> queryDepartAndUser(String departId) {
        List<DepartmentUserDTO> list = Lists.newArrayList();
        List<DepartmentUserTree> departmentUserTreeList = departmentDao.queryDepartAndUserByDeptId(departId);
        for (DepartmentUserTree departmentUser : departmentUserTreeList) {
            BeanCopier beanCopier = BeanCopier.create(DepartmentUserTree.class, DepartmentUserDTO.class, false);
            DepartmentUserDTO dto = new DepartmentUserDTO();
            beanCopier.copy(departmentUser, dto, null);
            list.add(dto);
        }
        return list;
    }
}
