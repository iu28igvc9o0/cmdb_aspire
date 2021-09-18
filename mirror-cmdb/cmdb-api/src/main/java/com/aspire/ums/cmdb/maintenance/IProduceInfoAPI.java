package com.aspire.ums.cmdb.maintenance;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Concat;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoRequest;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoResq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "维保厂商信息接口类")
@RequestMapping("/cmdb/maintenProduce")
public interface IProduceInfoAPI {

    /**
     *  分页查询厂商信息
     * @return 模型列表
     */
    @PostMapping(value = "/listProduceByPage" )
    @ApiOperation(value = "分页查询厂商信息", notes = "分页查询厂商信息", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<ProduceInfoResq> selectProduceByPage(@RequestBody ProduceInfoRequest produceInfoRequest);
    /**
     *  根据id查询厂商详情
     * @return 模型列表
     */
    @GetMapping(value = "/getProduceById" )
    @ApiOperation(value = "根据id查询厂商详情", notes = "根据id查询厂商详情", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ProduceInfoResq getProduceById(@RequestParam("id") String id);

    /**
     *  新增厂商信息
     * @return 模型列表
     */
    @PostMapping(value = "/insertProduce" )
    @ApiOperation(value = "新增厂商信息", notes = "新增厂商信息", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insertProduce(@RequestBody ProduceInfoResq produceInfoRequest);

    /**
     *  修改厂商信息
     * @return 模型列表
     */
    @PostMapping(value = "/updateProduce" )
    @ApiOperation(value = "修改厂商信息", notes = "修改厂商信息", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> updateProduce(@RequestBody ProduceInfoResq produceInfoRequest);

    /**
     *  删除厂商信息
     * @return 模型列表
     */
    @DeleteMapping(value = "/deleteProduce" )
    @ApiOperation(value = "删除厂商信息", notes = "删除厂商信息", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> deleteProduce(@RequestParam("produceId") String produceId);



    @PostMapping(value = "/export")
    @ApiOperation(value = "导出数据", notes = "导出数据", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject export(@RequestBody Map<String, Object> sendRequest);

    @GetMapping(value = "/queryProduceInfoList")
    @ApiOperation(value = "查询厂商信息接口", notes = "查询厂商信息接口", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ProduceInfoResq> queryProduceInfoList();



    /**
     * 根据厂商ID和类型查询联系人信息
     * @param produceId
     * @return
     */
    @GetMapping(value = "/queryConcat" )
    @ApiOperation(value = "根据厂商ID和类型查询联系人信息", notes = "根据厂商ID和类型查询联系人信息", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "根据厂商ID和类型查询联系人信息成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Concat> queryConcat(@RequestParam("produceId") String produceId,
                             @RequestParam(value = "personType", required = false) String type);

    /**
     * 添加联系人
     * @param concat
     * @return
     */
    @PostMapping(value = "/addConcat" )
    @ApiOperation(value = "添加联系人", notes = "添加联系人", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "添加联系人", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> addConcat(@RequestBody Concat concat);

    /**
     * 更新联系人
     * @param concat
     * @return
     */
    @PostMapping(value = "/updateConcat" )
    @ApiOperation(value = "更新联系人", notes = "更新联系人", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "添加联系人", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> updateConcat(@RequestBody Concat concat);


    /**
     * 删除联系人
     * @param id
     */
    @DeleteMapping(value = "/deleteConcat" )
    @ApiOperation(value = "删除联系人", notes = "删除联系人", tags = {"CMDB Mainten Produce API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除联系人", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> deleteConcat(@RequestParam("id") String id);


}
