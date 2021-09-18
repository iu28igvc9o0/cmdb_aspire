package com.aspire.ums.cmdb.sync.client;

import com.aspire.mirror.common.configuration.FeignHttpClientConfiguration;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "CMDB", configuration = FeignHttpClientConfiguration.class)
public interface CmdbServiceClient {

    @GetMapping("/cmdb/orgManager/getAllEipOrg")
    @ApiOperation(value = "获取EIP组织列表", notes = "获取EIP组织列表", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<OrgManager> getAllEipOrg();

    /**
     * 新增组织信息
     * @param orgManager 组织信息对象
     * @return
     */
    @PostMapping("/cmdb/orgManager/eip-add")
    @ApiOperation(value = "新增组织信息", notes = "新增组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String insert(@RequestBody OrgManager orgManager);

    /**
     * 更新组织信息
     * @param orgManager 组织对象
     * @return
     */
    @PostMapping("/cmdb/orgManager/update")
    @ApiOperation(value = "更新组织信息", notes = "更新组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "修改成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String update(@RequestBody OrgManager orgManager);

    /**
     * 删除组织信息
     * @param id 组织ID
     * @return
     */
    @DeleteMapping("/cmdb/orgManager/deleteOrg")
    @ApiOperation(value = "删除组织信息", notes = "删除组织信息", tags = {"CMDB OrgManager API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteOrg(@RequestParam("id") String id);
}
