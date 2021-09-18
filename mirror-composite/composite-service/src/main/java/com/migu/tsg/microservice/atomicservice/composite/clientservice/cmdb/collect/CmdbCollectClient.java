package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.collect;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@FeignClient(value = "CMDB")
public interface CmdbCollectClient {

    /**
     * 获取采集配置项名称列表
     */
    @RequestMapping(value = "/cmdb/collect/approval/getFiledNameList", method = RequestMethod.GET)
    List<Map<String, String>> getFiledNameList();

    /**
     * 获取变更方式列表
     * @return
     */
    @RequestMapping(value = "/cmdb/collect/approval/getOperatorTypeList", method = RequestMethod.GET)
    @ApiOperation(value = "获取变更方式列表", notes = "获取变更方式列表", tags = {"CMDB Collect API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getOperatorTypeList();
    /**
     * 获取采集变更审批列表
     * @param approvalQuery 采集审批查询信息
     */
    @RequestMapping(value = "/cmdb/collect/approval/list", method = RequestMethod.POST)
    Result<Map<String, Object>> list(@RequestBody CmdbCollectApprovalQuery approvalQuery);

    @RequestMapping(value = "/cmdb/collect/approval/getApprovalHeaderCode", method = RequestMethod.GET)
    List<CmdbSimpleCode> getApprovalHeaderCode(@RequestParam("moduleId") String moduleId);
    /**
     * 审批通过/驳回
     * @param approval 审批信息
     */
    @RequestMapping(value = "/cmdb/collect/approval/update", method = RequestMethod.POST)
    Map<String, Object> update(@RequestParam("username") String userName,
                               @RequestBody CmdbCollectApproval approval);
    
//    /**
//     * 批量审批通过/驳回
//     * @param approveInfo 审批信息
//     */
//    @RequestMapping(value = "/cmdb/collect/approval/batchUpdate/{ids}", method = RequestMethod.POST)
//    Map<String, Object> batchUpdate(@RequestParam("username") String userName,
//                                    @PathVariable("ids") String ids,
//                                    @RequestBody Map<String, Object> approveInfo);
}
