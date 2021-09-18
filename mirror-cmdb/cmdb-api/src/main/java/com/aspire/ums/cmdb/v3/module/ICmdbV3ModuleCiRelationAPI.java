package com.aspire.ums.cmdb.v3.module;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetail;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetailResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCiRelationAPI
 * Author:   hangfang
 * Date:     2020/4/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/v3/cmdb/module/relation")
public interface ICmdbV3ModuleCiRelationAPI {
    /**
     * 根据模型id获取所模型关系
     * @param
     * @return
     */
    @GetMapping("/listByModuleId")
    @ApiOperation(value = "根据模型id获取所模型关系", notes = "根据模型id获取所模型关系", tags = {"Cmdb Module Relation API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbV3ModuleCiRelation> listByModuleId(@RequestParam("moduleId") String moduleId);


    /**
     * 根据模型id获取所模型关系
     * @param
     * @return
     */
    @GetMapping("/listRDetailByModuleId")
    @ApiOperation(value = "根据模型id获取所模型关系", notes = "根据模型id获取所模型关系", tags = {"Cmdb Module Relation API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbV3ModuleCiRelationDetail> listDetailByModuleId(@RequestParam("moduleId") String moduleId);

    /**
     * 保存模型关系
     * @param
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存模型关系", notes = "保存模型关系", tags = {"Cmdb Module Relation API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestParam("userName") String userName, @RequestBody CmdbV3ModuleCiRelation relation);

    /**
     * 更新模型关系
     * @param
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新模型关系", notes = "更新模型关系", tags = {"Cmdb Module Relation API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "更新成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> update(@RequestParam("userName") String userName, @RequestBody CmdbV3ModuleCiRelation relation);

    /**
     * 保存模型关系
     * @param
     * @return
     */
    @DeleteMapping("/deleteById")
    @ApiOperation(value = "删除模型关系", notes = "删除模型关系", tags = {"Cmdb Module Relation API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> deleteById(@RequestParam("id") String id);

    /**
     * 根据关系查询当前ci关系详情
     * @param
     * @return
     */
    @GetMapping("/getCiRelationDetail")
    @ApiOperation(value = "根据关系查询当前ci关系详情", notes = "根据关系查询当前ci关系详情", tags = {"Cmdb Module Relation API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbV3ModuleCiRelationDetailResponse getCiRelationDetail(@RequestParam("relationId") String relationId,
                                                             @RequestParam("instanceId") String instanceId);

}
