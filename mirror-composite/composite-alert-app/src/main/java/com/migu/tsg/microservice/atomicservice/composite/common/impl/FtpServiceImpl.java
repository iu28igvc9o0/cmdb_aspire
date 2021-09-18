package com.migu.tsg.microservice.atomicservice.composite.common.impl;

import com.migu.tsg.microservice.atomicservice.composite.common.FtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FtpServiceImpl implements FtpService {

    @Value("${ftp.host:}")
    private String host;

    @Value("${ftp.port:}")
    private final int port = FTPClient.DEFAULT_PORT;

    @Value("${ftp.username:}")
    private String username;

    @Value("${ftp.password:}")
    private String password;

    @Value("${ftp.path:}")
    private String path;

    @Value("${ftp.produceIconPath:}")
    private String produceIconPath;
    @Value("${ftp.bufferSize: 8096}")
    private int bufferSize;
    @Value("${ftp.mode:pasv}")
    private String mode;
    private static final String SLS_FTP_MODE_PORT = "port";
    private static final String SLS_FTP_MODE_PASV = "pasv";

    /**
     * 上传文件到ftp
     * @param fileName
     * @param input
     * @return
     */
    @Override
    public Map<String, String> uploadtoFTP(String fileName, InputStream input) {
        log.info("ftp.host:{}, ftp.port:{}, ftp.username:{}, ftp.password:{}, ftp.path:{}", host,port,username,password,path);
        Map<String, String> map = new HashMap<>();
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateDir = df.format(date);
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器
            ftp.login(username, password);// 登录
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(bufferSize);
            ftp.setControlEncoding("UTF-8");
            // 设置Linux环境
            FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
            ftp.configure(conf);
            switch (mode) {
                case SLS_FTP_MODE_PORT:
                    ftp.enterLocalActiveMode();//设置为主动模式
                    break;
                case SLS_FTP_MODE_PASV:
                    ftp.enterLocalPassiveMode();//设置为被动模式
                    ftp.setRemoteVerificationEnabled(false);
                    break;
                default:
            }
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                map.put("code", "error");
                map.put("message", "ftp isPositiveCompletion failed.");
                return map;
            }
            // 切换到上传目录
            if (ftp.changeWorkingDirectory(path)) {
                // 如果目录不存在创建目录
                if (!ftp.changeWorkingDirectory(dateDir)) {
                    if (!ftp.makeDirectory(dateDir)) {
                        map.put("code", "error");
                        map.put("message", "ftp maked directory failed.");
                        return map;
                    } else {
                        ftp.changeWorkingDirectory(dateDir);
                    }
                }
            } else {
                map.put("code", "error");
                map.put("message", "change working directory failed.");
                return map;
            }
            // 上传文件
            String[] fileNames = fileName.split("\\.");
            String formalName = UUID.randomUUID().toString();
            if (fileNames.length >= 2) {
                formalName = formalName + "." + fileNames[fileNames.length - 1];
            }
            if (!ftp.storeFile(formalName, input)) {
                map.put("code", "error");
                map.put("message", "ftp upload failed.");
                return map;
            }
            map.put("code", "success");
            map.put("message", "success");
            if (path.startsWith("/")) {
                map.put("path", path + "/" + dateDir+ "/" + formalName);
            } else {
                map.put("path", "/" + path + "/" + dateDir+ "/" +  formalName);
            }
        } catch (IOException e) {
            log.error("upload error",e);
            map.put("code", "error");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
            try {
                ftp.logout();
            }catch (IOException e) {
                log.error("upload error",e);
            }

            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("upload error",ioe);
                }
            }
        }
        return map;
    }

    /**
     * 上传图片到ftp
     * @param fileName
     * @param input
     * @return
     */
    @Override
    public Map<String, String> uploadImageToFTP(String fileName, InputStream input) {
        log.info("ftp.host:{}, ftp.port:{}, ftp.username:{}, ftp.password:{}, ftp.path:{}", host,port,username,password,path);
        Map<String, String> map = new HashMap<>();
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器
            ftp.login(username, password);// 登录
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(bufferSize);
            // 设置Linux环境
            FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
            ftp.configure(conf);
            switch (mode) {
                case SLS_FTP_MODE_PORT:
                    ftp.enterLocalActiveMode();//设置为主动模式
                    break;
                case SLS_FTP_MODE_PASV:
                    ftp.enterLocalPassiveMode();//设置为被动模式
                    ftp.setRemoteVerificationEnabled(false);
                    break;
                default:
            }
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                map.put("code", "error");
                map.put("message", "ftp isPositiveCompletion failed.");
                return map;
            }
            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(produceIconPath)) {
                if (!ftp.makeDirectory(produceIconPath)) {
                    map.put("code", "error");
                    map.put("message", "ftp maked directory failed.");
                    return map;
                }
                ftp.changeWorkingDirectory(produceIconPath);
            }
            if (!ftp.storeFile(fileName, input)) {
                map.put("code", "error");
                map.put("message", "ftp upload failed.");
                return map;
            }
            map.put("code", "success");
            map.put("message", "success");
            if (produceIconPath.startsWith("/")) {
                map.put("path", produceIconPath + fileName);
            } else {
                map.put("path", "/" + produceIconPath + fileName);
            }
        } catch (IOException e) {
            log.error("upload error",e);
            map.put("code", "error");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
            try {
                ftp.logout();
            }catch (IOException e) {
                log.error("upload error",e);
            }

            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("upload error",ioe);
                }
            }
        }
        return map;
    }


    /**
     * 上传图片到ftp
     * @param fileName
     * @param input
     * @return
     */
    @Override
    public Map<String, String> uploadImageToFTP2(String path,String fileName, InputStream input) {
        log.info("ftp.host:{}, ftp.port:{}, ftp.username:{}, ftp.password:{}, ftp.path:{}", host,port,username,password,path);
        Map<String, String> map = new HashMap<>();
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器
            ftp.login(username, password);// 登录
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(bufferSize);
            // 设置Linux环境
            FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
            ftp.configure(conf);
            switch (mode) {
                case SLS_FTP_MODE_PORT:
                    ftp.enterLocalActiveMode();//设置为主动模式
                    break;
                case SLS_FTP_MODE_PASV:
                    ftp.enterLocalPassiveMode();//设置为被动模式
                    ftp.setRemoteVerificationEnabled(false);
                    break;
                default:
            }
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                map.put("code", "error");
                map.put("message", "ftp isPositiveCompletion failed.");
                return map;
            }
            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(path)) {
                if (!ftp.makeDirectory(path)) {
                    map.put("code", "error");
                    map.put("message", "ftp maked directory failed.");
                    return map;
                }
                ftp.changeWorkingDirectory(path);
            }
            if (!ftp.storeFile(fileName, input)) {
                map.put("code", "error");
                map.put("message", "ftp upload failed.");
                return map;
            }
            map.put("code", "success");
            map.put("message", "success");
            if (path.startsWith("/")) {
                map.put("path", path + fileName);
            } else {
                map.put("path", "/" + path + fileName);
            }
        } catch (IOException e) {
            log.error("upload error",e);
            map.put("code", "error");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
            try {
                ftp.logout();
            }catch (IOException e) {
                log.error("upload error",e);
            }

            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("upload error",ioe);
                }
            }
        }
        return map;
    }
}
