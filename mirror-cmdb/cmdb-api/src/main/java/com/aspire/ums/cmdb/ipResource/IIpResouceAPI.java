package com.aspire.ums.cmdb.ipResource;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipResource.payload.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-06-16 14:19
 * @description
 */
@RequestMapping(value = "/cmdb/ipResource")
public interface IIpResouceAPI {

    @ApiOperation(value = "获取查询条件的下拉框数据", notes = "获取查询条件的下拉框数据", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/condition/getDropDownBoxList", method = RequestMethod.POST)
    ResultVo getDropDownBoxList(@RequestParam(value = "queryType") String queryType);

    @ApiOperation(value = "获取下拉框数据", notes = "获取下拉框数据", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/combobox/{path}", method = RequestMethod.POST)
    ResultVo getComboboxList(@PathVariable(value = "path") String path, @RequestParam(value = "pid", required = false) String pid);

    @ApiOperation(value = "获取一定数量的可分配IP", notes = "获取一定数量的可分配IP", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/autoAllocate/getIpListByCount", method = RequestMethod.POST)
    ResultVo getIpListByCount(@RequestBody AutoAllocateIpParam param);

    @ApiOperation(value = "网段列表查询(管理IP、业务IP1、业务IP2、consoleIp)", notes = "网段列表查询(管理IP、业务IP1、业务IP2、consoleIp)", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getSegmentInfoList", method = RequestMethod.POST)
    ResultVo getSegmentInfoList(@RequestBody SegmentInfoParam param);

    @ApiOperation(value = "IP列表查询", notes = "IP列表查询", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getIpInfoInfoList", method = RequestMethod.POST)
    ResultVo getIpInfoInfoList(@RequestBody IpInfoParam param);

    @ApiOperation(value = "资产选择弹框列表", notes = "资产选择弹框列表", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getAssetInfoList", method = RequestMethod.POST)
    ResultVo getAssetInfoList(@RequestBody AssetInfoParam param);

    @ApiOperation(value = "资产对应的所有IP类型查询列表", notes = "资产对应的所有IP类型查询列表", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getAssetIpList", method = RequestMethod.POST)
    ResultVo getAssetIpList(@RequestBody AssetIpInfoParam param);

    @ApiOperation(value = "网段-IP查询列表", notes = "包含IP变更、IP申领、IP回收", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getChangeIpList", method = RequestMethod.POST)
    ResultVo getChangeIpList(@RequestBody SegmentIpInfoParam param);


    @ApiOperation(value = "构建资产模板导出下拉框参数", notes = "构建资产模板导出下拉框参数", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getAssetExcelInfoList", method = RequestMethod.POST)
    ResultVo getAssetExcelInfoList();

    @ApiOperation(value = "构建资产模板导出头部参数", notes = "构建资产模板导出头部参数", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getAssetExcelHeaderList", method = RequestMethod.POST)
    ResultVo getAssetExcelHeaderList();

    @ApiOperation(value = "资产模板导出", notes = "资产模板导出", tags = {"Cmdb IpResource Restful API"})
    @GetMapping(value = "/export/exportAssetExcel")
    void exportAssetExcel(HttpServletResponse response) throws Exception;

    @ApiOperation(value = "IP地址库更新", notes = "在验证关单节点，通过流程的后置事件调用cmdb的ip状态更新接口更新对应IP信息", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/update/updateIpInfo", method = RequestMethod.POST)
    ResultVo updateIpInfo(@RequestBody IpInfoUpdateParam param);

    @ApiOperation(value = "资源回收-删除cmdb资产", notes = "在验证关单节点，通过流程的后置事件调用cmdb的资产删除接口删除对应的资产", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/update/delAssetInfoById", method = RequestMethod.POST)
    ResultVo delAssetInfoById(@RequestBody Map<String, String> param);

    @ApiOperation(value = "KVM虚拟机模板查询", notes = "KVM虚拟机模板查询", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/list/getKvmTemplateList", method = RequestMethod.POST)
    ResultVo getKvmTemplateList(@RequestBody Map<String, String> param);

    @ApiOperation(value = "通过参数获取查询条件的下拉框数据", notes = "通过参数获取查询条件的下拉框数据", tags = {"Cmdb IpResource Restful API"})
    @RequestMapping(value = "/condition/getDropDownBoxListByParam", method = RequestMethod.POST)
    ResultVo getDropDownBoxListByParam(@RequestBody Map<String, String> param);
}
