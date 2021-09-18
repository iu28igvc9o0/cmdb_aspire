package com.aspire.ums.cmdb.v2.restful.service;

import com.aspire.ums.cmdb.common.Result;

import java.util.List;
import java.util.Map;

public interface IBPMRestfulService {
    /**
     * 对接BPM资源申请流程
     * @param resourceInfo 申请资源信息
     *  {
     *    "bizSystem": "业务系统",
     *    "idcType": "资源池名称",
     *    "request_type": "request/release",
     *    "data": [{
     *          "type": "资源类型",
     *          "num": "申请数量",
     *          "cpu": "云主机CPU数量",
     *          "memory": "云主机内存数量"
     *       }]
     *  }
     */
    Map<String, Object> resourceRequestProcess(Map<String, Object> resourceInfo);

    Map<String, Object> syncOrgSystem(Map<String, Object> orgManagerData);

    /**
     * 根据系统账号获取业务系统列表
     * 1. 如果账号有绑定业务系统, 则显示绑定的业务系统列表
     * 2. 如果账号没有绑定业务系统, 则显示账号归属的部门及该部门下所有子部门的业务系统列表
     *
     * @param account 系统账号
     * @return
     */
    Result<Map<String, Object>> getBizSystemListByAccount(String account, String bizSystem, int currentPage, int pageSize);

    /**
     * 获取31省份运维工单数据
     */
    List<Map<String, Object>> listOrderReportData(String submitTime);
}
