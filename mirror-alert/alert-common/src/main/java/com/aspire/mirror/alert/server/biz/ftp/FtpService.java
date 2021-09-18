package com.aspire.mirror.alert.server.biz.ftp;

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
    Map<String, Object> uploadtoFTP(String fileName, InputStream input);
    
    Map<String, Object> uploadtoFTPNew(String fileName, InputStream input,String path);

	void download(String fileName, String ftpPath,String localPath,String day);

    /**
     * ftp文件批量打包再上传ftp，用于页面下载
     * @param pathList
     * @return
     */
    Map<String, Object> downloadBatch (List<String> pathList);
}
