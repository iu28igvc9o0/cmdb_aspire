package com.aspire.mirror.alert.server.biz.leakScan.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.aspire.mirror.alert.server.vo.leakScan.LeakScanSummaryVo;
import com.aspire.mirror.alert.server.vo.leakScan.SecurityLeakScanRecordVo;
import com.aspire.mirror.alert.server.vo.leakScan.SecurityLeakScanReportVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.aspire.mirror.alert.server.biz.leakScan.SecurityLeakScanBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbAlertRestfulClient;
import com.aspire.mirror.alert.server.clientservice.CmdbInstanceClient;
import com.aspire.mirror.alert.server.dao.leakScan.SecurityLeakScanRecordDao;
import com.aspire.mirror.alert.server.dao.leakScan.SecurityLeakScanReportDao;
import com.aspire.mirror.alert.server.dao.leakScan.SecurityLeakScanReportFileDao;
import com.aspire.mirror.alert.server.dao.leakScan.po.SecurityLeakScanRecord;
import com.aspire.mirror.alert.server.dao.leakScan.po.SecurityLeakScanReport;
import com.aspire.mirror.alert.server.dao.leakScan.po.SecurityLeakScanReportFile;
import com.aspire.mirror.alert.server.util.AntZipUtil;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.ExcelReaderUtil;
import com.aspire.mirror.alert.server.util.ExcelSheetPO;
import com.aspire.mirror.alert.server.util.Md5Utils;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.collect.Lists;

@Service
public class SecurityLeakScanBizImpl implements SecurityLeakScanBiz {

