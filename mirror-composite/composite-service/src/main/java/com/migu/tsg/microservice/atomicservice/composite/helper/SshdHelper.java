package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.config.CommonSftpServerConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.SshUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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

    private SshUtil.SshdServer sshdServer;

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
        if (sshdServer == null) {
            sshdServer = new SshUtil.SshdServer();
            sshdServer.setIpAddress(sftpConfig.getIpAddress());
            sshdServer.setPort(sftpConfig.getPort());
            sshdServer.setLoginUser(sftpConfig.getLoginUser());
            sshdServer.setLoginPass(sftpConfig.getLoginPass());
        }
        return sshdServer;
    }
    public Pair<String, List<File>> downloadFile(List<String> fullDownFilePathList, String tempDir, String filePath){
        SshUtil.SshdServer sftpServer = this.getSshdServer();
        Path localDownDir = null;
        List<File> filePathList = Lists.newArrayList();
        try {
            localDownDir = Files.createTempDirectory(tempDir);
            localDownDir = Files.createDirectory(Paths.get(localDownDir.toFile().getAbsolutePath() + File.separator + filePath));
            for (String fullDownFilePath : fullDownFilePathList) {

                Pair<SshUtil.SshResultWrap, Path> downResult
                        = SshUtil.sftpDownload(sftpServer, fullDownFilePath, localDownDir.toFile().getCanonicalPath());
                SshUtil.SshResultWrap wrap = downResult.getLeft();
                if (!wrap.isSuccess()) {
                    throw new RuntimeException(wrap.getBizLog());
                }else {
                    filePathList.add(downResult.getRight().toFile());
                }
            }
        } catch (IOException e) {
            log.error("download file for sftp is error!", e);
        }
        return Pair.of(localDownDir.getParent().toFile().getAbsolutePath(), filePathList);
    }

    public static void main(String[] args) {
        Path localDownDir = null;
        try {
            localDownDir = Files.createTempDirectory("aaa_");
            localDownDir = Files.createDirectory(Paths.get(localDownDir.toFile().getAbsolutePath() + File.separator + "bbb"));
            System.out.println(localDownDir.getParent().toFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void downloadFile(@RequestBody Map<String, String> downParam, HttpServletResponse response, String contentType, String tempDir) {
        String filePath = downParam.get("file_path");
        String isRelativePath = downParam.get("is_relative");
        String fullDownFilePath = isRelativePath != null && "N".equalsIgnoreCase(isRelativePath) ? filePath : sftpConfig.getRootDirectory() + filePath;
        String fileName = Paths.get(filePath).getFileName().toFile().getName();

//        String mimeType = request.getServletContext().getMimeType(fileName);
//        mimeType = mimeType == null ? "application/octet-stream": mimeType;
//        String mimeType = "application/octet-stream";
        FileInputStream fileInput = null;

        try (OutputStream os = response.getOutputStream()){
            SshUtil.SshdServer sftpServer = this.getSshdServer();
            Path localDownDir = Files.createTempDirectory(tempDir);
            Pair<SshUtil.SshResultWrap, Path> downResult
                    = SshUtil.sftpDownload(sftpServer, fullDownFilePath, localDownDir.toFile().getCanonicalPath());
            SshUtil.SshResultWrap wrap = downResult.getLeft();
            if (!wrap.isSuccess()) {
                os.write(wrap.getBizLog().getBytes());
                throw new RuntimeException(wrap.getBizLog());
            }
            response.setHeader("Content-Disposition",
                    "attachment;filename*=utf-8'zh_cn'".concat(URLEncoder.encode(fileName, "UTF-8")));
            response.setHeader("Connection", "close");
            response.setContentType(contentType.concat("; charset=UTF-8"));
            // luowenbo 2020-07-24 修改： 代码审计——存储型XSS缺陷
            response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max- age=seconds; HttpOnly");
            fileInput = new FileInputStream(downResult.getRight().toFile());
            byte[] buff = new byte[20480];
            int read = 0;
            while ((read = fileInput.read(buff)) > 0) {
                os.write(buff, 0, read);
            }
            os.flush();
        } catch (Exception e) {
            log.error("OpsFileManageController download file {} failed!", fullDownFilePath, e);
        } finally {
            IOUtils.closeQuietly(fileInput);
        }
    }

}
