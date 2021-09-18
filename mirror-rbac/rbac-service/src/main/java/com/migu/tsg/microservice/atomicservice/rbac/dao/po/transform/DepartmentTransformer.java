package com.migu.tsg.microservice.atomicservice.rbac.dao.po.transform;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Department;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;

/**
 * 对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     Department
 * 类描述:    对应的PO与DTO的转换类
 * 创建人:     曾祥华
 * 创建时间:     2019-03-04 16:04:48
 */
public final class DepartmentTransformer {
	private DepartmentTransformer() {
    }
    
    /**
     * 将PO实体转换为动作DTO实体
     *
     * @param department 动作PO实体
     * @return DepartmentDTO 动作DTO实体
     */
    public static DepartmentDTO fromPo(final Department department) {
        if (null == department) {
            return null;
        }

        DepartmentDTO departmentDTO = new DepartmentDTO();
        
			departmentDTO.setUuid(department.getUuid());
			departmentDTO.setParentId(department.getParentId());
			departmentDTO.setName(department.getName());
			departmentDTO.setNo(department.getNo());
			departmentDTO.setDeptType(department.getDeptType());
			departmentDTO.setDescr(department.getDescr());
			departmentDTO.setLevel(department.getLevel());
			departmentDTO.setNamespace(department.getNamespace());

        return departmentDTO;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     *
     * @param listDepartment 业务实体对象集合
     * @return List<DepartmentDTO> 持久化对象集合
     */
    public static List<DepartmentDTO> fromPo(final List<Department> listDepartment) {
        if (CollectionUtils.isEmpty(listDepartment)) {
            return Lists.newArrayList();
        }
        List<DepartmentDTO> listDepartmentDTO = Lists.newArrayList();

        for (Department department : listDepartment) {
            listDepartmentDTO.add(DepartmentTransformer.fromPo(department));
        }
        return listDepartmentDTO;
    }

    /**
     * 将DTO实体转换为动作PO实体
     *
     * @param departmentDTO DTO实体类
     * @return Department PO实体
     */
    public static Department toPo(final DepartmentDTO departmentDTO) {
        if (null == departmentDTO) {
            return null;
        }

        Department department = new Department();
        
			department.setUuid(departmentDTO.getUuid());
			department.setParentId(departmentDTO.getParentId());
			department.setName(departmentDTO.getName());
			department.setNo(departmentDTO.getNo());
			department.setDeptType(departmentDTO.getDeptType());
			department.setDescr(departmentDTO.getDescr());
			department.setLevel(departmentDTO.getLevel());
			department.setNamespace(departmentDTO.getNamespace());
			department.setTop(departmentDTO.isTop());
        
        return department;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     *
     * @param listDepartmentDTO 业务实体对象集合
     * @return List<Department> 持久化对象集合
     */
    public static List<Department> toPo(final List<DepartmentDTO> listDepartmentDTO) {
        if (CollectionUtils.isEmpty(listDepartmentDTO)) {
            return Lists.newArrayList();
        }
        List<Department> listDepartment = Lists.newArrayList();

        for (DepartmentDTO departmentdTO : listDepartmentDTO) {
            listDepartment.add(DepartmentTransformer.toPo(departmentdTO));
        }
        return listDepartment;
    }

    /**
     * 将业务实体对象集合转换为Map
     *
     * @param listDepartmentDTO 业务实体对象集合
     * @return Map<String,DepartmentDTO> Map[key=String|value=DepartmentDTO]
     */
    public static Map<String, DepartmentDTO> toDTOMap(final List<DepartmentDTO> listDepartmentDTO) {
        if (CollectionUtils.isEmpty(listDepartmentDTO)) {
            return Maps.newHashMap();
        }
        Map<String, DepartmentDTO> departmentDTOMaps = Maps.newHashMap();

        for (DepartmentDTO departmentDTO : listDepartmentDTO) {
            departmentDTOMaps.put(departmentDTO.getUuid(), departmentDTO);
        }
        return departmentDTOMaps;
    }

    /**
     * 将业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listDepartmentDTO 业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<DepartmentDTO> listDepartmentDTO) {
        if (CollectionUtils.isEmpty(listDepartmentDTO)) {
            return null;
        }
        int size = listDepartmentDTO.size();
        String[] departmentIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            departmentIdArrays[i] = listDepartmentDTO.get(i).getUuid();
        }
        return departmentIdArrays;
    }

}
