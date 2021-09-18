package com.migu.tsg.microservice.atomicservice.composite.service.logs;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.ConfigCompareLogsResp;
import com.aspire.mirror.log.api.dto.ConfigCompareReq;
import com.aspire.mirror.log.api.dto.ConfigCompareResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: IConfigCompareService
 * @Package com.aspire.mirror.log.api.service
 * @Description: TODO
 * @date 2020/12/17 11:42
 */
@Api(value = "主备设备比对接口")
@RequestMapping(value = "/v1/configCompare")
public interface ICompConfigCompareService {

    /**
     * 获取比对列表
     *
     * @param masterIp
     * @param backupIp
     * @param compareTimeFrom
     * @param compareTimeTo
     * @param compareType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取比对列表", notes = "获取比对列表", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<ConfigCompareResp> getCompareList(@RequestParam(name = "masterIp", required = false) String masterIp,
                                                   @RequestParam(name = "backupIp", required = false) String backupIp,
                                                   @RequestParam(name = "compareTimeFrom", required = false) String compareTimeFrom,
                                                   @RequestParam(name = "compareTimeTo", required = false) String compareTimeTo,
                                                   @RequestParam(name = "compareType", required = false) String compareType,
                                                   @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * 导出比对清单
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出比对清单", notes = "导出比对清单", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportCompareList(@RequestBody List<Integer> ids);

    /**
     * 下载模板
     *
     * @return
     */
    @PostMapping(value = "/downloadTemplate")
    @ApiOperation(value = "下载模板", notes = "下载模板", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void downloadTemplate();
    /**
     * 导出比对明细
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/exportDetail")
    @ApiOperation(value = "导出比对明细", notes = "导出比对明细", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportCompareDetailList(@RequestBody List<Integer> ids);

    /**
     * 比对
     *
     * @param id
     * @param masterIndex
     * @param backupIndex
     * @return
     */
    @PostMapping(value = "/compare/{id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "比对", notes = "比对", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> compare(@PathVariable("id") Integer id,
                              @RequestParam(name = "masterIndex") String masterIndex,
                              @RequestParam(name = "backupIndex") String backupIndex);

    /**
     * 新增
     *
     * @param configCompare
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增主备比对", notes = "新增主备比对", response = void.class, tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void insert(@RequestBody ConfigCompareReq configCompare);

    /**
     * 导入比对列表
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "导入比对列表", notes = "导入比对列表", response = Map.class, tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> importFile(@RequestPart("file") MultipartFile file);

    /**
     * 获取比对记录
     *
     * @param compareId
     * @return
     */
    @GetMapping(value = "/logs/{compare_id}")
    @ApiOperation(value = "获取比对记录", notes = "获取比对记录", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ConfigCompareLogsResp> getLogs(@PathVariable("compare_id") Integer compareId);

    /**
     * 导出比对记录
     *
     * @param compareId
     * @return
     */
    @GetMapping(value = "/exportLogs/{compare_id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "导出比对记录", notes = "导出比对记录", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportLogs(@PathVariable("compare_id") Integer compareId);

    /**
     * 获取索引列表
     *
     * @param compareId
     * @return
     */
    @GetMapping(value = "/index/{compare_id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取索引列表", notes = "获取索引列表", tags = {"CONFIG COMPARE DATA API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getIndex(@PathVariable("compare_id") Integer compareId);
}
