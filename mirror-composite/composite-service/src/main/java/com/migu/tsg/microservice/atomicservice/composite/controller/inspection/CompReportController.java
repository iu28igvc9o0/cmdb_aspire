package com.migu.tsg.microservice.atomicservice.composite.controller.inspection;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.composite.service.inspection.ICompReportService;
import com.aspire.mirror.composite.service.inspection.payload.CompInspectionDetail;
import com.aspire.mirror.composite.service.inspection.payload.CompReportDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportItemDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompReportItemPageRequest;
import com.aspire.mirror.composite.service.inspection.payload.CompReportPageRequest;
import com.aspire.mirror.composite.service.inspection.payload.CompReportResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemDetailResponse;
import com.aspire.mirror.inspection.api.dto.ReportItemPageRequest;
import com.aspire.mirror.inspection.api.dto.ReportItemValue;
import com.aspire.mirror.inspection.api.dto.ReportPageRequest;
import com.aspire.mirror.inspection.api.dto.ReportTaskDetailResponse;
import com.aspire.mirror.inspection.api.dto.TaskObjectDetailResponse;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.template.api.dto.TemplateDetailResponse;
import com.aspire.mirror.template.api.dto.TriggersDetailResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.ReportDataServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.ReportItemServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.ReportServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.TaskObjectServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.TemplateServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.TriggersServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf.Excel2Pdf;
import com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf.ExcelObject;
import com.migu.tsg.microservice.atomicservice.composite.config.CommonSftpServerConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.SshUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.SshdHelper;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 巡检报告实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.inspection
 * 类名称:    CompReportController.java
 * 类描述:    巡检报告实现
 * 创建人:    JinSu
 * 创建时间:  2018/8/12 11:01
 * 版本:      v1.0
 */
@RestController
public class CompReportController extends CommonResourceController implements ICompReportService {

    private static final String REPORT_LOCAL_DIR_PREFIX = "report_upload_local_";
    private static final String REPORT_TRANSFER_DIR_PREFIX = "report_transfer_local_";
    private static final String REPORT_DIR = "/ops_report/";
    Logger logger = LoggerFactory.getLogger(CompReportController.class);

    @Autowired
    private ReportServiceClient reportService;

    @Autowired
    private ReportItemServiceClient reportItemService;

    @Autowired
    private TaskObjectServiceClient taskDeviceService;

    @Autowired
    private TemplateServiceClient templateService;

    @Autowired
    private ReportDataServiceClient reportDataService;

    @Autowired
    private TriggersServiceClient triggersService;

    @Autowired
    private CommonSftpServerConfig sftpConfig;

    @Autowired
    private SshdHelper sshdHelper;

    //    @Value("${deviceListUrl}")
//    private String devceListUrl;
    @Override
    @ResAction(action = "view", resType = "report")
    public CompReportResponse findByPrimaryKey(@PathVariable("report_id") String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            return null;
        }
//        logger.info("method[findByPrimaryKey] reportId is {}", reportId);
//        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        logger.info("[findByPrimaryKey]Username is {}.", authCtx.getUser().getUsername());
//        RbacResource rbacData = new RbacResource();
//        rbacData.setUuid(reportId);
//        rbacData.setResTypeAction(authCtx.getResAction());
//        logger.info("[findByPrimaryKey]The rbacResource is {}.", rbacData);
//        // 鉴权
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        //调原子层获取数据
//        CompReportResponse response = new CompReportResponse();
//
        CompReportResponse response = reportDataService.retriveReportData(reportId);

        return response;
    }

    @Override
    public CompReportResponse getBaseInfo(@PathVariable("report_id") String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            return null;
        }
        return reportDataService.getBaseInfo(reportId);
    }
