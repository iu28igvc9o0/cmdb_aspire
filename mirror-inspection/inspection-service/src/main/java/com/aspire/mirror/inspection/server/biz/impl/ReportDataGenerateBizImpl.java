package com.aspire.mirror.inspection.server.biz.impl;

import static org.apache.commons.lang3.StringUtils.join;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.aspire.mirror.inspection.api.dto.model.InspectionCountQueryModel;
import com.aspire.mirror.inspection.api.dto.model.InspectionCountResp;
import com.aspire.mirror.inspection.api.dto.model.OpsTimeConsumeStatisticBase;
import com.aspire.mirror.inspection.api.dto.model.TaskDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.inspection.api.dto.model.ReportDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportDataWrap;
import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportItemDataWrap;
import com.aspire.mirror.inspection.api.dto.model.TaskObjectDTO;
import com.aspire.mirror.inspection.server.biz.ReportBiz;
import com.aspire.mirror.inspection.server.biz.ReportDataGenerateBiz;
import com.aspire.mirror.inspection.server.biz.ReportItemBiz;
import com.aspire.mirror.inspection.server.biz.TaskBiz;
import com.aspire.mirror.inspection.server.biz.cmdbadapt.CmdbAdaptFacade;
import com.aspire.mirror.inspection.server.biz.cmdbadapt.CommonCmdbDevice;
import com.aspire.mirror.inspection.server.biz.cmdbadapt.CommonUserInfo;
import com.aspire.mirror.inspection.server.clientservice.TemplateApiClient;
import com.aspire.mirror.inspection.server.clientservice.payload.ItemsDetailResponse;
import com.aspire.mirror.inspection.server.clientservice.payload.TriggersDetailResponse;
import com.aspire.mirror.inspection.server.dao.ReportDao;
import com.aspire.mirror.inspection.server.dao.TaskReceiverDao;
import com.aspire.mirror.inspection.server.dao.po.Report;
import com.aspire.mirror.inspection.server.helper.MailSendHelper;
import com.aspire.mirror.inspection.server.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import lombok.extern.slf4j.Slf4j;

