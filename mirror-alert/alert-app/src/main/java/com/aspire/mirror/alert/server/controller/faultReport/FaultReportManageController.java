package com.aspire.mirror.alert.server.controller.faultReport;

import com.aspire.mirror.alert.api.dto.faultReport.FaultReportManageReq;
import com.aspire.mirror.alert.api.service.faultReport.FaultReportManageService;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.biz.faultReport.IFaultReportManageBiz;
import com.aspire.mirror.alert.server.dao.faultReport.po.FaultReportManage;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Slf4j
@RestController
public class FaultReportManageController implements FaultReportManageService {

    @Autowired
    private IFaultReportManageBiz faultReportManageBiz;
    @Autowired
    private FtpService ftpService;
    /**
     * 新增
     *
     * @param manage
     * @return
     */
    @Override
    public void insert(@RequestBody FaultReportManageReq manage) {
        faultReportManageBiz.insert(PayloadParseUtil.jacksonBaseParse(FaultReportManage.class,manage));
    }

    /**
     * 跟新
     *
     * @param manage
     * @return
     */
    @Override
    public void update(@RequestBody FaultReportManageReq manage) {
        faultReportManageBiz.update(PayloadParseUtil.jacksonBaseParse(FaultReportManage.class,manage));
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public FaultReportManageReq selectById(@PathVariable("id") Integer id) {
        FaultReportManage faultReportManage = faultReportManageBiz.selectById(id);
        if (faultReportManage == null) {
            return null;
        } else {
            return PayloadParseUtil.jacksonBaseParse(FaultReportManageReq.class, faultReportManage);
        }

    }

    /**
     * 导出附件
     *
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> exportAnnex(@RequestBody List<Integer> ids) {
        List<FaultReportManage> faultReportManageList = faultReportManageBiz.selectByIds(ids);
        List<String> pathList = Lists.newArrayList();
        for (FaultReportManage fault: faultReportManageList) {
            String reportEnclosure = fault.getReportEnclosure();
            if (!StringUtils.isEmpty(reportEnclosure)) {
                pathList.add(reportEnclosure);
            }
        }

        return ftpService.downloadBatch(pathList);
    }

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
    @Override
    public PageResponse<FaultReportManageReq> selectListByParams(@RequestParam(value = "faultReportUser",required = false) String faultReportUser,
                                                                 @RequestParam(value = "reportUser",required = false) String reportUser,
                                                                 @RequestParam(value = "faultHappenTimeFrom",required = false) String faultHappenTimeFrom,
                                                                 @RequestParam(value = "faultHappenTimeTo",required = false) String faultHappenTimeTo,
                                                                 @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageResponse<FaultReportManage> faultReportManagePageResponse = faultReportManageBiz.selectListByParams(faultReportUser, reportUser, faultHappenTimeFrom, faultHappenTimeTo, pageSize, pageNum);

        PageResponse<FaultReportManageReq> pageResponse = new PageResponse<>();
        pageResponse.setCount(faultReportManagePageResponse.getCount());
        pageResponse.setResult(PayloadParseUtil.jacksonBaseParse(FaultReportManageReq.class, faultReportManagePageResponse.getResult()));
        return pageResponse;
    }

    /**
     * 导出列表
     *
     * @param faultReportUser
     * @param reportUser
     * @param faultHappenTimeFrom
     * @param faultHappenTimeTo
     * @return
     */
    @Override
    public Map<String, Object> export(@RequestParam(value = "faultReportUser",required = false) String faultReportUser,
                                      @RequestParam(value = "reportUser",required = false) String reportUser,
                                      @RequestParam(value = "faultHappenTimeFrom",required = false) String faultHappenTimeFrom,
                                      @RequestParam(value = "faultHappenTimeTo",required = false) String faultHappenTimeTo) {
        Map<String, Object> map = new HashMap<>();
        Long time = System.currentTimeMillis();
        String[] headerList = {"故障ID","上报人姓名","上报人电话","上报人邮箱","上报人业务组","上报时间","故障发生时间",
                "上报及时","资源池","上报工单ID","上报内容简述","接收报障时间","故障报告名称","报告编写人","编写人电话","编写人邮箱",
                "编写人业务组","全网ID","工单ID","事件单下发时间","事件单要求完成时间","实际提交故障报告时间","故障上报及时性","平台恢复时间",
                "平台故障持续时长","业务恢复时间","业务恢复时长","影响业务系统数量","故障影响描述","故障分类","故障原因描述","厂家","事件性质","扣分",
                "故障报告附件","后续计划","后续计划完成说明","其他附注信息"};
        String[] keyList = {"faultId","faultReportUser","faultReporMobile","faultReportEmail","faultReportBizsys",
                "faultReportTime","faultHappenTime","faultReportTimely","faultIdcType","faultOrderId","faultContent",
                "faultRegtime","reportTitle","reportUser","reportMobile","reportEmail","reportBizsys","reportWnId",
                "reportOrderId","reportCreateTime","reportPlainFinish","reportFinishTime","reportTimely","reportPlatformRecoverytime",
                "reportPlatformRecoveryhours","reportBizsysRecoverytime","reportBizsysRecoveryhours","reportAffectBizsyss",
                "reportAffectDescribe","reportType","reportResson","reportProducer","reportNature","reportDeductPoints",
                "reportEnclosure","reportFollowPlan","reportFollowPlanExplain","reportRemark"};

        List<FaultReportManage> list = faultReportManageBiz.queryExports(faultReportUser, reportUser, faultHappenTimeFrom, faultHappenTimeTo);
        log.info("dataLists的值=======###￥￥￥" + list.size());

        String title = "告警导出列表";
        String fileName = title + ".xlsx";
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        SXSSFWorkbook book = new SXSSFWorkbook(128);
        book.setCompressTempFiles(true);
        try {
            eeu.exportExcel(book, 0, title, headerList, MapUtils.toMapList(list), keyList);
        } catch (Exception e) {
            log.error("generate excel failed,", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
            return map;
        }

        //主动释放资源
        list.clear();
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        log.info("-----2.create excel use-----: {} ms", (System.currentTimeMillis() - time));
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            map = ftpService.uploadtoFTP(fileName, in);
            ops.flush();
            log.info("-----3.upload excel to ftp use-----: {} ms", (System.currentTimeMillis() - time));
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
        }
        return map;
    }

    /**
     * 导入
     *
     * @param id
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> importFile(@PathVariable("id") Integer id, @RequestPart("file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        // 上传ftp
        InputStream in = null;
        try {
            in = file.getInputStream();
            map = ftpService.uploadtoFTPNew(file.getOriginalFilename(), in,"fault");
            log.info("-----upload excel to ftp");
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }

        // 更新数据库
        String code = MapUtils.getString(map, "code");
        if ("0000".equals(code)) {
            String path = MapUtils.getString(map, "path");
            FaultReportManage faultReportManage = new FaultReportManage();
            faultReportManage.setId(id);
            faultReportManage.setReportEnclosure(MapUtils.getString(map, "path"));
            faultReportManageBiz.update(faultReportManage);
        }
        return map;
    }
}