//

    /**
     * @param reportPageRequest:巡检任务查询对象
     * @return
     */
    @Override
    @ResAction(action = "view", resType = "report")
    public PageResponse<CompReportDetailResponse> list(@RequestBody CompReportPageRequest reportPageRequest) {
        logger.info("method[list] request body is {}", reportPageRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[list]Username is {}.", authCtx.getUser().getUsername());
        RbacResource rbacData = new RbacResource();
        //查询鉴权
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[list]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        //调原子层获取数据
        ReportPageRequest pageRequest = jacksonBaseParse(ReportPageRequest.class, reportPageRequest);
        if (pageRequest.getPageNo() == 0) {
            pageRequest.setPageNo(1);
        }
        if (pageRequest.getPageSize() == 0) {
            pageRequest.setPageSize(1000);
        }
        PageResponse<ReportTaskDetailResponse> pageResponse = reportService.list(pageRequest);
        List<CompReportDetailResponse> detailResponseList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            for (ReportTaskDetailResponse detailResponse : pageResponse.getResult()) {

                CompReportDetailResponse compDetailResponse = jacksonBaseParse(CompReportDetailResponse.class,
                        detailResponse);
                if (StringUtils.isEmpty(compDetailResponse.getResult())) {


                    // 设置巡检结果
                    PageResponse<ReportItemDetailResponse> reportItemList = reportItemService.listByReportId
                            (detailResponse.getReportId());
                    if (!CollectionUtils.isEmpty(reportItemList.getResult())) {
                        String result = "扫描机器{0}台，扫描项共计{1}，结果项共计{2} 异常项<span style=\"color: red;font-weight: 800;" +
                                "\">{3}</span>，正常项<span " +
                                "style=\"color: green;font-weight: 800;\">{4}</span>，{5}项未取值，{6}项人工判断";

                        int exceptionNum = 0, healthNum = 0, noValueNum = 0, resultNum = 0, artificialJudgmentNum = 0;
                        Set<String> ipSet = Sets.newHashSet();
                        for (ReportItemDetailResponse reportItemDetailResponse : reportItemList.getResult()) {
                            if (!ipSet.contains(reportItemDetailResponse.getObjectId())) {
                                ipSet.add(reportItemDetailResponse.getObjectId());
                            }
                            if (CollectionUtils.isEmpty(reportItemDetailResponse.getReportItemValueList())) {
                                if (reportItemDetailResponse.getStatus() != null) {
                                    if (reportItemDetailResponse.getStatus().equals("0")) {
                                        healthNum++;
                                    } else if (reportItemDetailResponse.getStatus().equals("1")) {
                                        exceptionNum++;
                                    } else if (reportItemDetailResponse.getStatus().equals("2")) {
                                        noValueNum++;
                                    } else if (reportItemDetailResponse.getStatus().equals("3")) {
                                        artificialJudgmentNum++;
                                    }
                                }
                            } else {
                                for (ReportItemValue reportItemValue : reportItemDetailResponse.getReportItemValueList()) {
                                    if (reportItemValue.getResultStatus().equals("0")) {
                                        healthNum++;
                                    } else if (reportItemValue.getResultStatus().equals("1")) {
                                        exceptionNum++;
                                    } else if (reportItemValue.getResultStatus().equals("2")) {
                                        noValueNum++;
                                    } else if (reportItemValue.getResultStatus().equals("3")) {
                                        artificialJudgmentNum++;
                                    }
                                }
                            }

                        }
                        resultNum = healthNum + exceptionNum + noValueNum + artificialJudgmentNum;
                        result = MessageFormat.format(result, ipSet.size(), reportItemList.getResult().size(), resultNum,
                                exceptionNum, healthNum,
                                noValueNum, artificialJudgmentNum);
                        compDetailResponse.setResult(result);
                    }
                }
                //设置模板信息
                List<TaskObjectDetailResponse> deviceList = taskDeviceService.findByTaskId(detailResponse.getTaskId());
                List<String> templateIdList = Lists.newArrayList();
                for (TaskObjectDetailResponse taskDeviceDetailResponse : deviceList) {
                    if (!templateIdList.contains(taskDeviceDetailResponse.getTemplateId())) {
                        templateIdList.add(taskDeviceDetailResponse.getTemplateId());
                    }
                }
                if (!CollectionUtils.isEmpty(templateIdList)) {
                    List<TemplateDetailResponse> listTemplate = templateService.listByPrimaryKeyArrays(Joiner.on(",")
                            .join(templateIdList));
                    List<String> templateNameList = Lists.newArrayList();
                    for (TemplateDetailResponse templateDetailResponse : listTemplate) {
                        templateNameList.add(templateDetailResponse.getName());
                    }
                    compDetailResponse.setTemplateNames(Joiner.on(",").join(templateNameList));
                }
                detailResponseList.add(compDetailResponse);
            }
        }
        PageResponse<CompReportDetailResponse> response = new PageResponse<>();
        response.setCount(pageResponse.getCount());
        response.setResult(detailResponseList);
        return response;
    }

    @Override
    public PageResponse<CompReportItemDetailResponse> listReportItem(@RequestBody CompReportItemPageRequest
                                                                             reportItemPageRequest) {
        ReportItemPageRequest pageRequest = jacksonBaseParse(ReportItemPageRequest.class, reportItemPageRequest);
        if (pageRequest.getPageNo() == 0) {
            pageRequest.setPageNo(1);
        }
        if (pageRequest.getPageSize() == 0) {
            pageRequest.setPageSize(1000);
        }
        PageResponse<ReportItemDetailResponse> pageResponse = reportItemService.pageList(pageRequest);
        List<CompReportItemDetailResponse> compReportItemList = Lists.newArrayList();
        for (ReportItemDetailResponse reportItemDetailResponse : pageResponse.getResult()) {
            CompReportItemDetailResponse compReportItemDetailResponse = jacksonBaseParse(CompReportItemDetailResponse
                    .class, reportItemDetailResponse);
            compReportItemList.add(compReportItemDetailResponse);
        }

        PageResponse<CompReportItemDetailResponse> response = new PageResponse<>();
        response.setCount(pageResponse.getCount());
        response.setResult(compReportItemList);
        return response;
    }

    /**
     * 导出报表
     *
     * @param reportId 巡检报告主键
     */
    @Override
    @ResAction(action = "view", resType = "report")
    public void export(@PathVariable("report_id") final String reportId, @PathVariable("type") final String type,
                       HttpServletResponse response) throws Exception {

        CompReportResponse reportResponse = this.findByPrimaryKey(reportId);
        Map<String, Object> map = new HashMap();
        List<Map<String, String>> listAllMap = new ArrayList();
        if (!CollectionUtils.isEmpty(reportResponse.getReportItemList())) {
            for (CompInspectionDetail compInspectionDetail : reportResponse.getReportItemList()) {
                Map<String, String> inspectionDetailMap = jacksonBaseParse(Map.class, compInspectionDetail);
                listAllMap.add(inspectionDetailMap);
            }
        }
        List<Map<String, String>> listExceptionMap = new ArrayList();
        if (!CollectionUtils.isEmpty(reportResponse.getException())) {
            for (CompInspectionDetail compInspectionDetail : reportResponse.getException()) {
                Map<String, String> inspectionDetailMap = jacksonBaseParse(Map.class, compInspectionDetail);
                listExceptionMap.add(inspectionDetailMap);
            }
        }
        List<Map<String, String>> listNoResultMap = new ArrayList();
        if (!CollectionUtils.isEmpty(reportResponse.getNoResult())) {
            for (CompInspectionDetail compInspectionDetail : reportResponse.getNoResult()) {
                Map<String, String> inspectionDetailMap = jacksonBaseParse(Map.class, compInspectionDetail);
                listNoResultMap.add(inspectionDetailMap);
            }
        }
        map.put("all", listAllMap);
        map.put("exception", listExceptionMap);
        map.put("noResult", listNoResultMap);
        map.put("name", reportResponse.getName());
        map.put("finishTime", reportResponse.getFinishTime());
        if (type.equals("EXCEL")) {
            TemplateExportParams params = new TemplateExportParams(
                    new File(Constant.EXPORT_NEW_TEMPLATE_NAME).getCanonicalPath(), true);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            String fileName = "巡检报告" + DateUtil.format(new Date(), DateUtil.DATE_TIME_FORMAT) + ".xls";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + fileName);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            OutputStream output = response.getOutputStream();
            workbook.write(output);
            output.close();
        } else if (type.equals("PDF")) {
            TemplateExportParams params = new TemplateExportParams(
                    new File(Constant.EXPORT_TEMPLATE_NAME).getCanonicalPath(), true);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File fileDir = new File("export");
            FileOutputStream fos = new FileOutputStream(fileDir.getCanonicalPath() + "/inspectionTemp.xls");
            workbook.write(fos);
            fos.close();
            // luowenbo 2020-07-24 修改：硬编码文件分隔符缺陷
            String fiPathName = fileDir.getCanonicalPath() + File.separator + "inspectionTemp.xls";
            FileInputStream fis1 = new FileInputStream(new File(fiPathName));
            List<ExcelObject> objects = new ArrayList<ExcelObject>();
            objects.add(new ExcelObject("inspectionTemp.xls", fis1));

            String fileName = "巡检报告" + DateUtil.format(new Date(), DateUtil.DATE_TIME_FORMAT) + ".pdf";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-Type", "application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + fileName);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");

            Excel2Pdf pdf = new Excel2Pdf(objects, response.getOutputStream());
            pdf.convert();

            if (null != fos) {
                fos.close();
            }
        }

