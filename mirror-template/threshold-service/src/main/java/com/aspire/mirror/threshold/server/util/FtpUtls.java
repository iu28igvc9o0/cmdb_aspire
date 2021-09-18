package com.aspire.mirror.threshold.server.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author JinSu
 */
//@Component
@Slf4j
public class FtpUtls implements AutoCloseable {

    private FTPClient ftpClient;

    public FtpUtls(String host, Integer port, String username, String password) throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        ftpClient.login(username, password);
        ftpClient.setBufferSize(1024);//设置上传缓存大小
        ftpClient.setControlEncoding("UTF-8");//设置编码
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//设置文件类型
    }

    /**
     * 下载ftp文件到本地
     *
     * @param remoteFileName 远程文件名称
     * @param localFile      本地文件[包含路径]
     * @return true/false
     * @throws IOException 异常
     */
    public boolean downloadFile(String remoteFileName, String localFile, Boolean isPath) throws IOException {
        boolean isSucc = true;
//        File outFileName = new File(localFile);
        OutputStream is = null;
        if (ftpClient == null)
            throw new IOException("ftp server not login");
        try {
            if (isPath) {
//                Boolean changesDirBool = ftpClient.changeWorkingDirectory(remoteFileName);
                Boolean changesDirBool = ftpClient.changeWorkingDirectory(new String(remoteFileName.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
                log.debug("change dir is " + changesDirBool);
                if (changesDirBool) {
                    FTPFile[] fs = ftpClient.listFiles();
                    for (FTPFile ff : fs) {
                        if (ff.getName().lastIndexOf(".txt") != -1) {
                            File outLocalFile = new File(localFile + "/" + ff.getName());
                            if (!outLocalFile.exists()) {
                                Boolean addFileBool = outLocalFile.createNewFile();
                                log.debug("local file create is " + addFileBool);
//                        Files.createFile(Paths.get(localFile + "/" + ff.getName()));
                                is = new FileOutputStream(outLocalFile);
                                Boolean downloadResult = ftpClient.retrieveFile(ff.getName(), is);
                                log.debug("ftp file name {}, download status is {}", ff.getName(), downloadResult);
                                // 存在一个失败则状态为失败
                                if (downloadResult == false) {
                                    isSucc = false;
                                }
                            }
                        }
                    }
                } else {
                    isSucc = false;
                }
            } else {
                OutputStream outputStream = new FileOutputStream(localFile);
                isSucc = ftpClient.retrieveFile(remoteFileName, outputStream);
            }
        } catch (Exception e) {
            log.error("------FTPUtil download file is error!", e);
            isSucc = false;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return isSucc;
    }

    /**
     * 上传文件制定目录
     *
     * @param remoteFileName 远程文件名
     * @param localFile      本地文件[必须带路径]
     * @return true/false
     * @throws IOException 异常
     */
    public boolean uploadFile(String remotePath, String remoteFileName, String localFile) throws IOException {
        boolean isSucc;
        try (InputStream inputStream = new FileInputStream(localFile)) {
            if (ftpClient == null)
                throw new IOException("ftp server not login");
            isSucc = uploadFile(remotePath, remoteFileName, inputStream);
        }
        return isSucc;
    }

    public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
        try {
            log.info("开始上传文件");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            //告知服务器打开客户端将连接到的数据端口以进行数据传输
            ftpClient.enterLocalPassiveMode();
            createDirecroty(pathname);
//            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);


            // 分批多少字节上传
//            OutputStream output;
//            output = ftpClient.storeFileStream(fileName);
//            Util.copyStream(inputStream, output);
//            output.close();
//            if(!ftpClient.completePendingCommand()) {
//                ftpClient.logout();
//                ftpClient.disconnect();
//                System.err.println("File transfer failed.");
//                System.exit(1);
//            }
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            log.info("上传文件【成功】");
        } catch (Exception e) {
            log.info("上传文件【失败】");
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean createDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * @Description: 改变边目录路径
     **/
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                log.info("进入文件夹" + directory + " 成功！");

            } else {
                log.info("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    @Override
    public void close() throws Exception {
        if (ftpClient != null && ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }
}