    private static final Logger logger = LoggerFactory.getLogger(SecurityLeakScanBizImpl.class);
    private static final String SYSTEM_PATH_SEPRATOR = System.getProperty("file.separator");
    @Value("${sls.local.download}")
    private String SLS_FILE_DOWNLOAD_BASE_PATH;
    private static final int columnCount = 5;
    private static final int startRow = 1;
    @Autowired
    private SecurityLeakScanRecordDao securityLeakScanRecordDao;
    @Autowired
    private SecurityLeakScanReportDao securityLeakScanReportDao;
    @Autowired
    private SecurityLeakScanReportFileDao securityLeakScanReportFileDao;
    @Autowired
    private CmdbAlertRestfulClient cmdbAlertRestfulClient;
    @Autowired
    private CmdbInstanceClient cmdbInstanceClient;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public String persistScanRecords(File zipFile, String dateStr, String ftpFilePath, String fileName)
            throws IOException, ParseException {
        File localFile = null;
        String bizLine = fileName.substring(0, fileName.indexOf("."));
        String zipFileDir = zipFile.getParentFile().getPath();
        if (AntZipUtil.unZip(zipFile.getAbsolutePath(), zipFileDir)) {
            String localExcelPath = zipFileDir + SYSTEM_PATH_SEPRATOR + bizLine;
            localFile = findFileByBizLine(new File(localExcelPath), bizLine);
        }
        if (localFile == null) {
            logger.error("压缩文件内不包含业务线:{} 对应的Excel文件!", bizLine);
            return null;
        }
        List<ExcelSheetPO> sheetPoList = ExcelReaderUtil.readExcel(localFile, null, columnCount);
        if (CollectionUtils.isEmpty(sheetPoList)) {
            logger.error("FTP 文件: {} 未解析到数据记录!", ftpFilePath);
            return null;
        }
        String fileDownloadURL = SLS_FILE_DOWNLOAD_BASE_PATH + "/" + dateStr + "/" + fileName;
        String scanId = UUID.randomUUID().toString();

        Map<String, String> departmentInfoByBizSystem = cmdbAlertRestfulClient.getDepartmentInfoByBizSystem(bizLine);
        SecurityLeakScanRecord record = new SecurityLeakScanRecord();
        record.setId(scanId);
        record.setDepartment1(StringUtils.isEmpty(departmentInfoByBizSystem.get("department1")) ? "" : departmentInfoByBizSystem.get("department1"));
        record.setDepartment2(StringUtils.isEmpty(departmentInfoByBizSystem.get("department2")) ? "" : departmentInfoByBizSystem.get("department2"));
        record.setBizLine(bizLine);
        record.setReportFileName(fileName);
        record.setReportFileUrl(fileDownloadURL);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        record.setScanDate(sdf.parse(dateStr));
        record.setRepairStat(0);
        securityLeakScanRecordDao.insert(record);

        for (ExcelSheetPO sheetPO : sheetPoList) {
            List<List<Object>> dataList = sheetPO.getDataList();
            List<SecurityLeakScanReport> leakScanReportList = new ArrayList<>();
            if (CollectionUtils.isEmpty(dataList) || dataList.size() <= startRow) {
                logger.error("Excel: {}, Sheet: {} 不存在有效数据 !", fileName, sheetPO.getSheetName());
                continue;
            }
            for (int i = startRow; i < dataList.size(); i++) {
                List<Object> row  = dataList.get(i);
                if (CollectionUtils.isEmpty(row) || row.size() < columnCount) {
                    logger.error("Excel 文件 {} 列数不符合要求，预期列数: {}, 实际列数: {}",
                            ftpFilePath, columnCount, row.size());
                    continue;
                }
                String reportId = UUID.randomUUID().toString();
                String ip = String.valueOf(row.get(0));
                Integer highLeaks = Double.valueOf(String.valueOf(row.get(1))).intValue();
                Integer mediumLeaks = Double.valueOf(String.valueOf(row.get(2))).intValue();
                Integer lowLeaks = Double.valueOf(String.valueOf(row.get(3))).intValue();
                Double riskVal = Double.valueOf(String.valueOf(row.get(4)));

                SecurityLeakScanReport report = new SecurityLeakScanReport();
                report.setId(reportId);
                report.setScanId(scanId);
                report.setIp(ip);
                report.setReportPath(SLS_FILE_DOWNLOAD_BASE_PATH + "/" + dateStr + "/" + bizLine + "/host/" + ip + ".html");
                report.setHighLeaks(highLeaks);
                report.setMediumLeaks(mediumLeaks);
                report.setLowLeaks(lowLeaks);
                report.setRiskVal(riskVal);
                leakScanReportList.add(report);
            }
            if (CollectionUtils.isNotEmpty(leakScanReportList)) {
                securityLeakScanReportDao.batchInsert(leakScanReportList);
            }
        }
        // 记录文件解析
        SecurityLeakScanReportFile reportFile = new SecurityLeakScanReportFile();
        reportFile.setScanId(scanId);
        reportFile.setFileName(Md5Utils.generateCheckCode(ftpFilePath));
        reportFile.setFtpPath(ftpFilePath);
        securityLeakScanReportFileDao.insert(reportFile);
        return scanId;
    }

    @Override
    public void fillScanRecordBpmId(String scanId, String bpmId) {
        securityLeakScanRecordDao.updateBpmId(scanId, bpmId);
    }

    @Override
    public void fillScanRecordBpmFileId(String scanId, String bpmFileId) {
        securityLeakScanRecordDao.updateBpmFileId(scanId, bpmFileId);
    }

    @Override
    public void fillScanReportHtmlPath(String scanId, String dateStr, String bizLine) {
        String prefix = SLS_FILE_DOWNLOAD_BASE_PATH + "/" + dateStr + "/" + bizLine + "/host/";
        securityLeakScanReportDao.batchUpateReportHtmlPath(scanId, prefix);
    }

    @Override
    public void setBpmReapirStat(String bpmId, int stat) {
        securityLeakScanRecordDao.updateBpmRepairStat(bpmId, stat);
    }

    @Override
    public List<SecurityLeakScanReportFile> getFileByFtpPath(String ftpFilePath) {
        return securityLeakScanReportFileDao.getFileByFtpPath(ftpFilePath);
    }

