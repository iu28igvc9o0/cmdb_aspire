package com.aspire.mirror.composite.service.scada;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.scada.payload.ComPathRequest;
import com.aspire.mirror.composite.service.scada.payload.CompScadaCanvas;
import com.aspire.mirror.composite.service.scada.payload.CompScadaCanvasPageRequest;
import com.aspire.mirror.composite.service.scada.payload.ResMap;
import com.aspire.mirror.composite.service.scada.payload.ScadaBindValueRequest;
import com.aspire.mirror.composite.service.scada.payload.ScadaBindValueResponse;
import com.aspire.mirror.composite.service.scada.payload.ScadaCanvasSearchReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 组态视图模块暴露接口服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.scada
 * 类名称:    ICanvasService.java
 * 类描述:    组态视图模块暴露接口服务
 * 创建时间:  2019/10/8 14:40
 * 版本:      v1.0
 * @author 金素
 */
@Api(value = "组态管理")
@RequestMapping("/${version}/scada")
public interface IScadaCanvasService {


    /**
     * 删除视图
     * @param id
     * @return
     */
    @ApiOperation(value = "删除视图")
    @DeleteMapping(path = "/delScadaCanvas/{id}", produces = "application/json;charset=UTF-8")
    ResMap delScadaCanvas(@PathVariable("id") String id);

    /**
     * 根据Id获取视图详情
     * @param id
     * @return
     */
    @ApiOperation(value = "根据Id获取视图详情")
    @GetMapping(path = "/findScadaCanvasInfoById", produces = "application/json;charset=UTF-8")
    CompScadaCanvas findScadaCanvasInfoById(@RequestParam("id") String id);

//    /**
//     * 图片转格式
//     * @param picture
//     * @return
//     */
//    @ApiOperation(value = "图片转格式")
//    @PostMapping(value = "/pictureTransformatting")
//    ResMap pictureTransformatting(@RequestParam("picture") MultipartFile picture);

    /**
      * 保存视图
      * @param compScadaCanvas
      * @return
      */
    @ApiOperation(value = "保存视图")
    @PostMapping(value = "/saveScadaCanvas")
    ResMap saveScadaCanvas(@RequestBody CompScadaCanvas compScadaCanvas) throws Exception;


    @ApiOperation(value = "获取视图绑定值")
    @PostMapping(value = "/getScadaBindValue")
    List<ScadaBindValueResponse> getScadaBindValue(@RequestBody ScadaBindValueRequest bindValueRequest);
    /**
     * 根据监控对象Id获取视图详情
     * @param precinctId
     * @return
     */
//    @ApiOperation(value = "根据监控对象Id获取视图详情")
//    @GetMapping(path = "/getScadaCanvasInfoByPrecinctId", produces = "application/json;charset=UTF-8")
//    ResMap getScadaCanvasInfoByPrecinctId(@RequestParam(value = "precinctId") String precinctId,
//                                          @RequestParam(value = "pictureType", required = false) Integer pictureType) throws Exception;

//    /**
//     * 上传文件
//     * @param request
//     * @param file
//     * @return
//     * @throws Exception
//     */
//    @ApiOperation(value = "上传文件")
//    @PostMapping(value ="/upload")   //consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    ResMap  upload(HttpServletRequest request, @RequestParam(value = "files") MultipartFile file) throws Exception;

    /**
     * 根据设备树节点查询告警数量或者测点值
     * @param comTree
     * @return
     */
//    @ApiOperation(value = "根据设备树节点查询告警数量或者测点值")
//    @PostMapping(path = "/getAlertCountOrMeteValueByPrecinctId", produces = "/applicationjson;charset=UTF-8")
//    List<AlertAndMeteVO> getAlertCountOrMeteValueByPrecinctId(@RequestBody ComTree comTree);
    /**
     * 查询所有画布
     * edit by zhujiahao
     */
    @PostMapping(value = "/findScadaCanvasList")
    @ApiOperation(value = "查询指定节点下的视图信息,不传参则查询所有视图")
    ResMap findScadaCanvasList(@RequestBody ScadaCanvasSearchReq scadaCanvasSearchReq) throws Exception;

    /**
     * 查询所有在线视图
     * edit by 金素
     */
    @PostMapping(value = "/findOnlineScada")
    @ApiOperation(value = "查询指定节点下的视图信息,不传参则查询所有视图")
    ResMap findOnlineScada(@RequestBody ScadaCanvasSearchReq scadaCanvasSearchReq) throws Exception;
    
    @PostMapping(value = "/getPath")
    @ApiOperation(value = "查找路径")
    ResMap getPath(@RequestBody ComPathRequest pathRequest) throws Exception;

    /**
     * 告警列表
     * @param pageRequest 查询page对象
     * @return PageResponse 列表返回对象
     */
    @PostMapping(value = "/pageList")
    @ApiOperation(value = "列表", notes = "查找画布列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompScadaCanvas> pageList(@RequestBody CompScadaCanvasPageRequest pageRequest);

//    @ApiOperation(value = "获取所有视图节点信息")
//    @GetMapping(path = "/getScadaCanvasNodeInfo", produces = "application/json;charset=UTF-8")
//    List<ScadaCanvasNodeInfo> getScadaCanvasNodeInfo() throws Exception;


//    @ApiOperation(value = "上传excel，解析内容并返回")
//    @PostMapping(value ="/uploadExcel")
//    List<Map<String,Object>> getExcelData(@RequestParam(value = "files") MultipartFile file) throws Exception;


//===========================特殊任务==========================================================//

//    //特殊任务
//    //视图批量插入数据库
//    @ApiOperation(value = "视图批量插入数据库")
//    @PostMapping(value = "/ScadaCanvas/batchInsert")
//    String batchSaveScadaCanvas() throws Exception;
}
