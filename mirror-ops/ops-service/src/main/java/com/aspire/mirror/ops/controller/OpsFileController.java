package com.aspire.mirror.ops.controller;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsFile;
import com.aspire.mirror.ops.api.domain.OpsFileQueryModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.service.IOpsFileService;
import com.aspire.mirror.ops.biz.OpsFileBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件管理控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.controller
 * 类名称:    OpsFileController.java
 * 类描述:    文件管理控制层
 * 创建人:    JinSu
 * 创建时间:  2020/6/9 20:53
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class OpsFileController implements IOpsFileService {
    @Autowired
    private OpsFileBiz opsFileBiz;
    @Override
    public GeneralResponse saveFile(@RequestBody OpsFile opsFile) {
        if (opsFile == null) {
            log.error("OpsFileController[saveFile] param opsFile is null");
            return new GeneralResponse(false, "saveFile method param is not valid.");
        }
        return opsFileBiz.saveFile(opsFile);
    }

    @Override
    public GeneralResponse deleteFile(@RequestParam("file_id") Long fileId) {
        if (fileId == null) {
            log.error("OpsFileController[deleteFile] param fileId is null");
            return new GeneralResponse(false, "deleteFile method param is not valid");
        }
        opsFileBiz.delete(fileId);
        return new GeneralResponse();
    }

//    @Override
//    public GeneralResponse updateFile(@RequestBody OpsFile opsFile) {
//        if (opsFile == null || opsFile.getFileId() == null) {
//            log.error("OpsFileController[updateFile] param opsFile or fileId is null");
//            return new GeneralResponse(false, "updateFile method param is not valid");
//        }
//        return opsFileBiz.updateFile(opsFile);
//    }

    @Override
    public OpsFile getFileDetail(@RequestParam("file_id") Long fileId) {
        if (fileId == null) {
            log.warn("OpsFileController[deleteFile] param fileId is null");
            return null;
        }
        return opsFileBiz.getFileById(fileId);
    }

    @Override
    public PageListQueryResult<OpsFile> pageList(@RequestBody OpsFileQueryModel opsFileQueryModel) {
        return opsFileBiz.pageList(opsFileQueryModel);
    }
}