//        bos.flush();
//        bos.close();
//        output.close();
    }

    /**
     * 重新生成
     *
     * @param reportId
     */
    @Override
    public BaseResponse regenerate(@PathVariable("report_id") String reportId) {
        PageResponse<ReportItemDetailResponse> reportItemPage = reportItemService.listByReportId(reportId);
        Set<String> itemIdSet = reportItemPage.getResult().stream().map(ReportItemDetailResponse::getItemId).distinct
                ().collect(Collectors
                .toSet());
        List<TriggersDetailResponse> listTrigger = triggersService.listByItemIdArrays(Joiner.on(",").join(itemIdSet));
        Map<String, String> expressionMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(listTrigger)) {
            for (TriggersDetailResponse triggersDetailResponse : listTrigger) {
                expressionMap.put(triggersDetailResponse.getItemId(), triggersDetailResponse.getExpression());
            }
            reportService.regenerate(reportId, expressionMap);
        }

        return new BaseResponse();
    }

    @Override
    public GeneralResponse uploadReport(@RequestParam("report_id") String reportId, @RequestParam("file")
            MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path tempDir = Files.createTempDirectory(REPORT_LOCAL_DIR_PREFIX);
            Path tempFile = tempDir.resolve(fileName);
            file.transferTo(tempFile.toFile());

            Pair<SshUtil.SshResultWrap, String> uploadResult = uploadReportFile(tempFile);
            Files.deleteIfExists(tempFile);
            if (!uploadResult.getLeft().isSuccess()) {
                return new GeneralResponse(false, uploadResult.getLeft().getBizLog());
            }
            reportService.updateReportFilePath(reportId, uploadResult.getRight());
            return new GeneralResponse(true, null, uploadResult.getRight());
        } catch (Exception e) {
            logger.error("Exception when uploadYumLocalFile().", e);
            return new GeneralResponse(false, e.toString());
        }
    }

    @Override
    public void downloadReport(@RequestBody Map<String, String> downParam) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        sshdHelper.downloadFile(downParam, response, "application/vnd.ms-excel", "report_file_down");
