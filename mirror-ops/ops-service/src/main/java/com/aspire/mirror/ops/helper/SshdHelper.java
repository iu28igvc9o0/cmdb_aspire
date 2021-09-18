package com.aspire.mirror.ops.helper;

import com.aspire.mirror.ops.biz.CommonSftpServerConfig;
import com.aspire.mirror.ops.util.SshUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.helper
 * 类名称:    SshdHelp.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/10 19:43
 * 版本:      v1.0
 */
@Component
@Slf4j
public class SshdHelper {
    @Autowired
    private CommonSftpServerConfig sftpConfig;


//    private Pair<SshUtil.SshResultWrap, String> uploadReportFile(Path localFile, Long instanceId, String subDirTemp) {
//        SshUtil.SshdServer sftpServer = new SshUtil.SshdServer();
//        sftpServer.setIpAddress(sftpConfig.getIpAddress());
//        sftpServer.setPort(sftpConfig.getPort());
//        sftpServer.setLoginUser(sftpConfig.getLoginUser());
//        sftpServer.setLoginPass(sftpConfig.getLoginPass());
//
//        String subDir = subDirTemp + instanceId;
//        String remotePath = sftpConfig.getRootDirectory() + subDir;
//        SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);
//
//        Pair<SshUtil.SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, localFile, remotePath);
//        String relativePath = subDir + "/" + localFile.toFile().getName(); // 使用相对路径
//        return Pair.of(uploadResult.getLeft(), relativePath);
//    }

    public Pair<SshUtil.SshResultWrap, String> uploadFile(Path localFile, String modelPath) {
        SshUtil.SshdServer sftpServer = getSshdServer();
        String subDir = modelPath + localFile.getParent().toFile().getName();
        String remotePath = sftpConfig.getRootDirectory() + subDir;
        SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);

        Pair<SshUtil.SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, localFile, remotePath);
        String relativePath = subDir + "/" + localFile.toFile().getName(); // 使用相对路径
        return Pair.of(uploadResult.getLeft(), relativePath);
    }

    //    public SshUtil.SshdServer getSshdServer() {
//        SshUtil.SshdServer sftpServer = new SshUtil.SshdServer();
//        sftpServer.setIpAddress(sftpConfig.getIpAddress());
//        sftpServer.setPort(sftpConfig.getPort());
//        sftpServer.setLoginUser(sftpConfig.getLoginUser());
//        sftpServer.setLoginPass(sftpConfig.getLoginPass());
//        return sftpServer;
//    }
    public SshUtil.SshdServer getSshdServer() {
        SshUtil.SshdServer sshdServer = new SshUtil.SshdServer();
        sshdServer.setIpAddress(sftpConfig.getIpAddress());
        sshdServer.setPort(sftpConfig.getPort());
        sshdServer.setLoginUser(sftpConfig.getLoginUser());
        sshdServer.setLoginPass(sftpConfig.getLoginPass());
        return sshdServer;
    }
}
