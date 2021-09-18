package com.aspire.ums.cmdb.ftp.service;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FtpService {

    /**
     * 上传文件到ftp
     * @param fileName
     * @param input
     * @return
     */
    Map<String, String> uploadtoFTP(String folderName, String fileName, InputStream input);

    /**
     * 上传文件到ftp
     * @param fileName
     * @param input
     * @return
     */
    Map<String, String> uploadtoFTP(String fileName, InputStream input);

    /**
     * 获取所有文件
     * @param folderName
     * @return
     */
    FTPFile[] getFileList(String folderName);
}