    @Override
    public SecurityLeakScanRecordVo getSecurityLeakScanRecordById(String id) {
        SecurityLeakScanRecord record = securityLeakScanRecordDao.selectById(id);
        SecurityLeakScanRecordVo dto = new SecurityLeakScanRecordVo();
        BeanUtils.copyProperties(record, dto);
        return dto;
    }

    @Override
    public List<SecurityLeakScanRecordVo> getSecurityLeakScanRecordByDateAndFileName(String dateStr, String fileName) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.SHORT_DATE_PATTERN, Locale.CHINA);
        Date date = format.parse(dateStr);
        List<SecurityLeakScanRecord> recordList = securityLeakScanRecordDao.selectByDateAndFileName(date, fileName);
        List<SecurityLeakScanRecordVo> dtoList = Lists.newArrayList();
        for (SecurityLeakScanRecord record : recordList) {
            SecurityLeakScanRecordVo dto = new SecurityLeakScanRecordVo();
            BeanUtils.copyProperties(record, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private File findFileByBizLine(File dir, String bizLine) {
        if (!dir.exists()) {
            throw new IllegalArgumentException("目录："+ dir +"不存在.");
        }
        if(!dir.isDirectory()){
            throw new IllegalArgumentException(dir.getAbsolutePath() + "不是目录。");
        }
        //如果要遍历子目录下的内容就需要构造File对象做递归操作，File提供了直接返回File对象的API
        File[] files=dir.listFiles();
        if(files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findFileByBizLine(file, bizLine);
                } else {
                    String fileName = file.getName();
                    if (fileName.startsWith(bizLine)) {
                        Locale.setDefault(Locale.CHINA);
                        if (fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx")) {
                            return file;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public PageResponse<LeakScanSummaryVo> summaryList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = securityLeakScanRecordDao.summaryListCount(page);
        PageResponse<LeakScanSummaryVo> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<SecurityLeakScanRecord> recordList = securityLeakScanRecordDao.summaryList(page);
            List<LeakScanSummaryVo> dtoList = Lists.newArrayList();
            for (SecurityLeakScanRecord scanRecord : recordList) {
                LeakScanSummaryVo dto = new LeakScanSummaryVo();
                BeanUtils.copyProperties(scanRecord, dto);
                dtoList.add(dto);
            }
            pageResponse.setResult(dtoList);
        }
        return pageResponse;
    }

    @Override
    public List<LeakScanSummaryVo> exportList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        List<SecurityLeakScanRecord> recordList = securityLeakScanRecordDao.exportList(page);
        List<LeakScanSummaryVo> dtoList = Lists.newArrayList();
        for (SecurityLeakScanRecord scanRecord : recordList) {
            LeakScanSummaryVo dto = new LeakScanSummaryVo();
            BeanUtils.copyProperties(scanRecord, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public PageResponse<SecurityLeakScanReportVo> reportList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = securityLeakScanReportDao.reportListCount(page);
        PageResponse<SecurityLeakScanReportVo> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<SecurityLeakScanReport> recordList = securityLeakScanReportDao.reportList(page);
            List<SecurityLeakScanReportVo> dtoList = Lists.newArrayList();
            for (SecurityLeakScanReport report : recordList) {
                SecurityLeakScanReportVo dto = new SecurityLeakScanReportVo();
                BeanUtils.copyProperties(report, dto);
                dtoList.add(dto);
            }
            pageResponse.setResult(dtoList);
        }
        return pageResponse;
    }

    @Override
    public List<SecurityLeakScanReportVo> getReportListByScanId(String scanId) {
        List<SecurityLeakScanReportVo> dtoList = Lists.newArrayList();
        List<SecurityLeakScanReport> reportList = securityLeakScanReportDao.getReportListByScanId(scanId);
        for (SecurityLeakScanReport report : reportList) {
            SecurityLeakScanReportVo dto = new SecurityLeakScanReportVo();
            BeanUtils.copyProperties(report, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public void clearPastScanRecords(String bizLine, String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(dateStr);
        securityLeakScanReportDao.delete(bizLine, date);
        securityLeakScanRecordDao.delete(bizLine, date);
    }
    
    
    @Override
    public PageResponse<LeakScanSummaryVo> getLeakScanDetailByDate(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        //page.setPageSize(null);
        PageResponse<LeakScanSummaryVo> pageResponse = new PageResponse<>();
        int count = securityLeakScanRecordDao.summaryListCount(page);
        if(count >0) {
        	List<SecurityLeakScanRecord> recordList = securityLeakScanRecordDao.summaryList(page);
            List<LeakScanSummaryVo> dtoList = Lists.newArrayList();
            for (SecurityLeakScanRecord scanRecord : recordList) {
                LeakScanSummaryVo dto = new LeakScanSummaryVo();
                BeanUtils.copyProperties(scanRecord, dto);
                dtoList.add(dto);
            }
            pageResponse.setResult(dtoList);
        }
        pageResponse.setCount(count);
        return pageResponse;
    }

	@Override
	public Map<String, Object> leaksRankDistribute(String beginDate, String endDate) {
		return securityLeakScanRecordDao.getleaksStatCount(beginDate, endDate);
	}

	@Override
	public PageResponse<Map<String, Object>> leakStatByBiz(String beginDate, String endDate, Integer begin,Integer pageSize) {
		int count = securityLeakScanRecordDao.getLeaksStatCountTotalByBizLine(beginDate, endDate);
		PageResponse<Map<String, Object>> pageResponse = new PageResponse<>();
		if(count >0) {
			pageResponse.setResult(securityLeakScanRecordDao.getLeaksStatCountByBizLine(beginDate, endDate,begin,pageSize));
		}
		pageResponse.setCount(count);
		return pageResponse;
	}

	@Override
	public PageResponse<Map<String, Object>> leakStatListByBiz(String beginDate, String endDate, String rankType, Integer begin,Integer pageSize) {
		int count = securityLeakScanRecordDao.getCountTotalByleaks(beginDate, endDate);
		PageResponse<Map<String, Object>> pageResponse = new PageResponse<>();
		if(count >0) {
			pageResponse.setResult(securityLeakScanRecordDao.getCountByleaks(beginDate, endDate,rankType,begin,pageSize));
		}
		pageResponse.setCount(count);
		return pageResponse;
	}

	@Override
	public PageResponse<Map<String, Object>> leakTrend(String beginDate, String endDate, Integer begin,Integer pageSize) {
		int count = securityLeakScanRecordDao.getScanResultCountByDay(beginDate, endDate);
		PageResponse<Map<String, Object>> pageResponse = new PageResponse<>();
		if(count >0) {
			pageResponse.setResult(securityLeakScanRecordDao.getScanResultByDay(beginDate, endDate,begin,pageSize));
		}
		pageResponse.setCount(count);
		return pageResponse;
	}

	@Override
	public Map<String, Object> leakSummary(String beginDate, String endDate) {
		Map<String, Object> map1 = securityLeakScanRecordDao.getScanCount(beginDate, endDate);
		Map<String, Object> map2 = securityLeakScanRecordDao.getBizLineCount(beginDate, endDate);
		Map<String, Object> map3 = securityLeakScanRecordDao.getLeaksAllAndRepairedStatCount(beginDate, endDate);
		if(null == map3) {
			map3 = new HashMap();
		}
		if(null != map1 && map1.containsKey("count")) {
			map3.put("scanCount", map1.get("count"));
		}
		if(null != map2 && map2.containsKey("count")) {
			map3.put("bizCount", map2.get("count"));
		}
		return map3;
	}

    @Override
    public List<Map<String, Object>> getLeakByBizSystem() {
        return securityLeakScanRecordDao.getLeakByBizSystem();
    }

    @Override
    public List<Map<String, Object>> getLeakByIp() {
        return securityLeakScanRecordDao.getLeakByIp();
    }

    @Override
    public List<Map<String,Object>> getLeakByIdcType() {
        List<Map<String, Object>> leakByIdcType = securityLeakScanRecordDao.getLeakByIdcType();
        leakByIdcType.removeIf(list -> null == list.get("idcType"));
        return leakByIdcType;
    }
}
