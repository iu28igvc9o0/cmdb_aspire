package com.aspire.mirror.composite.service.notify;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.composite.payload.notify.CompSmsRecordExportVo;
import com.aspire.mirror.composite.payload.notify.CompSmsRecordReq;
import com.aspire.mirror.composite.payload.notify.CompSmsTemplateVo;
import com.aspire.mirror.composite.payload.notify.SmsListParamReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author menglinjie
 */
@RequestMapping("${version}/alerts/sms")
@Api("发送短信管理")
public interface ICompSmsService {

    /**
     * 发送短信
     * @param compSmsRecordReq
     * @return
     */
    @PostMapping(value = "/sendSms")
    @ApiOperation(value = "发送短信", notes = "发送短信", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> sendSms(@RequestBody CompSmsRecordReq compSmsRecordReq);

    /**
     * findSmsList
     * @param param
     * @return
     */
    @PostMapping(value = "/findSmsList")
    @ApiOperation(value = "查询短信列表", notes = "查询短信列表", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> findSmsList(@RequestBody SmsListParamReq param);

    /**
     * 新增短信模板分类
     * @param voList
     * @return
     */
    @PostMapping(value = "/addSmsTemplate")
    @ApiOperation(value = "新增短信模板分类", notes = "新增短信模板分类", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map>  addSmsTemplate(@RequestBody List<CompSmsTemplateVo> voList);

    /**
     * 查询模板分类列表
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/findSmsTemplateList")
    @ApiOperation(value = "查询模板分类列表", notes = "查询模板分类列表", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> findSmsTemplateList(@ApiParam(name="pageNo",required = false) @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                    @ApiParam(name="pageSize",required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize) throws ParseException;

    /**
     * 将短信保存为模板
     * @param vo
     * @return
     */
    @PostMapping(value = "/addContentToTemplate")
    @ApiOperation(value = "将短信保存为模板", notes = "将短信保存为模板", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> addContentToTemplate(@RequestBody CompSmsTemplateVo vo);

    /**
     * 删除模板
     * @param vo
     * @return
     */
    @PostMapping(value = "/deleteTemplate")
    @ApiOperation(value = "删除模板", notes = "删除模板", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> deleteTemplate(@RequestBody CompSmsTemplateVo vo);
    
    /**
     * 删除模板下面内容
     * @param vo
     * @return
     */
    @PostMapping(value = "/deleteTemplateContent")
    @ApiOperation(value = "删除模板下面内容", notes = "删除模板下面内容", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> deleteTemplateContent(@RequestBody CompSmsTemplateVo vo);
    
    /**
     * 编辑模板下面内容
     * @param vo
     * @return
     */
    @PostMapping(value = "/editTemplateContent")
    @ApiOperation(value = "编辑模板下面内容", notes = "编辑模板下面内容", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> editTemplateContent(@RequestBody CompSmsTemplateVo vo);
    
    /**
     * 删除模板
     * @param vo
     * @return
     */
    @PostMapping(value = "/editTemplate")
    @ApiOperation(value = "编辑模板", notes = "编辑模板", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> editTemplate(@RequestBody CompSmsTemplateVo vo);

    /**
     * 根据条件查询模板短信
     * @param templateId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ParseException
     */
    @GetMapping(value = "/findDetailListByCondition")
    @ApiOperation(value = "根据条件查询模板短信", notes = "根据条件查询模板短信", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> findDetailListByCondition(@ApiParam(name="templateId",required = false) @RequestParam(value = "templateId", required = false) String templateId,
                                                   @ApiParam(name="content",required = false) @RequestParam(value = "content", required = false) String content,
                                                   @ApiParam(name="pageNo",required = false) @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                   @ApiParam(name="pageSize",required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize) throws ParseException;



    /**
     * 删除短信
     * @param req
     * @return
     */
    @PostMapping(value = "/deleteSmsRecord")
    @ApiOperation(value = "删除短信", notes = "删除短信", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> deleteSmsRecord(@RequestBody CompSmsRecordReq req);

    /**
     * 导出短信记录
     * @param vo
     * @throws ParseException
     */
    @PostMapping("/exportSmsRecord")
    @ApiOperation(value = "导出短信记录", notes = "导出短信记录", tags = {"Sms API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<Map> exportSmsRecord(@RequestBody CompSmsRecordExportVo vo ) throws ParseException ;
}
