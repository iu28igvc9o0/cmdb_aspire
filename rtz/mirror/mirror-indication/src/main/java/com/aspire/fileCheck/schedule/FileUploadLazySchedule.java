package com.aspire.fileCheck.schedule;

import com.aspire.fileCheck.service.IFileIndicationUploadService;
import com.aspire.fileCheck.util.FileConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@Slf4j
public class FileUploadLazySchedule extends BaseSchedule<FileUploadLazySchedule> {

    @Autowired
    private IFileIndicationUploadService fileIndicationUploadService;

    /**
     * 文件上传延时检测
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checked(){
        updateJobManager("CHECK_FILE_UPLOAD_LAZY", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_RUNNING);
        fileIndicationUploadService.check();
        updateJobManager("CHECK_FILE_UPLOAD_LAZY", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_FINISH);
    }
}
