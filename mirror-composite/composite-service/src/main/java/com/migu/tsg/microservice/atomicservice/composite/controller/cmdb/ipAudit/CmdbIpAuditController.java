package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.ipAudit;

import com.aspire.mirror.composite.service.cmdb.ipAudit.IIpAuditAPI;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipAudit.payload.*;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipAudit.IpAuditClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JavaBeanUtil;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huanggongrui
 * @Datetime: 2020/5/23 16:57
 */
@Slf4j
@RestController
public class CmdbIpAuditController implements IIpAuditAPI {

    @Autowired
    private IpAuditClient ipAuditClient;

    @Override
    public IpAuditPageBean<SurvivingUnrecordIntranetIpResp> getSurvivingUnrecordIntranetIpList(@RequestBody SurvivingUnrecordIntranetIpReq req) {
        return ipAuditClient.getSurvivingUnrecordIntranetIpList(req);
    }

    @Override
    public void exportSurvivingUnrecordIntranetIpList(@RequestBody SurvivingUnrecordIntranetIpReq param, HttpServletResponse response) {
        log.info("SurvivingUnrecordIntranetIpReq is {}", param);
        String[] headerList = {"检测时间", "IP", "使用状态", "网段类型", "网段子类",
                "地址用途", "分配一级业务", "分配状态", "分配时间", "分配人", "申请人",
                "网关设备IP", "所属资源池", "处理状态", "原因说明", "操作人", "操作时间", "是否已通知", "工单号"};
        String[] keyList = {"checkTime", "ip", "useStatus", "networkSegmentType", "networkSegmentSubType", "ipUse",
                "firstBusinessSystem", "assignStatus", "assignTime", "assignUser", "requestPerson",
                "deviceIp", "dc", "processingStatusDesc", "reason", "operator", "operatingTime", "isNotify", "orderNum"};
        String title = "存活IP未录入CMDB";
        String fileName = title + ".xlsx";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            IpAuditPageBean<SurvivingUnrecordIntranetIpResp> pageList = ipAuditClient.getSurvivingUnrecordIntranetIpList(param);

            List<SurvivingUnrecordIntranetIpResp> list = pageList.getResult();
            for (SurvivingUnrecordIntranetIpResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                dataLists.add(map);
            }
            os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
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
    public ResultVo updateSurvivingUnrecordIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updateSurvivingUnrecordIntranetIpProcessStatus(request);
    }

    @Override
    public IpAuditPageBean<RecordedUnsurvivingIntranetIpResp> getRecordedUnsurvivingIntranetIpList(@RequestBody RecordedUnsurvivingIntranetIpReq req) {
        return ipAuditClient.getRecordedUnsurvivingIntranetIpList(req);
    }

