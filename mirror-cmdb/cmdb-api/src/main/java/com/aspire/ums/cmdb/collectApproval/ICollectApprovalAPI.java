package com.aspire.ums.cmdb.collectApproval;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Api(value = "自动采集变更审批接口类")
@RequestMapping("/cmdb/collect/approval")
public interface ICollectApprovalAPI {

    /**
     * 获取采集配置项名称列表
     */
    @RequestMapping(value = "/getFiledNameList", method = RequestMethod.GET)
    @ApiOperation(value = "获取待审核的配置项名称列表", notes = "获取待审核的配置项名称列表", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getFiledNameList();

    /**
     * 获取变更方式列表
     * @return
     */
    @RequestMapping(value = "/getOperatorTypeList", method = RequestMethod.GET)
    @ApiOperation(value = "获取变更方式列表", notes = "获取变更方式列表", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getOperatorTypeList();


    /**
     * 获取变更审核条件和表头
     * @return
     */
    @RequestMapping(value = "/getApprovalHeaderCode", method = RequestMethod.GET)
    @ApiOperation(value = "获取变更审核条件和表头", notes = "获取变更审核条件和表头", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbSimpleCode> getApprovalHeaderCode(@RequestParam("moduleId") String moduleId);
    /**
     * 获取采集变更审批列表
     * @param approvalQuery 采集审批查询信息
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "新增码表信息", notes = "新增码表信息", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String,Object>> list(@RequestBody CmdbCollectApprovalQuery approvalQuery);


//    /**
//     * 审批通过/驳回
//     * @param approval 审批信息
//     * @param userName 用户信息
//     */
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    @ApiOperation(value = "审批通过/驳回", notes = "审批通过/驳回", tags = {"CMDB Collect API"})
//    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = void.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    Map<String, Object> update(@RequestParam("userName") String userName, @RequestBody CmdbCollectApproval approval);
    
//    /**
//     * 批量审批通过/驳回
//     * @param approveInfo 审批信息
//     */
//    @RequestMapping(value = "/batchUpdate/{ids}", method = RequestMethod.POST)
//    @ApiOperation(value = "批量审批通过/驳回", notes = "批量审批通过/驳回", tags = {"CMDB Collect API"})
//    @ApiResponses(value = {@ApiResponse(code = 204, message = "批量审核成功", response = void.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    Map<String, Object> batchUpdate(@RequestParam("username") String userName,
//                                    @PathVariable("ids") String ids,
//                                    @RequestBody Map<String, Object> approveInfo);

    /**
     * 批量入审核库
     * @param approvals 审批信息
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "批量入审核库", notes = "批量入审核库", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insert(@RequestBody List<CmdbCollectApproval> approvals);

}
