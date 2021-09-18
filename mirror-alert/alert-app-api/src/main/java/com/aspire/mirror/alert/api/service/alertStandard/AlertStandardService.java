package com.aspire.mirror.alert.api.service.alertStandard;

import com.aspire.mirror.alert.api.dto.alertStandard.AlertStandardReq;
import com.aspire.mirror.alert.api.dto.alertStandard.AlertStandardResp;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

/**
 * @projectName: AlertStandardService
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-06-10 16:14
 **/
@RequestMapping("/alert/standard")
public interface AlertStandardService {
    /*
     * 新增告警标准化实体
     * */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增告警标准化实体", notes = "新增告警标准化实体", response = ResponseEntity.class, tags = {"AlertStandardService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> insert(@RequestBody AlertStandardReq as);

    /*
     * 修改告警标准化实体
     * */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改告警标准化实体", notes = "修改告警标准化实体", response = ResponseEntity.class, tags = {"AlertStandardService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> update(@RequestBody AlertStandardReq as);

    /*
     * 批量删除告警标准化实体
     * */
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "批量删除告警标准化实体", notes = "批量删除告警标准化实体",
            tags = {"AlertStandardService API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    ResponseEntity<String> deleteByIds(@RequestParam("operator") String operator,
                                       @RequestBody String[] ids);


    /*
     * 分页查询告警标准化列表
     * */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "分页查询告警标准化列表", notes = "分页查询告警标准化列表", response = PageResponse.class, tags = {"AlertStandardService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<AlertStandardResp> listWithPage(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                 @RequestParam(value = "deviceType",required = false) String deviceType,
                                                 @RequestParam(value = "monitorKey",required = false) String monitorKey,
                                                 @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
                                                 @RequestParam(value = "pageSize",defaultValue = "50") int pageSize);


    /*
     * 修改告警标准化状态 禁用或者启动
     * */
    @PutMapping(value = "/update/status")
    @ApiOperation(value = "修改告警标准化状态 禁用或者启动", notes = "修改告警标准化状态 禁用或者启动",
            tags = {"AlertStandardService API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功")})
    ResponseEntity<String> operatorStatus(@RequestParam("operator") String operator,
                                          @RequestBody String[] ids);

    /*
     * 状态为启用的告警标准名称，全部同步历史告警数据
     * */
    @PutMapping(value = "/update/history", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "状态为启用的告警标准名称，全部同步历史告警数据", notes = "状态为启用的告警标准名称，全部同步历史告警数据", response = ResponseEntity.class, tags = {"AlertStandardService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> updateHistory(@RequestBody Map<String,String> req);

    /*
     * 同步历史告警数据
     * */
    @PutMapping(value = "/update/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "某一个告警标准名称，同步历史告警数据", notes = "某一个告警标准名称，同步历史告警数据", response = ResponseEntity.class, tags = {"AlertStandardService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> updateHistoryOneRow(@PathVariable("id") String id,
                                               @RequestBody Map<String,String> req);

    /*
     * 导入告警标准数据
     * */
    @PostMapping(value = "/import",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "导入告警标准数据", notes = "导入告警标准数据", response = ResponseEntity.class, tags = {"AlertStandardService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    ResponseEntity<String> importAlertStandard(@RequestPart("file") MultipartFile file,
                                               @RequestParam("operator") String operator);

    /*
     * 导出全量告警标准数据
     * */
    @GetMapping(value = "/export")
    @ApiOperation(value = "导出全量告警标准数据", notes = "导出全量告警标准数据", response = ResponseEntity.class, tags = {"AlertStandardService API"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<Map<String,Object>> exportAlertStandard(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                 @RequestParam(value = "deviceType",required = false) String deviceType,
                                                 @RequestParam(value = "monitorKey",required = false) String monitorKey);
}
