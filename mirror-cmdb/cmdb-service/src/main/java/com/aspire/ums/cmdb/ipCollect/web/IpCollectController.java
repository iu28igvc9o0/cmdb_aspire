package com.aspire.ums.cmdb.ipCollect.web;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.deviceStatistic.util.ExportExcelUtil;
import com.aspire.ums.cmdb.ipCollect.IIpCollectAPI;
import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.BaseInstanceRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;
import com.aspire.ums.cmdb.ipCollect.service.*;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.JavaBeanUtil;
import com.aspire.ums.cmdb.util.JsonUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/19 18:45
 */
@Slf4j
@RestController
public class IpCollectController implements IIpCollectAPI {
    @Autowired
    private CmdbCollectApiService cmdbCollectApiService;
    @Autowired
    private CmdbIpCollectService cmdbIpCollectService;
    @Autowired
    private CmdbVipCollectService cmdbVipCollectService;
    @Autowired
    private CmdbVmwareInstanceLogService instanceLogService;
    @Autowired
    private VmwareFullSyncApiService vmwareFullSyncApiService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public ResultVo create(@RequestBody InstanceCreateRequest createRequest) {
        log.info(">>>>> 接收到自动化平台推送的[创建-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo(true, "保存成功!");
        createRequest.setRequestBody(JsonUtil.toJacksonJson(createRequest));
        log.info("获取到的自动化请求requestBody={}", createRequest.getRequestBody());
        try {
            if (!cmdbCollectApiService.instanceCreate(createRequest)) {
                resultInfo.setSuccess(false);
                resultInfo.setMsg("保存失败！");
            } else {
                resultInfo.setSuccess(true);
            }

        } catch (Exception e) {
            log.error("保存失败！", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg(String.format("保存失败！%s", e.getMessage()));
        }
        log.info("<<<< 自动化平台推送的[创建-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo update(@RequestBody InstanceUpdateRequest updateRequest) {
        log.info(">>>>> 接收到自动化平台推送的[修改-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo(true, "更新成功！");
        updateRequest.setRequestBody(JsonUtil.toJacksonJson(updateRequest));
        log.info("获取到的自动化请求requestBody={}", updateRequest.getRequestBody());
        try {
            if (!cmdbCollectApiService.instanceUpdate(updateRequest)) {
                resultInfo.setSuccess(false);
                resultInfo.setMsg("更新失败！");
            } else {
                resultInfo.setSuccess(true);
            }
        } catch (Exception e) {
            log.error("更新失败！", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg(String.format("更新失败！%s", e.getMessage()));
        }
        log.info("<<<< 自动化平台推送的[修改-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo delete(@RequestBody InstanceDeleteRequest deleteRequest) {
        log.info(">>>>> 接收到自动化平台推送的[删除-实例]请求 >>>>");
        ResultVo resultInfo = new ResultVo(true, "删除成功！");
        deleteRequest.setRequestBody(JsonUtil.toJacksonJson(deleteRequest));
        log.info("获取到的自动化请求requestBody={}", deleteRequest.getRequestBody());
        try {
            if (!cmdbCollectApiService.instanceDelete(deleteRequest)) {
                resultInfo.setSuccess(false);
                resultInfo.setMsg("删除成功！");
            } else {
                resultInfo.setSuccess(true);
            }
        } catch (Exception e) {
            log.error("删除成功！", e);
            resultInfo.setSuccess(false);
            resultInfo.setMsg(String.format("删除成功！%s", e.getMessage()));
        }
        log.info("<<<< 自动化平台推送的[删除-实例]处理完成! <<<<<");
        return resultInfo;
    }

    @Override
    public ResultVo<List<CmdbIpCollectResponse>> findListS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest) {
        log.info("IpCollectController.findList is {}", cmdbIpCollectRequest);
        ResultVo resultVo = new ResultVo(true, "查询成功！");
        try {
            cmdbIpCollectRequest.updatePageNo();
            cmdbIpCollectRequest.setPageNo(0);
            cmdbIpCollectRequest.setPageSize(0);
            setHisFlag4CmdbIp(cmdbIpCollectRequest);
            resultVo.setData(buildIpPageCondition(cmdbIpCollectRequest));
        } catch (Exception e) {
            log.error("findList 查询失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg(String.format("findList 查询失败！%s", e.getMessage()));
        }
        return resultVo;
    }

    @Override
    public CmdbIpCollectResult findPageS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest) {
        log.info("cmdbIpCollectRequest is {}", cmdbIpCollectRequest);
        CmdbIpCollectResult result = new CmdbIpCollectResult();
        try {
            cmdbIpCollectRequest.updatePageNo();
            setHisFlag4CmdbIp(cmdbIpCollectRequest);
            result.setData(buildIpPageCondition(cmdbIpCollectRequest));
            result.setTotalSize(cmdbIpCollectService.findPageCount(cmdbIpCollectRequest));
            result.setTopTotal(cmdbIpCollectService.findTopTotal(cmdbIpCollectRequest));
        } catch (Exception e) {
            log.error("findPageS 查询失败！", e);
        }
        return result;
    }

    @Override
    public ResultVo getResourceS() {
        ResultVo resultVo = new ResultVo(true, "查询成功！");
        try {
            List<Map> dataMap = cmdbIpCollectService.getResource();
            resultVo.setData(dataMap);
        } catch (Exception e) {
            log.error("获取资源池列表失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("获取资源池列表失败！");
        }
        return resultVo;
    }

    @Override
    public ResultVo getSourceS() {
        ResultVo resultVo = new ResultVo(true, "查询成功！");
        try {
            List<Map> dataMap = cmdbIpCollectService.getSource();
            resultVo.setData(dataMap);
        } catch (Exception e) {
            log.error("获取来源列表失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("获取来源列表失败！");
        }
        return resultVo;
    }

    @Override
    public void exportS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest, HttpServletResponse response) {
        log.info("cmdbIpCollectRequest is {}", cmdbIpCollectRequest);
        String[] headerList = {"检测时间", "IP", "MAC地址", "IP类型", "网关设备IP", "所属资源池"};
        String[] keyList = {"time", "ip", "mac", "iptype", "gateway", "resource"};
        String title = "存活IP列表";
        String fileName = title + ".xlsx";

        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // List<CmdbIpAddressPoolEntity> entityList = cmdbIpAddressPoolService.findData(cmdbIpCollectRequest);
            // for (CmdbIpAddressPoolEntity entity : entityList) {
            //     Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
            //     map.put("time", sdf.format(entity.getTime()));
            //     dataLists.add(map);
            // }

            os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition",
                    "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setContentType("application/vnd.ms-excel");
            // excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
            os.flush();
            log.info("导出/生成文件: {} 成功!", fileName);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public ResultVo<List<CmdbVipCollectEntity>> findListF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest) {
        ResultVo resultVo = new ResultVo(true, "查询成功！");
        try {
            cmdbVipCollectRequest.updatePageNo();
            cmdbVipCollectRequest.setPageNo(0);
            cmdbVipCollectRequest.setPageSize(0);
            resultVo.setData(cmdbVipCollectService.findData(cmdbVipCollectRequest));
        } catch (Exception e) {
            log.error("findList 查询失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg(String.format("findList 查询失败！%s", e.getMessage()));
        }
        return resultVo;
    }

    @Override
    public Result<CmdbVipCollectEntity> findPageF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest) {
        log.info("cmdbVipConllecRequest is {}", cmdbVipCollectRequest);
        Result result = new Result();
        try {
            cmdbVipCollectRequest.updatePageNo();
            result.setData(cmdbVipCollectService.findData(cmdbVipCollectRequest));
            result.setTotalSize(cmdbVipCollectService.findDataCount(cmdbVipCollectRequest));
        } catch (Exception e) {
            log.error("findPageF 查询失败！", e);
        }
        return result;
    }

    @Override
    public ResultVo getResourceF() {
        ResultVo resultVo = new ResultVo(true, "查询成功！");
        try {
            List<Map<String, String>> dataMap = cmdbVipCollectService.getResource();
            resultVo.setData(dataMap);
        } catch (Exception e) {
            log.error("getResourceF 获取资源池列表失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("getResourceF 获取资源池列表失败！");
        }
        return resultVo;
    }

    @Override
    public ResultVo getUserTypeF() {
        ResultVo resultVo = new ResultVo(true, "查询成功！");
        try {
            List<Map<String, String>> dataMap = cmdbVipCollectService.getUseType();
            resultVo.setData(dataMap);
        } catch (Exception e) {
            log.error("getUserTypeF 获取使用类型列表失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("getUserTypeF 获取使用类型列表失败！");
        }
        return resultVo;
    }

    @Override
    public void exportF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest, HttpServletResponse response) {
        log.info("cmdbVipCollectRequest is {}", cmdbVipCollectRequest);
        String[] headerList = {"检测时间", "虚拟IP", "当前绑定IP", "漂移IP列表", "虚拟IP使用类型", "所属资源池"};
        String[] keyList = {"time", "vip", "bindip", "iplist", "usetype", "resource"};
        String title = "虚拟IP列表";
        String fileName = title + ".xlsx";

        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            List<CmdbVipCollectEntity> entityList = cmdbVipCollectService.findData(cmdbVipCollectRequest);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (CmdbVipCollectEntity entity : entityList) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                map.put("time", sdf.format(entity.getTime()));
                dataLists.add(map);
            }

            os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition",
                    "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setContentType("application/vnd.ms-excel");
            // excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
            os.flush();
            log.info("导出/生成文件: {} 成功!", fileName);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * 保存请求日志
     *
     * @param request
     */
    private void saveLog(BaseInstanceRequest request) {
        executorService.execute(() -> instanceLogService.saveInstanceLog(request));
    }

    @Override
    public ResultVo updateIpInfoByTask() {
        ResultVo resultVo = new ResultVo(true, "执行成功！");
        try {
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("1"))) {
                cmdbIpCollectService.updateCmdbAssetAllIpInfo();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("2"))) {
                cmdbIpCollectService.updatePublicIpInfo();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("3"))) {
                cmdbIpCollectService.updateIpv6Info();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("4"))) {
                cmdbIpCollectService.updateIpBusinessByAsset();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("5"))) {
                vmwareFullSyncApiService.syncIpAddressPool();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("6"))) {
                vmwareFullSyncApiService.syncIpConfPool();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("7"))) {
                vmwareFullSyncApiService.syncIpArpPool();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("8"))) {
                cmdbIpCollectService.updateFirstSurvivalTime4IpInfo();
            }
            if ("Y".equals(cmdbIpCollectService.getIpUpdateConfig("9"))) {
                cmdbIpCollectService.buildAndSaveIpClashList4Now();
                cmdbIpCollectService.updateCmdbAssetSurvialInfo();
            }
        } catch (Exception e) {
            log.error("任务执行失败",e);
            resultVo.setSuccess(false);
            resultVo.setMsg("任务执行失败" + e.getMessage());
        }
        return resultVo;
    }

    /**
     * 构建存活IP的查询条件
     * @param cmdbIpCollectRequest 初始条件
     */
    private List<CmdbIpCollectResponse> buildIpPageCondition(CmdbIpCollectRequest cmdbIpCollectRequest) {
        List<CmdbIpCollectResponse> pageList;
        if("ip_remove_repetition".equals(cmdbIpCollectRequest.getSource())) {
            cmdbIpCollectRequest.setOneIpFlag("是");
            cmdbIpCollectRequest.setSource("");
            pageList = cmdbIpCollectService.findPage(cmdbIpCollectRequest);
        } else {
            pageList = cmdbIpCollectService.findPage(cmdbIpCollectRequest);
        }
        return buildIpPageList(pageList);
    }

    /**
     * 整合同一个IP的来源数据
     * @param pageList 存活IP数组
     */
    private List<CmdbIpCollectResponse> buildIpPageList(List<CmdbIpCollectResponse> pageList) {
        List<CmdbIpCollectResponse> retList = new ArrayList<>();
        Map<String, String> configMap = cmdbIpCollectService.buildConfig4Map("cmdbIpCollectSourceType");
        if (pageList.isEmpty()) {
            return retList;
        }
        pageList.forEach(map -> {
            String source = map.getSource();
            String[] split = source.split(",");
            if (split.length == 1) {
                map.setSource(configMap.get(split[0]));
            } else {
                Set<String> tempSet = new HashSet<>();
                for (String s : split) {
                    tempSet.add(configMap.get(s));
                }
                ArrayList<String> tempList = new ArrayList<>(tempSet);
                String join = String.join(";", tempList);
                map.setSource(join);
            }
            retList.add(map);
        });
        return retList;
    }

    /**
     * 如果查询日期不是当天的日期则查询历史记录表
     * @param cmdbIpCollectRequest 存活IP查询入参
     */
    private void setHisFlag4CmdbIp(CmdbIpCollectRequest cmdbIpCollectRequest) {
        Date startTime = cmdbIpCollectRequest.getStartTime();
        Date endTime = cmdbIpCollectRequest.getEndTime();
        if(null == startTime || null == endTime ) {
            cmdbIpCollectRequest.setHisFlag("1");
            return;
        }
        try {
            String startTimeStr = DateUtils.toDateH(startTime);
            String endTimeStr = DateUtils.toDateH(endTime);

            String nowDate = DateUtils.getDateTimeStr();
            String subDate = nowDate.substring(0,10);
            String nowDateStart = DateUtils.startDateH(subDate);
            String nowDateEnd = DateUtils.endDateH(subDate);
            if (nowDateStart.equals(startTimeStr) && nowDateEnd.equals(endTimeStr)) {
                return;
            }

            if (!DateUtils.isToDay(startTime)) {
                cmdbIpCollectRequest.setHisFlag("1");
                return;
            }
            if (!DateUtils.isToDay(endTime)) {
                cmdbIpCollectRequest.setHisFlag("1");
            }
        } catch (Exception e) {
            cmdbIpCollectRequest.setHisFlag("1");
            log.error("存活IP查询日期解析失败",e);
        }
    }
}