//        String relativeFilePath = downParam.get("file_path");
//
//        try (OutputStream os = response.getOutputStream()) {
//            String fileName = Paths.get(relativeFilePath).getFileName().toFile().getName();
//            String fullDownFilePath = sftpConfig.getRootDirectory() + relativeFilePath;
//
//            SshUtil.SshdServer sftpServer = new SshUtil.SshdServer();
//            sftpServer.setIpAddress(sftpConfig.getIpAddress());
//            sftpServer.setPort(sftpConfig.getPort());
//            sftpServer.setLoginUser(sftpConfig.getLoginUser());
//            sftpServer.setLoginPass(sftpConfig.getLoginPass());
//
//            Path localDownDir = Files.createTempDirectory("report_file_down");
//            Pair<SshUtil.SshResultWrap, Path> downResult
//                    = SshUtil.sftpDownload(sftpServer, fullDownFilePath, localDownDir.toFile().getCanonicalPath());
//            SshUtil.SshResultWrap wrap = downResult.getLeft();
//            if (!wrap.isSuccess()) {
//                throw new RuntimeException(wrap.getBizLog());
//            }
//
//            response.setHeader("Content-Disposition",
//                    "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
//            response.setHeader("Connection", "close");
//            response.setCharacterEncoding("utf-8");
//            response.setHeader("Content-Type", "application/vnd.ms-excel");
//            os.write(Files.readAllBytes(downResult.getRight()));
//            os.flush();
//        } catch (Exception e) {
//            logger.error("DownReportFile failed!", e);
//        }
    }

    private Pair<SshUtil.SshResultWrap, String> uploadReportFile(Path localFile) {
//        SshUtil.SshdServer sftpServer = new SshUtil.SshdServer();
//        sftpServer.setIpAddress(sftpConfig.getIpAddress());
//        sftpServer.setPort(sftpConfig.getPort());
//        sftpServer.setLoginUser(sftpConfig.getLoginUser());
//        sftpServer.setLoginPass(sftpConfig.getLoginPass());
//
//        String subDir = REPORT_DIR + localFile.getParent().toFile().getName();
//        String remotePath = sftpConfig.getRootDirectory() + subDir;
//        SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);
//
//        Pair<SshUtil.SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, localFile, remotePath);


//        String relativePath = subDir + "/" + localFile.toFile().getName(); // 使用相对路径
        return sshdHelper.uploadFile(localFile, REPORT_DIR);
    }

    @PostConstruct
    private void getTemplatePath() throws IOException {
        File fileDir = new File("export");
//        System.out.println(fileDir.getCanonicalPath());
        if (!fileDir.exists()) {
            Files.createDirectory(Paths.get(fileDir.getCanonicalPath()));
        }

//        File outFile2 = new File(Constant.EXPORT_NEW_TEMPLATE_NAME);
        templateGenereate(Constant.EXPORT_TEMPLATE_NAME);
        templateGenereate(Constant.EXPORT_NEW_TEMPLATE_NAME);
    }

    private void templateGenereate(String path) throws IOException {
        File outFile = new File(path);
        logger.info("export template path is {}", outFile.getCanonicalPath());
        Files.deleteIfExists(Paths.get(outFile.getCanonicalPath()));
        Files.createFile(Paths.get(outFile.getCanonicalPath()));
        Resource res = new ClassPathResource(path);
        InputStream input = null;
        OutputStream out = null;
        try {
            input = res.getInputStream();
            out = new FileOutputStream(outFile);
            IOUtils.copy(input, out);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(out);
        }
    }

}
