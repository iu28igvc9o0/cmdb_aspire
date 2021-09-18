package com.migu.tsg.microservice.atomicservice.composite.common;

import java.io.InputStream;
import java.util.Map;

public interface FtpService {

    /**
     * 上传文件到ftp
     * @param fileName
     * @param input
     * @return
     */
    Map<String, String> uploadtoFTP(String fileName, InputStream input);

    /**
     * 上传图片到ftp
     * @param fileName
     * @param input
     * @return
     */
    Map<String, String> uploadImageToFTP(String fileName, InputStream input);

	Map<String, String> uploadImageToFTP2(String path, String fileName, InputStream input);
}
