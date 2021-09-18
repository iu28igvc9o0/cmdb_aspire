package com.aspire.ums.cmdb.restful.bpm;

import com.aspire.ums.cmdb.common.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IAutomateRestfulAPI
 * Author:   zhu.juwang
 * Date:     2019/9/11 10:55
 * Description: 该接口类用来专门提供接口给BPM使用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/restful/bpm")
public interface IBPMRestfulAPI {

    /**
     * 对接BPM资源申请流程
     * @param resourceInfo 申请资源信息
     *  {
     *    "bizSystem": "业务系统",
     *    "idcType": "资源池名称",
     *    "data": [{
     *          "resource_type": "资源类型",
     *          "num": "申请数量",
     *          "cpu": "云主机CPU数量",
     *          "memory": "云主机内存数量"
     *       }]
     *  }
     */
    @RequestMapping(value = "/resource/request", method = RequestMethod.POST)
    @ApiOperation(value = "对接BPM资源申请和回收流程", notes = "对接BPM资源申请和回收流程", tags = {"Cmdb BPM Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> resourceRequestProcess(@RequestBody Map<String, Object> resourceInfo);

    /**
     * 对接BPM组织信息
     * @param orgManagerData 组织部门信息
     * {
     *     "updateOrg": [
     *
     * 		{
     *             "descr": "中国移动通信集团安徽有限公司/中移服务分公司/人力资源部（党群工作部、纪检监察室）",
     *             "createdAt": "2020-08-13 08:52:32",
     *             "deleted": "true",
     *             "is_lv_one_company": "true",
     *             "parent_id": "ORG-AH-0017",
     *             "name": "人力资源部（党群工作部、纪检监察室）",
     *             "namespace": "PROVINCIAL_COMPANY",
     *             "uuid": "ORG-AH-0268",
     *             "updatedAt": "2020-08-13 08:52:32"
     *         }
     *     ],
     *     "insertOrg": [  {
     *             "descr": "中国移动通信集团浙江有限公司/舟山分公司/网络部",
     *             "createdAt": "2020-08-13 09:22:33",
     *             "deleted": "false",
     *             "is_lv_one_company": "false",
     *             "parent_id": "ORG-ZJ-0029",
     *             "name": "网络部xx",
     *             "namespace": "PROVINCIAL_COMPANY",
     *             "uuid": "ORG-ZJ-0318",
     *             "updatedAt": "2020-08-13 09:22:33"
     *         }]
     * }
     */
    @RequestMapping(value = "/resource/syncOrgSystem", method = RequestMethod.POST)
    @ApiOperation(value = "对接BPM组织信息", notes = "对接BPM组织信息", tags = {"Cmdb BPM Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> syncOrgSystem(@RequestBody Map<String, Object> orgManagerData);

    /**
     * 获取工单数据-提供给BPM
     * @return 返回所有实例数据
     */
    @RequestMapping(value = "/resource/listOrderReportData", method = RequestMethod.GET)
    @ApiOperation(value = "获取工单数据", notes = "获取工单数据", tags = {"Cmdb BPM Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> listOrderReportData(@RequestParam("time") String submitMonth);

    /**
     * 根据系统账号获取业务系统列表
     * 1. 如果账号有绑定业务系统, 则显示绑定的业务系统列表
     * 2. 如果账号没有绑定业务系统, 则显示账号归属的部门及该部门下所有子部门的业务系统列表
     * @param account 系统账号
     * @return
     */
    @RequestMapping(value = "/resource/getBizSystemListByAccount", method = RequestMethod.GET)
    @ApiOperation(value = "根据系统账号获取业务系统列表", notes = "根据系统账号获取业务系统列表", tags = {"Cmdb BPM Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String, Object>> getBizSystemListByAccount(@RequestParam("account") String account,
                                                          @RequestParam(value = "bizSystem", required = false) String bizSystem,
                                                          @RequestParam("currentPage") int currentPage,
                                                          @RequestParam("pageSize") int pageSize);
}
