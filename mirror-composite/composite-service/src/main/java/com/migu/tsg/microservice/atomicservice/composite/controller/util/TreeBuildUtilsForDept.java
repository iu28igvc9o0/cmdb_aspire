package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.DepartmentTreePayload;

import java.util.List;
import java.util.stream.Collectors;

/** 根据父节点的ID获取所有子节点 */
public class TreeBuildUtilsForDept {

  public static List<DepartmentTreePayload> build(List<DepartmentTreePayload> list, String parentId){
    List<DepartmentTreePayload> filterList;
    List<DepartmentTreePayload> returnList = Lists.newArrayList();

    //1 遍历出与根节点id相同的父数据 , 以父部门分组 快速 建树 , 递归逻辑也是如此 >> 通过过滤来建立分组, 减少递归次数
    filterList = list.parallelStream().filter(orgs -> parentId.equals(orgs.getParentId())).collect(Collectors.toList());
    for (DepartmentTreePayload obj : filterList) {
      if(!parentId.equals(obj.getParentId())) continue;
      recursionFn(list, obj);
      returnList.add(obj);
    }


    return returnList;
  }

  /** 递归列表 */
  private static boolean recursionFn(
      List<DepartmentTreePayload> list, DepartmentTreePayload t)
     {
    // 得到子节点列表
    List<DepartmentTreePayload> childList = getChildList(list, t);
    t.setChildList(childList);
    //过滤出子部门
       List<DepartmentTreePayload> filterChildList=
       childList.parallelStream().filter(orgs ->hasChild(list,orgs)).collect(Collectors.toList());

       for (DepartmentTreePayload dept : filterChildList) {
         recursionFn(list, dept);
       }
return false;

  }

  /** 得到子节点列表 */
  private static List<DepartmentTreePayload> getChildList(List<DepartmentTreePayload> list, DepartmentTreePayload t)
      {
    List<DepartmentTreePayload> tList =list.parallelStream().filter(orgs -> t.getUuid()
            .equals(orgs.getParentId())).collect(Collectors.toList());
    return tList;
  }

  /** 判断是否有子节点 */
  private static boolean hasChild(List<DepartmentTreePayload> list, DepartmentTreePayload t){
    return !getChildList(list, t).isEmpty();
  }
}
