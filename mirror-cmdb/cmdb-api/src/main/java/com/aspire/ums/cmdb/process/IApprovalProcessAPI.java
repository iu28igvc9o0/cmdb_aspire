package com.aspire.ums.cmdb.process;

import com.aspire.ums.cmdb.collectApproval.payload.CmdbApprovalUpdateReq;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IProcessAPI
 * Author:   zhu.juwang
 * Date:     2019/6/11 9:55
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/process")
public interface IApprovalProcessAPI {

    /**
     * 批量审核
     * @param userName 审核人
     * @param updateReq 审核信息实例
     */
    @RequestMapping(value = "/approval", method = RequestMethod.POST)
    @ApiOperation(value = "批量审核", notes = "批量审核", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "审核成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> approvalProcess(@RequestParam("userName") String userName, @RequestBody CmdbApprovalUpdateReq updateReq);

    /**
     * 批量更新CI入审核库
     */
    @RequestMapping(value = "/batchUpdateInstance", method = RequestMethod.POST)
    @ApiOperation(value = "批量更新CI入审核库", notes = "批量更新CI入审核库", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "入审核库成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> batchUpdateInstance(@RequestParam("userName") String userName,
                                            @RequestParam("moduleId") String moduleId,
                                            @RequestBody Map<String, Object> batchUpdate);

    /**
     * 获取审核进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/approval/{processId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取导入文件进度", notes = "获取导入文件进度", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "审核成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getApprovalProcess(@PathVariable("processId") String processId);

    /**
     * 移除审核进度
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/remove/approval/{processId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "移除审核进度", notes = "移除审核进度", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "审核成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> removeApprovalProcess(@PathVariable("processId") String processId);

    /**
     * 下载失败文件
     * @param processId 处理进程ID
     * @return
     */
    @RequestMapping(value = "/approval/export/error/{processId}", method = RequestMethod.POST)
    @ApiOperation(value = "下载失败文件", notes = "下载失败文件", tags = {"CMDB Process API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "下载成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ImportProcess exportErrorFile(@PathVariable("processId") String processId);
}
