package com.aspire.mirror.composite.service.faultReport;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.faultReport.CompFaultReportManageReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/v1/alerts/fault_manage")
@Api(value = "故障管理")
public interface ICompFaultReportManageService {

    /**
     * 新增
     *
     * @param manage
     * @return
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "新增故障管理", notes = "新增故障管理", response = void.class, tags = {"fault report manage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void insert(@RequestBody CompFaultReportManageReq manage);

    /**
     * 跟新
     *
     * @param manage
     * @return
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更新故障管理", notes = "更新故障管理", response = void.class, tags = {"fault report manage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = void.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = void.class)})
    void update(@RequestBody CompFaultReportManageReq manage);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询详情", notes = "查询详情", response = CompFaultReportManageReq.class, tags = {"fault report manage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = CompFaultReportManageReq.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompFaultReportManageReq.class)})
    CompFaultReportManageReq selectById(@PathVariable("id") Integer id);

    /**
     * 导出附件
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/exportAnnex", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出附件", notes = "导出附件", response = Map.class, tags = {"fault report manage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> exportAnnex(@RequestBody List<Integer> ids);

    /**
     * 列表查询
     *
     * @param faultReportUser
     * @param reportUser
     * @param faultHappenTimeFrom
     * @param faultHappenTimeTo
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "列表查询", notes = "列表查询", response = PageResponse.class, tags = {"fault report manage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompFaultReportManageReq> selectListByParams(@RequestParam(value = "faultReportUser",required = false) String faultReportUser,
                                                          @RequestParam(value = "reportUser",required = false) String reportUser,
                                                          @RequestParam(value = "faultHappenTimeFrom",required = false) String faultHappenTimeFrom,
                                                          @RequestParam(value = "faultHappenTimeTo",required = false) String faultHappenTimeTo,
                                                          @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum);

    /**
     * 导出列表
     *
     * @param faultReportUser
     * @param reportUser
     * @param faultHappenTimeFrom
     * @param faultHappenTimeTo
     * @return
     */
    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出列表", notes = "导出列表", response = Map.class, tags = {"fault report manage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> export(@RequestParam(value = "faultReportUser",required = false) String faultReportUser,
                               @RequestParam(value = "reportUser",required = false) String reportUser,
                               @RequestParam(value = "faultHappenTimeFrom",required = false) String faultHappenTimeFrom,
                               @RequestParam(value = "faultHappenTimeTo",required = false) String faultHappenTimeTo);

    /**
     * 导入
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/import/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "导入", notes = "导入", response = Map.class, tags = {"fault report manage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> importFile(@PathVariable("id") Integer id, @RequestPart("file") MultipartFile file);
}
