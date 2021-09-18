package com.aspire.fileCheck.service.impl;

import com.aspire.fileCheck.dao.IFileIndicationUploadDAO;
import com.aspire.fileCheck.entity.FileIndicationPeriodConfigEntity;
import com.aspire.fileCheck.entity.FileIndicationUploadEntity;
import com.aspire.fileCheck.entity.RemoteFile;
import com.aspire.fileCheck.service.IFileIndicationPeriodConfigService;
import com.aspire.fileCheck.service.IFileIndicationUploadService;
import com.aspire.fileCheck.util.FileConst;
import com.aspire.fileCheck.util.FileMirrorDict;
import com.aspire.fileCheck.util.SSHUtil;
import com.aspire.fileCheck.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FileIndicationUploadServiceImpl implements IFileIndicationUploadService {

    @Autowired
    private IFileIndicationUploadDAO fileIndicationUploadDao;
    @Autowired
    private IFileIndicationPeriodConfigService fileIndicationPeriodConfigService;

    @Override
    public List<Map<String, String>> getFileUploadDelayDetectionData(Map<String, Object> param) {
        return fileIndicationUploadDao.getFileUploadDelayDetectionData1(param);
    }

    @Override
    public void batchInsert(List<FileIndicationUploadEntity> entityList) {
        fileIndicationUploadDao.batchInsert(entityList);
    }

    @Override
    public List<FileIndicationUploadEntity> getUploadFilesByPeriodConfigId(Integer configId) {
        return fileIndicationUploadDao.getUploadFilesByPeriodConfigId(configId);
    }

    @Override
    public void deleteEntity(FileIndicationUploadEntity entity) {
        fileIndicationUploadDao.deleteEntity(entity);
    }

    /**
     * 文件上传延迟检查
     */
    public void check() {
        long beginTime = Calendar.getInstance().getTimeInMillis();
        log.info("开始文件上传延迟检测:{}", beginTime);
        List<String> periodList = TimeUtil.getNeedCalcPeriod(new Date());
        for (final String period : periodList) {
            final String calcDate = period.substring(0,8);
            final String calcHour = period.substring(8,10);
            //获取该小时所属的时区段, 包含的指标配置数据
            List<FileIndicationPeriodConfigEntity> periodConfigList = fileIndicationPeriodConfigService.getConfigsByPeriod(calcHour);
            if (periodConfigList == null || periodConfigList.size() == 0) {
                return;
            }
            for (FileIndicationPeriodConfigEntity config : periodConfigList) {
                log.info("Begin check date {} province {} period {}. indication {}", calcDate, config.getProvinceCode(),
                        calcHour, config.getFileIndication().getFileIndicationName());
                //检查文件缺失
                StringBuilder pathBuilder = new StringBuilder();
//                pathBuilder.append(config.getProvinceEntity().getProvinceFileDir());
                pathBuilder.append("/");
                pathBuilder.append(calcDate);
                pathBuilder.append(config.getFileIndication().getFileIndicationDir());

                List<RemoteFile> fileList = SSHUtil.getRemoteFiles(config.getProvinceEntity(), pathBuilder.toString());
                List<String> fileNameList = new ArrayList<String>();
                List<FileIndicationUploadEntity> commitDataList = new ArrayList<FileIndicationUploadEntity>();
                for (RemoteFile file : fileList) {
                    String fileName = file.getFileName();
                    fileNameList.add(fileName);
                    //判断文件是否包含日期, 如果包含则默认属于该时间段
                    if (fileName.contains(period)) {
                        long timeInMillis = file.getModifyDate().getTime();
                        Calendar lastModifyTime = Calendar.getInstance();
                        lastModifyTime.setTimeInMillis(timeInMillis);
                        Date fileDate;
                        try {
                            String[] splitFile = fileName.split("_");
                            fileDate = DateUtils.parseDate(splitFile[1], new String[]{TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS});
                        } catch (Exception e) {
                            log.error("文件{}名称无效,请确保文件名称开头格式如:指标名称_yyyyMMddHHmmss_ .", fileName);
                            continue;
                        }
                        //文件创建时间-文件所属时间段时间-2个小时(2个小时延迟属于正常) = 延迟时间
                        long lazy = timeInMillis - fileDate.getTime() - TimeUtil.getHourInMillis(FileConst.PERIOD_LAZY_HOUR);
                        Integer lazyStatus = null;
                        //小于1小时
                        if (lazy > 0 && lazy <= TimeUtil.getHourInMillis(1)) {
                            lazyStatus = FileMirrorDict.LAZY_STATUS_ONE;
                        } else if (lazy > TimeUtil.getHourInMillis(1) && lazy <= TimeUtil.getHourInMillis(2)) {
                            lazyStatus = FileMirrorDict.LAZY_STATUS_ONETOTWO;
                        } else if (lazy > TimeUtil.getHourInMillis(2) && lazy <= TimeUtil.getHourInMillis(6)) {
                            lazyStatus = FileMirrorDict.LAZY_STATUS_TWOTOSIX;
                        } else if (lazy > TimeUtil.getHourInMillis(6)) {
                            lazyStatus = FileMirrorDict.LAZY_STATUS_SIX;
                        }
                        //有延迟, 则入库
                        if (lazyStatus != null) {
                            FileIndicationUploadEntity entity = new FileIndicationUploadEntity();
                            entity.setUploadDate(calcDate);
                            entity.setPeriodConfigId(config.getConfigId());
                            entity.setFileName(fileName);
                            entity.setDictLazyStatus(lazyStatus);
                            entity.setFileUploadTime(DateFormatUtils.format(fileDate, TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
                            entity.setFileCreateTime(DateFormatUtils.format(lastModifyTime.getTime(), TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
                            commitDataList.add(entity);
                        }
                    }
                }
                if (commitDataList.size() > 0) {
                    batchInsert(commitDataList);
                }
                //判断是否有删除的文件, 如果有则需要删除
                List<FileIndicationUploadEntity> uploadList = getUploadFilesByPeriodConfigId(config.getConfigId());
                if (uploadList == null || uploadList.size() == 0) {
                    continue;
                }
                for (FileIndicationUploadEntity entity : uploadList) {
                    if (!fileNameList.contains(entity.getFileName())) {
                        //需要删除文件记录
                        deleteEntity(entity);
                    }
                }
            }
        }
        long endTime = Calendar.getInstance().getTimeInMillis();
        log.info("文件上传延迟检测结束:{}. 用时:{}s", endTime, (endTime - beginTime) / 1000);
    }
}