/**
 * 报表数据生成及通知实现   <br/>
 * Project Name:inspection-service
 * File Name:ReportDataGenerateBizImpl.java
 * Package Name:com.aspire.mirror.inspection.server.biz.impl
 * ClassName: ReportDataGenerateBizImpl <br/>
 * date: 2018年8月28日 下午5:35:57 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Slf4j
@Service
public class ReportDataGenerateBizImpl implements ReportDataGenerateBiz {
    private static final String EXPORT_DIR = "export";
    private static final String REPORT_EXPORT_FILE_NAME = "reportDataExportTemplate.xls";
    @Autowired
    private TaskBiz taskBiz;
    @Autowired
    private ReportBiz reportBiz;
    @Autowired
    private ReportItemBiz reportItemBiz;
    @Autowired
    private TemplateApiClient templateApiClient;
    @Autowired
    private CmdbAdaptFacade cmdbFacade;
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private TaskReceiverDao taskReceiverDao;
    @Autowired
    private MailSendHelper mailHelper;

    /**
     * 把模版文件拷贝到jar包所在的export目录, 供后续poi导出时读取模版. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @throws IOException
     */
    @PostConstruct
    private void prepareExportTemplate() throws IOException {
        File fileDir = new File(EXPORT_DIR);
        if (!fileDir.exists()) {
            Files.createDirectory(Paths.get(fileDir.getCanonicalPath()));
        }
        File outTemplateFile = new File(EXPORT_DIR, REPORT_EXPORT_FILE_NAME);
        String templateFile = outTemplateFile.getCanonicalPath();
        if (!Files.exists(Paths.get(templateFile))) {
            Files.createFile(Paths.get(templateFile));
        }

        InputStream input = null;
        OutputStream out = null;
        Resource res = new ClassPathResource("export/reportDataExport.xls");
        try {
            input = res.getInputStream();
            out = new FileOutputStream(outTemplateFile);
            IOUtils.copy(input, out);
        } finally {
            if (input != null) {
                input.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDataWrap generateReportData(String reportId) {

        ReportDataWrap reportData = fetchBasicReportData(reportId);
        popupItemRelateInfo(reportData);
        popupDeviceItemInfo(reportData);
        popupBizItemInfo(reportData);
        return reportData;
    }

    /**
     * 获取报表基础数据. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param reportId
     * @return
     */
    private ReportDataWrap fetchBasicReportData(String reportId) {
        ReportDTO reportDto = reportBiz.selectByPrimaryKey(reportId);
        if (reportDto == null) {
            throw new RuntimeException(String.format("There is no report info with reportId: %s", reportId));
        }
        TaskDTO task = taskBiz.selectByPrimaryKey(reportDto.getTaskId());
        ReportDataWrap dataWrap = new ReportDataWrap();
        dataWrap.setReportId(reportId);
        dataWrap.setFinishTime(reportDto.getFinishTime());
        dataWrap.setName(reportDto.getName());
        dataWrap.setTaskName(task.getName());
        dataWrap.setCreateTime(reportDto.getCreateTime());
        dataWrap.setTaskType(task.getType());
        dataWrap.setDeviceNum(taskBiz.getDeviceNumByTaskId(task.getTaskId()));
        dataWrap.setItemNum(reportItemBiz.getItemNumByReportId(reportId));
        dataWrap.setTaskId(reportDto.getTaskId());
        List<ReportItemDTO> reportItemList = reportItemBiz.selectByReportId(reportId);
        dataWrap.setReportItemDataList(ReportItemDataWrap.parse(reportItemList));
        return dataWrap;
    }

    /**
     * 填充监控项、触发器相关信息. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param reportData
     */
    private void popupItemRelateInfo(ReportDataWrap reportData) {
//		List<String> itemIdList = new ArrayList<String>();
//		for (ReportItemDataWrap itemData : reportData.getReportItemDataList()) {
//			itemIdList.add(itemData.getItemId());
//		}
//		String joinedItemIds = join(itemIdList, ',');
//		List<ItemsDetailResponse> itemDetailList = templateApiClient.listItemsByIdArr(joinedItemIds);
//		for (ReportItemDataWrap itemData : reportData.getReportItemDataList()) {
//			for (ItemsDetailResponse itemDetail : itemDetailList) {
//				if (itemData.getItemId().equals(itemDetail.getItemId())) {
//					itemData.setItemName(itemDetail.getName());
//					itemData.setUnit(itemDetail.getUnits());
//					break;
//				}
//			}
//		}

//		List<TriggersDetailResponse> triggerList = templateApiClient.listTriggersByItemIdArr(joinedItemIds);
//		if (CollectionUtils.isEmpty(triggerList)) {
//			return;
//		}
//		for (ReportItemDataWrap itemData : reportData.getReportItemDataList()) {
//			for (TriggersDetailResponse trigger : triggerList) {
//				if (itemData.getItemId().equals(trigger.getItemId())) {
//					itemData.setExpression(trigger.getExpression().replace("::contains::", "包含"));
//					break;
//				}
//			}
//		}
    }

    /**
     * 填充设备类型 监控项  相关信息. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param reportData
     */
    private void popupDeviceItemInfo(ReportDataWrap reportData) {
        Set<String> cmdbIdSet = new HashSet<String>();
        for (ReportItemDataWrap itemData : reportData.getReportItemDataList()) {
            if (TaskObjectDTO.OBJECT_TYPE_DEVICE.equals(itemData.getObjectType())) {
                cmdbIdSet.add(itemData.getObjectId());
            }
        }
        if (CollectionUtils.isEmpty(cmdbIdSet)) {
            return;
        }

        List<CommonCmdbDevice> deviceDetailList = cmdbFacade.listCmdbDeviceByIds((join(cmdbIdSet, ',')));
        for (ReportItemDataWrap itemData : reportData.getReportItemDataList()) {
            for (CommonCmdbDevice device : deviceDetailList) {
                if (TaskObjectDTO.OBJECT_TYPE_DEVICE.equals(itemData.getObjectType())
                        && itemData.getObjectId().equals(device.getDeviceId())) {
                    itemData.setName(device.getName());
                    itemData.setDeviceType(device.getDeviceType());
                    itemData.setIp(device.getDeviceIp());
                    break;
                }
            }
        }
    }

    /**
     * TODO 填充业务监控项相关信息. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param reportData
     */
    private void popupBizItemInfo(ReportDataWrap reportData) {

    }

    @Override
    public void reportDataNotify(ReportDataWrap reportData) {
        File excelFile = generateReportDataExcel(reportData);
        log.info("Generate report excel file: " + excelFile.getPath());

        String attachNameSuffix = ".xls";
        String attachName = reportData.getName() + attachNameSuffix;
        List<String> receiverList = getMailReceiverList(reportData.getReportId());
        String[] receiverArr = receiverList.toArray(new String[receiverList.size()]);
        mailHelper.asycSendMail("【巡检报告】【" + reportData.getName() + "】", MessageFormat.format("名称为{0}巡检报告已生成，详情请参见附件！", reportData.getName()), Pair.of(attachName, excelFile), receiverArr);
    }

    @Override
    public ReportDataWrap getBaseInfo(String reportId) {
        return fetchBasicReportData(reportId);
    }

    @Override
    public InspectionCountResp getInspectionCount(InspectionCountQueryModel inspectionCountQueryModel) {
        return reportDao.selectInspectionCount(inspectionCountQueryModel);
    }

    @Override
    public OpsTimeConsumeStatisticBase getInspectionTimeStatistic(Map<String, Object> param) {
        return reportDao.getInspectionTimeStatistic(param);
    }

    /**
     * 根据报表数据生成excel内容文件. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param reportData
     * @return
     */
    private File generateReportDataExcel(ReportDataWrap reportData) {
        FileOutputStream fos = null;
        try {
            File template = new File(EXPORT_DIR, REPORT_EXPORT_FILE_NAME);
            TemplateExportParams params = new TemplateExportParams(template.getCanonicalPath());
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
            };
            Map<String, Object> map = JsonUtil.jacksonConvert(reportData, typeRef);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);

            File tempFile = File.createTempFile("reportData_", ".xls");
            fos = new FileOutputStream(tempFile);
            workbook.write(fos);
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Error when try to create inpection report excel file.", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> getMailReceiverList(String reportId) {
        Report report = reportDao.selectByPrimaryKey(reportId);
        List<String> userIdList = taskReceiverDao.selectByTaskId(report.getTaskId());
        List<CommonUserInfo> userInfoList = cmdbFacade.queryUserInfoListByIds(userIdList);
        List<String> userMailList = new ArrayList<String>();

        for (CommonUserInfo userInfo : userInfoList) {
            if (StringUtils.isBlank(userInfo.getEmail())) {
                continue;
            }
            userMailList.add(userInfo.getEmail());
        }
        return userMailList;
    }
}
