package com.aspire.mirror.scada.api.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.scada.api.dto.PathRequest;
import com.aspire.mirror.scada.api.dto.ScadaCanvasPageRequest;
import com.aspire.mirror.scada.api.dto.ScadaCanvasReq;
import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;
import com.aspire.mirror.scada.common.entity.ResMap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 视图对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.scada.entity
 * 类名称:     ScadaCanvasService
 * 类描述:     对外暴露接口层
 * 创建时间:     2019-06-11 11:32:23
 *
 * @author JinSu
 */

@Api(value = "")
public interface ScadaCanvasService {

    /**
     * 根据主键删除视图
     *
     * @param scadaCanvasId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/scadaCanvas/{id}")
    @ApiOperation(value = "删除视图")
    ResMap deleteByPrimaryKey(@PathVariable("id") String scadaCanvasId);

    /**
     * 根据主键修改视图
     *
     * @param scadaCanvasReq 修改请求对象
     * @return CanvasUpdateResponse 修改响应对象
     */
    @PutMapping(value = "/v1/scadaCanvas/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改视图")
    ResMap modifyByPrimaryKey(@RequestBody ScadaCanvasReq scadaCanvasReq);

    /**
     * 根据主键查作详情信息
     *
     * @param scadaCanvasId 动作主键
     * @return CanvasVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/scadaCanvas")
    @ApiOperation(value = "详情")
    ScadaCanvasDTO findByPrimaryKey(@RequestParam("id") String scadaCanvasId);

//    /**
//     * 图片转格式
//     *
//     * @param picture
//     * @return
//     */
//    @PostMapping(value = "/v1/pictureTransformatting")
//    @ApiOperation(value = "图片转格式")
//    ResMap pictureTransformatting(@RequestParam("picture") MultipartFile picture);

    /**
     * 新增视图
     *
     * @param scadaCanvasReq
     * @return
     */
    @PostMapping(value = "/v1/scadaCanvas/insert")
    @ApiOperation(value = "新增视图")
    ResMap creatScadaCanvas(@RequestBody ScadaCanvasReq scadaCanvasReq);

//    /**
//     * 根据监控对象Id获取视图详情
//     *
//     * @param precinctId
//     * @param pictureType
//     * @return
//     */
//    @GetMapping(value = "/v1/getScadaCanvasInfoByPrecinctId")
//    @ApiOperation(value = "根据监控对象Id获取视图详情")
//    ResMap getScadaCanvasInfoByPrecinctId(@RequestParam("precinctId") String precinctId,
//                                          @RequestParam("pictureType") Integer pictureType);

//    /**
//     * 上传文件
//     *
//     * @param request
//     * @param file
//     * @return
//     * @throws Exception
//     */
//    @PostMapping(value = "/upload")  //consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    @ApiOperation(value = "上传文件")
//    ResMap upload(HttpServletRequest request, @RequestParam(value = "files") MultipartFile file) throws Exception;


//    /**
//     * 将服务器文件以IO流形式传给前端
//     *
//     * @param remoteFilePath
//     * @param response
//     * @throws Exception
//     */
//    @PostMapping(value = "/getFile")
//    @ApiOperation(value = "下载文件")
//    public void getFile(@PathVariable("remoteFilePath") String remoteFilePath, @PathVariable("response")
//            HttpServletResponse response) throws Exception;

//    /**
//     * 上传文件
//     *
//     * @param request
//     * @param buffer
//     * @param fileName
//     * @return
//     */
//    @PostMapping(value = "/uploadByByte")
//    @ApiOperation(value = "上传文件")
//    ResMap uploadByByte(HttpServletRequest request, @RequestParam(value = "buffer") byte[] buffer,
//                        @RequestParam(value = "fileName") String fileName);

    /**
     * 查询所有画布
     *
     * @return
     */
    @PostMapping(value = "/v1/findScadaCanvasList")
    @ApiOperation(value = "获取拓扑图列表")
    ResMap findScadaCanvasList(@RequestBody(required=false) ScadaCanvasReq scadaCanvasReq);


//===========================特殊任务==========================================================//
//
//    @GetMapping(value = "/v1/scadaCanvas/read")
//    @ApiOperation(value = "将文件名和节点id关联",
//            notes = "将文件名和节点id关联",
//            tags = {"scadaCanvas_mytest"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
//            @ApiResponse(code = 500, message = "内部错误")})
//    String batchFindPrecinctId();


//    @GetMapping(value = "/v1/scadaCanvas/batchinsert")
//    @ApiOperation(value = "视图批量插入数据库",
//            notes = "视图批量插入数据库",
//            tags = {"scadaCanvas_mytest"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
//            @ApiResponse(code = 500, message = "内部错误")})
//    String batchSaveScadaCanvas();

    @GetMapping(value = "/v1/scadaCanvas/findScadaCanvasByName")
    @ApiOperation(value = "根据名称查询")
    ScadaCanvasDTO findScadaCanvasByName(@RequestParam("name") String name);

    @PostMapping(value = "/v1/scadaCanvas/pageList")
    PageResponse<ScadaCanvasDTO> pageList(@RequestBody ScadaCanvasPageRequest scadaCanvasPageRequest);

    @PostMapping(value = "/v1/scadaCanvas/getPath")
    ResMap getPath(@RequestBody PathRequest pathRequest) throws Exception;
}