    @Override
    public void exportRecordedUnsurvivingIntranetIpList(@RequestBody RecordedUnsurvivingIntranetIpReq param, HttpServletResponse response) {
        log.info("recordedUnsurvivingIntranetIpReq is {}", param);
        String[] headerList = {"检测时间", "IP", "IP类型", "资产管理IP", "主备",
                "所属资源池", "是否资源池管理", "所属独立业务", "独立业务子模块", "存活状态", "最近存活时间",
                "未存活时长(小时)", "处理状态", "原因说明", "操作人", "操作时间"};
        String[] keyList = {"checkTime", "ip", "ipType", "deviceIp", "hostBackup", "dc",
                "mgrByPool", "businessLevel1", "businessLevel2", "surviveStatus", "lastSurviveTime",
                "unsurvivedDuration", "processingStatusDesc", "reason", "operator", "operatingTime"};
        String title = "已录CMDB未存活IP";
        String fileName = title + ".xlsx";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            IpAuditPageBean<RecordedUnsurvivingIntranetIpResp> pageList = ipAuditClient.getRecordedUnsurvivingIntranetIpList(param);
            List<RecordedUnsurvivingIntranetIpResp> list = pageList.getResult();
            for (RecordedUnsurvivingIntranetIpResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
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

    @Override
    public ResultVo updateRecordedUnsurvivingIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updateRecordedUnsurvivingIntranetIpProcessStatus(request);
    }

    @Override
    public IpAuditPageBean<AssignedUnsurvivingIntranetIpResp> getAssignedUnsurvivingIntranetIpList(@RequestBody AssignedUnsurvivingIntranetIpReq req) {
        return ipAuditClient.getAssignedUnsurvivingIntranetIpList(req);
    }

    @Override
    public void exportAssignedUnsurvivingIntranetIpList(@RequestBody AssignedUnsurvivingIntranetIpReq param, HttpServletResponse response) {
        log.info("assignedUnsurvivingIntranetIpReq is {}", param);
        String[] headerList = {"检测时间", "IP", "所属资源池", "网段类型", "网段子类",
                "地址用途", "分配一级业务", "分配独立业务", "分配状态", "分配时间", "分配人", "申请人", "申请时间",
                "存活状态", "最近存活时间", "未存活时长(小时)", "处理状态", "原因说明", "操作人", "操作时间"};
        String[] keyList = {"checkTime", "ip", "dc", "networkSegmentType", "networkSegmentSubType", "ipUse",
                "firstBusinessSystem", "aloneBusinessSystem", "assignStatus", "assignTime", "assignUser", "requestPerson",
                "requestTime", "surviveStatus", "lastSurviveTime", "unsurvivedDuration", "processingStatusDesc",
                "reason", "operator", "operatingTime"};
        String title = "已分配未存活IP";
        String fileName = title + ".xlsx";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            IpAuditPageBean<AssignedUnsurvivingIntranetIpResp> pageList = ipAuditClient.getAssignedUnsurvivingIntranetIpList(param);
            List<AssignedUnsurvivingIntranetIpResp> list = pageList.getResult();
            if (!CollectionUtils.isEmpty(list)) {
                for (AssignedUnsurvivingIntranetIpResp entity : list) {
                    Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                    dataLists.add(map);
                }
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

    @Override
    public ResultVo updateAssignedUnsurvivingIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updateAssignedUnsurvivingIntranetIpProcessStatus(request);
    }

    @Override
    public IpAuditPageBean<SurvivingUnplannedIntranetIpResp> getSurvivingUnplannedIntranetIpList(@RequestBody SurvivingUnplannedIntranetIpReq req) {
        return ipAuditClient.getSurvivingUnplannedIntranetIpList(req);
    }

    @Override
    public void exportSurvivingUnplannedIntranetIpList(@RequestBody SurvivingUnplannedIntranetIpReq param, HttpServletResponse response) {
        log.info("survivingUnplannedIntranetIpReq is {}", param);
        String[] headerList = {"检测时间", "IP", "网络设备", "所属资源池", "是否已通知",
                "处理状态", "原因说明", "操作人", "操作时间", "工单号"};
        String[] keyList = {"checkTime", "ip", "deviceIp", "dc", "isNotify", "processingStatusDesc",
                "reason", "operator", "operatingTime", "orderNum"};
        String title = "存活未规划IP";
        String fileName = title + ".xlsx";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            IpAuditPageBean<SurvivingUnplannedIntranetIpResp> pageList = ipAuditClient.getSurvivingUnplannedIntranetIpList(param);
            List<SurvivingUnplannedIntranetIpResp> list = pageList.getResult();
            if (!CollectionUtils.isEmpty(list)) {
                for (SurvivingUnplannedIntranetIpResp entity : list) {
                    Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                    dataLists.add(map);
                }
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

    @Override
    public ResultVo updateSurvivingUnplannedIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updateSurvivingUnplannedIntranetIpProcessStatus(request);
    }

    @Override
    public IpAuditPageBean<SurvivingUnassignIntranetIpResp> getSurvivingUnassignIntranetIpList(@RequestBody SurvivingUnassignIntranetIpReq req) {
        return ipAuditClient.getSurvivingUnassignIntranetIpList(req);
    }

    @Override
    public void exportSurvivingUnassignIntranetIpList(@RequestBody SurvivingUnassignIntranetIpReq param, HttpServletResponse response) {
        log.info("survivingUnassignIntranetIpReq is {}", param);
        String[] headerList = {"检测时间", "IP", "VLAN号", "所属资源池", "是否资源池管理", "网段类型", "网段子类",
                "安全域", "地址用途", "分配一级业务", "分配独立业务", "是否录入CMDB", "使用方独立业务",
                "处理状态", "原因说明", "操作人", "操作时间"};
        String[] keyList = {"checkTime", "ip", "vlanNumber", "dc", "isPool", "networkSegmentType", "networkSegmentSubType",
                "safeRegion", "ipUse", "firstBusinessSystem", "aloneBusinessSystem", "isCmdbManager",
                "onlineBusiness", "processingStatusDesc", "reason", "operator", "operatingTime"};
        String title = "存活未分配IP";
        String fileName = title + ".xlsx";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            IpAuditPageBean<SurvivingUnassignIntranetIpResp> pageList = ipAuditClient.getSurvivingUnassignIntranetIpList(param);
            List<SurvivingUnassignIntranetIpResp> list = pageList.getResult();
            if (!CollectionUtils.isEmpty(list)) {
                for (SurvivingUnassignIntranetIpResp entity : list) {
                    Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                    dataLists.add(map);
                }
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

    @Override
    public ResultVo updateSurvivingUnassignIntranetIpProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updateSurvivingUnassignIntranetIpProcessStatus(request);
    }

    @Override
    public IpAuditPageBean<RegistrationExpiredIpResp> getRegistrationExpiredIpList(@RequestBody RegistrationExpiredIpReq req) {
        return ipAuditClient.getRegistrationExpiredIpList(req);
    }

    @Override
    public void exportRegistrationExpiredIpList(@RequestBody RegistrationExpiredIpReq param, HttpServletResponse response) {
        log.info("registrationExpiredIpReq is {}", param);
        String[] headerList = {"检测时间", "IP", "所属资源池", "网段类型", "网段子类",
                "安全域", "地址用途", "分配一级业务", "分配独立业务", "是否录入CMDB", "使用方独立业务", "申请人", "申请时间",
                "有效期", "处理状态", "原因说明", "操作人", "操作时间"};
        String[] keyList = {"checkTime", "ip", "dc", "networkSegmentType", "networkSegmentSubType", "safeRegion",
                "ipUse", "firstBusinessSystem", "aloneBusinessSystem", "isCmdbManager", "onlineBusiness", "requestPerson",
                "requestTime", "expirationDate", "processingStatusDesc", "reason", "operator", "operatingTime"};
        String title = "登记已过期IP";
        String fileName = title + ".xlsx";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            IpAuditPageBean<RegistrationExpiredIpResp> pageList = ipAuditClient.getRegistrationExpiredIpList(param);
            List<RegistrationExpiredIpResp> list = pageList.getResult();
            if (!CollectionUtils.isEmpty(list)) {
                for (RegistrationExpiredIpResp entity : list) {
                    Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                    dataLists.add(map);
                }
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

    @Override
    public ResultVo updateRegistrationExpiredIpProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updateRegistrationExpiredIpProcessStatus(request);
    }

    //=========================公网IP=============================
    @Override
    public IpAuditPageBean<IpAuditCommonResp> getUnPlanPublicIpList(@RequestBody IpAuditCommonReq req) {
        return ipAuditClient.getUnPlanPublicIpList(req);
    }

    @Override
    public void exportUnPlanPublicIpList(@RequestBody IpAuditCommonReq param, HttpServletResponse response) {
        String[] headerList = {"检测时间", "IP","网络设备IP","所属资源池","是否已通知","处理状态","原因说明","操作人","操作时间","工单号"};
        String[] keyList = {"checkTime","ip","deviceIp","dc","isNotify","processingStatusDesc","reason","operator","operatingTime","orderNum"};
        String title = "存活未规划公网IP";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            List<IpAuditCommonResp> list = ipAuditClient.getUnPlanPublicIpList(param).getResult();
            for (IpAuditCommonResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                dataLists.add(map);
            }
            os = response.getOutputStream();// 取得输出流
            exportExcel(os,response,title,headerList,keyList,dataLists);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public IpAuditPageBean<UnassignPublicIpResp> getUnAssignPublicIpList(@RequestBody UnassignPublicIpReq req) {
        return ipAuditClient.getUnAssignPublicIpList(req);
    }

    @Override
    public void exportUnAssignPublicIpList(@RequestBody UnassignPublicIpReq param, HttpServletResponse response) {
        String[] headerList = {"检测时间", "IP","网段名称","所属资源池","是否资源池管理","线路类型","地址用途","分配一级业务","分配独立业务","处理状态","原因说明","操作人","操作时间"};
        String[] keyList = {"checkTime","ip","networkName","belongIdc","idcManageFlag","circuitType","addressUse","businessName1","businessName2","processingStatusDesc","reason","operator","operatingTime"};
        String title = "存活未分配公网IP";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            List<UnassignPublicIpResp> list = ipAuditClient.getUnAssignPublicIpList(param).getResult();
            for (IpAuditCommonResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                dataLists.add(map);
            }
            os = response.getOutputStream();// 取得输出流
            exportExcel(os,response,title,headerList,keyList,dataLists);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public ResultVo updatePublicIpProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updatePublicIpProcessStatus(request);
    }

    @Override
    public IpAuditPageBean<RegistrationExpiredPublicIpResp> getRegistrationExpiredPublicIpList(@RequestBody RegistrationExpiredPublicIpReq req) {
        return ipAuditClient.getRegistrationExpiredPublicIpList(req);
    }

    @Override
    public void exportRegistrationExpiredPublicIpList(@RequestBody RegistrationExpiredPublicIpReq param, HttpServletResponse response) {
        String[] headerList = {"检测时间", "IP","网段名称","所属资源池","是否资源池管理","线路类型","分配一级业务","分配独立业务","处理状态","申请人","申请时间","有效期","原因说明","操作人","操作时间"};
        String[] keyList = {"checkTime","ip","networkName","belongIdc","idcManageFlag","circuitType","businessName1","businessName2","processingStatusDesc","requestPerson","requestTime","expirationDate","reason","operator","operatingTime"};
        String title = "登记已过期公网IP";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            List<RegistrationExpiredPublicIpResp> list = ipAuditClient.getRegistrationExpiredPublicIpList(param).getResult();
            for (IpAuditCommonResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                dataLists.add(map);
            }
            os = response.getOutputStream();// 取得输出流
            exportExcel(os,response,title,headerList,keyList,dataLists);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    //=========================IPv6===============================
    @Override
    public IpAuditPageBean<IpAuditCommonResp> getUnPlanIPv6List(@RequestBody IpAuditCommonReq req) {
        return ipAuditClient.getUnPlanIPv6List(req);
    }

    @Override
    public void exportUnPlanIPv6List(@RequestBody IpAuditCommonReq param, HttpServletResponse response) {
        String[] headerList = {"检测时间", "IP","网络设备IP","所属资源池","是否已通知","处理状态","原因说明","操作人","操作时间","工单号"};
        String[] keyList = {"checkTime","ip","deviceIp","dc","isNotify","processingStatusDesc","reason","operator","operatingTime","orderNum"};
        String title = "存活未规划IPv6";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            List<IpAuditCommonResp> list = ipAuditClient.getUnPlanIPv6List(param).getResult();
            for (IpAuditCommonResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                dataLists.add(map);
            }
            os = response.getOutputStream();// 取得输出流
            exportExcel(os,response,title,headerList,keyList,dataLists);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public IpAuditPageBean<UnassignIpV6Resp> getUnAssignIPv6List(@RequestBody UnassignIpV6Req req) {
        return ipAuditClient.getUnAssignIPv6List(req);
    }

    @Override
    public void exportUnAssignIPv6List(@RequestBody UnassignIpV6Req param, HttpServletResponse response) {
        String[] headerList = {"检测时间", "IP","网段名称","所属资源池","是否资源池管理","网段类型","网段子类","地址用途","分配一级业务","分配独立业务","是否录入CMDB","使用方独立业务","处理状态","原因说明","操作人","操作时间"};
        String[] keyList = {"checkTime","ip","networkName","belongIdc","idcManageFlag","networkType","networkSub","addressUse","businessName1","businessName2","cmdbManageFlag","businessNameUse","processingStatusDesc","reason","operator","operatingTime"};
        String title = "存活分配IPv6";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            List<UnassignIpV6Resp> list = ipAuditClient.getUnAssignIPv6List(param).getResult();
            for (IpAuditCommonResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                dataLists.add(map);
            }
            os = response.getOutputStream();// 取得输出流
            exportExcel(os,response,title,headerList,keyList,dataLists);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public ResultVo updateIPv6ProcessStatus(@RequestBody IpAuditUpdateRequest request) {
        return ipAuditClient.updateIPv6ProcessStatus(request);
    }

    @Override
    public IpAuditPageBean<RegistrationExpiredIpv6Resp> getRegistrationExpiredIpv6List(@RequestBody RegistrationExpiredIpv6Req req) {
        return ipAuditClient.getRegistrationExpiredIpv6List(req);
    }

    @Override
    public void exportRegistrationExpiredIpv6List(@RequestBody RegistrationExpiredIpv6Req param, HttpServletResponse response) {
        String[] headerList = {"检测时间", "IP","网段名称","所属资源池","是否资源池管理","网段类型","网段子类","地址用途","分配一级业务","分配独立业务","是否录入CMDB","使用方独立业务","处理状态","申请人","申请时间","有效期","原因说明","操作人","操作时间"};
        String[] keyList = {"checkTime","ip","networkName","belongIdc","idcManageFlag","networkType","networkSub","addressUse","businessName1","businessName2","cmdbManageFlag","businessNameUse","processingStatusDesc","requestPerson","requestTime","expirationDate","reason","operator","operatingTime"};
        String title = "登记已过期IPv6";
        OutputStream os = null;
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        try {
            List<RegistrationExpiredIpv6Resp> list = ipAuditClient.getRegistrationExpiredIpv6List(param).getResult();
            for (IpAuditCommonResp entity : list) {
                Map<String, Object> map = JavaBeanUtil.convertBeanToMap(entity);
                dataLists.add(map);
            }
            os = response.getOutputStream();// 取得输出流
            exportExcel(os,response,title,headerList,keyList,dataLists);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override
    public ResultVo getResource(String type, String pid) {
        return ipAuditClient.getResource(type, pid);
    }

    @Override
    public ResultVo updateIpRepositoryInfo(@RequestBody IpAuditUpdateRequest param) {
        return ipAuditClient.updateIpRepositoryInfo(param);
    }

    private void exportExcel(OutputStream os, HttpServletResponse response, String title, String[] headerList, String[] keyList, List<Map<String, Object>> dataLists) throws Exception{
        String fileName = title + ".xlsx";
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
    }
}
