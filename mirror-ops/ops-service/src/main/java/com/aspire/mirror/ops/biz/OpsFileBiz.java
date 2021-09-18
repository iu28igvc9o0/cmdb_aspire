package com.aspire.mirror.ops.biz;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsFile;
import com.aspire.mirror.ops.api.domain.OpsFileQueryModel;
import com.aspire.mirror.ops.api.domain.OpsGroupObjectTypeEnum;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.biz.model.OpsGroupObjectHandler;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.OpsFileDao;
import com.aspire.mirror.ops.util.SshUtil;
import com.aspire.mirror.ops.util.SshUtil.SshdServer;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.biz
 * 类名称:    OpsFileBiz.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/9 20:57
 * 版本:      v1.0
 */
@Service
@Transactional
@Slf4j
public class OpsFileBiz {
    @Autowired
    private OpsFileDao opsFileDao;

    @Autowired
    private OpsGroupObjectHandler opsGroupObjectHandler;

    @Autowired
    private CommonSftpServerConfig	sftpConfig;


    public GeneralResponse saveFile(OpsFile opsFile) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        Date now = new Date();
        opsFile.setLastUpdater(currUser);
        opsFile.setLastUpdateTime(now);
        if (opsFile.getFileId() == null) {
            OpsFile existedFile = opsFileDao.selectByNameAndVersion(opsFile.getFileName(), opsFile.getFileVersion());
            if (existedFile != null) {
                return new GeneralResponse(false, "The file name is already exists", opsFile);
            }
            opsFile.setCreater(currUser);
            opsFile.setCreateTime(now);
            opsFileDao.insert(opsFile);
        } else {
            OpsFile existedFile = opsFileDao.selectByNameAndVersion(opsFile.getFileName(), opsFile.getFileVersion());
            if (existedFile != null && !existedFile.getFileId().equals(opsFile.getFileId())) {
                return new GeneralResponse(false, "The file name is already exists", opsFile);
            }
            opsFileDao.updateByFileId(opsFile);
        }
        opsGroupObjectHandler.saveOpsGroup(opsFile, opsFile.getFileId(), OpsGroupObjectTypeEnum.FILE.getStatusCode());
        return new GeneralResponse(true, "", opsFile);
    }

    public void delete(Long fileId) {
//        OpsFile opsFile = opsFileDao.getFileById(fileId);
//        // 删除ops文件
//        SshdServer sftpServer = new SshdServer();
//        sftpServer.setIpAddress(sftpConfig.getIpAddress());
//        sftpServer.setPort(sftpConfig.getPort());
//        sftpServer.setLoginUser(sftpConfig.getLoginUser());
//        sftpServer.setLoginPass(sftpConfig.getLoginPass());
//
//        String subDir = opsFile.getFilePath();
//        String remotePath = sftpConfig.getRootDirectory() + subDir;
//        SshUtil.SshResultWrap sshResultWrap = SshUtil.executeShellCmd(sftpServer, 5, "rm -rf" + remotePath);
//        if (sshResultWrap.isSuccess()) {
            opsFileDao.delete(fileId);
            opsGroupObjectHandler.deleteGroupRelationByObjectIdAndType(fileId, OpsGroupObjectTypeEnum.FILE.getStatusCode());
//        } else {
//            log.error("删除文件出错！{0}", sshResultWrap.getBizLog());
//            throw new TimeoutException("删除文件出错！");
//        }
    }


    public OpsFile getFileById(Long fileId) {
        OpsFile opsFile = opsFileDao.getFileById(fileId);
        return opsFile;
    }

    public PageListQueryResult<OpsFile> pageList(OpsFileQueryModel opsFileQueryModel) {
        PageListQueryResult<OpsFile> result = new PageListQueryResult<>();
        Integer totalCount = opsFileDao.queryListTotalSize(opsFileQueryModel);
        List<OpsFile> fileList = Lists.newArrayList();
        if (totalCount > 0) {
            fileList = opsFileDao.queryList(opsFileQueryModel);
        }
        result.setTotalCount(totalCount);
        result.setDataList(fileList);
        return result;
    }
}
