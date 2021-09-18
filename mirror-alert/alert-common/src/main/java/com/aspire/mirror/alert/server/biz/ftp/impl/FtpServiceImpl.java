package com.aspire.mirror.alert.server.biz.ftp.impl;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aspire.mirror.alert.server.biz.ftp.FtpService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class FtpServiceImpl implements FtpService {
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.port}")
    private int port = FTPClient.DEFAULT_PORT;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.path}")
    private String path;
    @Value("${ftp.bufferSize: 8096}")
    private int bufferSize;
    @Value("${ftp.mode:pasv}")
    private String mode;
    private static final String SLS_FTP_MODE_PORT = "port";
    private static final String SLS_FTP_MODE_PASV = "pasv";
    
    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";
    // FTP协议里面，规定文件名编码为iso-8859-1
    private static final String SERVER_CHARSET = "ISO-8859-1";
    private static final String SYSTEM_LINE_SEPRATOR =File.separator;

    /**
     * 上传文件到ftp
     * @param fileName
     * @param input
     * @return
     */
    public Map<String, Object> uploadtoFTP(String fileName, InputStream input) {
        log.info("ftp.host:{}, ftp.port:{}, ftp.username:{}, ftp.password:{}, ftp.path:{}", host,port,username,password,path);
        Map<String, Object> map = new HashMap<>();
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
                map.put("code", "0002");
                map.put("message", "ftp isPositiveCompletion failed.");
                return map;
            }
            // 切换到上传目录
            if (ftp.changeWorkingDirectory(path)) {
                // 如果目录不存在创建目录
//                String[] dirs = filePath.split("/");
//                String tempPath = "";
//                for (String dir : dirs) {
//                    if (null == dir || "".equals(dir))
//                        continue;
//                    tempPath += "/" + dir;
//                    if (!ftp.changeWorkingDirectory(tempPath)) {
//                        if (!ftp.makeDirectory(tempPath)) {
//                            map.put("code", "0003");
//                            map.put("message", "ftp maked directory failed.");
//                            return map;
//                        } else {
//                            ftp.changeWorkingDirectory(tempPath);
//                        }
//                    }
//                }
                if (!ftp.changeWorkingDirectory(dateDir)) {
                    if (!ftp.makeDirectory(dateDir)) {
                        map.put("code", "0003");
                        map.put("message", "ftp maked directory failed.");
                        return map;
                    } else {
                        ftp.changeWorkingDirectory(dateDir);
                    }
                }
            } else {
                map.put("code", "0006");
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
                map.put("code", "0004");
                map.put("message", "ftp upload failed.");
                return map;
            }
            map.put("code", "0000");
            map.put("message", "success");
            if (path.startsWith("/")) {
                map.put("path", path + "/" + dateDir+ "/" + formalName);
            } else {
                map.put("path", "/" + path + "/" + dateDir+ "/" +  formalName);
            }
        } catch (IOException e) {
            log.error("upload error",e);
            map.put("code", "0005");
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
     * 上传文件到ftp
     * @param fileName
     * @param input
     * @return
     */
    public Map<String, Object> uploadtoFTPNew(String fileName, InputStream input,String pathNew) {
    	
        log.info("ftp.host:{}, ftp.port:{}, ftp.username:{}, ftp.password:{}, ftp.path:{}", host,port,username,password,path);
        Map<String, Object> map = new HashMap<>();
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
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftp.setControlEncoding(LOCAL_CHARSET);
            
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
                map.put("code", "0002");
                map.put("message", "ftp isPositiveCompletion failed.");
                return map;
            }
            // 切换到上传目录
            if (ftp.changeWorkingDirectory(this.path)) {
            	if(StringUtils.isNotBlank(pathNew)) {
            		if(!ftp.changeWorkingDirectory(pathNew)){
                		ftp.makeDirectory(pathNew);
                		ftp.changeWorkingDirectory(pathNew);
                	}
            	}
            	

                if (!ftp.changeWorkingDirectory(dateDir)) {
                    if (!ftp.makeDirectory(dateDir)) {
                        map.put("code", "0003");
                        map.put("message", "ftp maked directory failed.");
                        return map;
                    } else {
                        ftp.changeWorkingDirectory(dateDir);
                    }
                }
            } else {
                map.put("code", "0006");
                map.put("message", "change working directory failed.");
                return map;
            }

            ftp.storeFile(new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET), input);

            map.put("code", "0000");
            map.put("message", "success");
            StringBuilder pathSb = new StringBuilder();
            if (!path.startsWith("/")) {
                pathSb.append("/");
            }
            pathSb.append(path).append("/");
            if (StringUtils.isNotEmpty(pathNew)) {
                pathSb.append(pathNew);
            }
            pathSb.append("/").append(dateDir).append("/").append(fileName);
            map.put("path",pathSb.toString());
        } catch (IOException e) {
            log.error("upload error",e);
            map.put("code", "0005");
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
    
    @Override
    public void download(String fileName,String ftpPath,String localPath,String day) {
    	   log.info("ftp.host:{}, ftp.port:{}, ftp.username:{}, ftp.password:{}, ftp.path:{}", host,port,username,password,path);
           Map<String, Object> map = new HashMap<>();
           Date date = new Date();
           DateFormat df = new SimpleDateFormat("yyyyMMdd");
           String dateDir = df.format(date);
           FTPClient ftp = new FTPClient();
           OutputStream outputStream = null;
           try {
               ftp.connect(host, port);// 连接FTP服务器
               ftp.login(username, password);// 登录
               // 设置上传文件的类型为二进制类型
               ftp.setFileType(FTP.BINARY_FILE_TYPE);
               ftp.setBufferSize(bufferSize);
              // ftp.setControlEncoding("UTF-8");
               if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                   LOCAL_CHARSET = "UTF-8";
               }
               ftp.setControlEncoding(LOCAL_CHARSET);
               // 设置Linux环境
               FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
               ftp.configure(conf);
               
               
               
            if (ftp.changeWorkingDirectory(this.path)) {
            	if(StringUtils.isNotBlank(ftpPath)) {
            		ftp.changeWorkingDirectory(ftpPath);
            		ftp.changeWorkingDirectory(day);
            	}
        //  String sss = ftp.printWorkingDirectory();
                    // 指定文件写入本地
            	String paths[] = localPath.split("\\/");
           	 String targetPath = "";
           	for(String p:paths) {
           		targetPath = targetPath+p+SYSTEM_LINE_SEPRATOR;
           	}
                   if(StringUtils.isBlank(fileName)) {
                	   FTPFile[] ftpFiles = ftp.listFiles();
                       for (FTPFile file : ftpFiles) {
                           String name = file.getName();
                           if (".".equals(name) || "..".equals(name)) {
                               continue;
                           }
                           String targetFile = targetPath  + dateDir+SYSTEM_LINE_SEPRATOR+name;
                           File localFile = new File(targetFile);

							if(!localFile.exists()){
							    //先得到文件的上级目录，并创建上级目录，在创建文件
								localFile.getParentFile().mkdirs();
							    try {
							        //创建文件
							    	localFile.createNewFile();
							    } catch (IOException e) {
							        e.printStackTrace();
							        return;
							    }
							}
                           outputStream = new FileOutputStream(localFile);
                          ftp.retrieveFile(new String(name.getBytes(LOCAL_CHARSET),
                                  SERVER_CHARSET), outputStream);
                          if (localFile.exists()) {
                          	log.info("成功下载文件: " + fileName + " 到本地: " + localFile.getAbsolutePath());
                              
                          } else {
                          	log.error("FTP 下载文件: {}/{} 失败 !!!!", ftpPath, fileName);
                          }
                       }
                   }else {
                	   String targetFile = targetPath  + dateDir+SYSTEM_LINE_SEPRATOR+fileName;
                       File localFile = new File(targetFile);
                       outputStream = new FileOutputStream(localFile);
                      ftp.retrieveFile(new String(fileName.getBytes(LOCAL_CHARSET),
                              SERVER_CHARSET), outputStream);
                      if (localFile.exists()) {
                      	log.info("成功下载文件: " + fileName + " 到本地: " + localFile.getAbsolutePath());
                          
                      } else {
                      	log.error("FTP 下载文件: {}/{} 失败 !!!!", ftpPath, fileName);
                      }
                   }
                   
                   
            } else {
            	log.error("#======>>> FTP 目录: {} 切换失败! 此目录可能不存在或者没有读取权限!", ftpPath);
            }
        } catch (Exception e) {
        	log.error("FTP 目录: {} 处理文件失败 !!!!", ftpPath, e);
        } finally {
        	IOUtils.closeQuietly(outputStream);
            try {
                ftp.logout();
            }catch (IOException e) {
                log.error("upload error",e);
            }
            if (ftp.isConnected()) {
                try {
                	ftp.disconnect();
                } catch (IOException e) {
                	log.error("断开FTP连接失败!", e);
                }
            }
        }
    }

    /**
     * ftp文件批量打包再上传ftp，用于页面下载
     * @param pathList
     * @return
     */
    public Map<String, Object> downloadBatch (List<String> pathList) {
        log.info("ftp.host:{}, ftp.port:{}, ftp.username:{}, ftp.password:{}, ftp.path:{}", host,port,username,password,path);
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isEmpty(pathList)) {
            map.put("code", "8888");
            map.put("message","empty annex");
            return map;
        }

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateDir = df.format(date);
        FTPClient ftp = new FTPClient();
        FileOutputStream fileOutputStream = null;
//        ZipOutputStream zipOut = null;
        InputStream inputStream = null;
        File zipFile = null;
        // 压缩文件
        String formalName = UUID.randomUUID().toString() + ".zip";
        zipFile = new File(formalName);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
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
                map.put("code", "0002");
                map.put("message", "ftp isPositiveCompletion failed.");
                return map;
            }

            List<FTPFile> ftpFileList = Lists.newArrayList();

            byte[] byteReader = new byte[bufferSize];
            ByteArrayOutputStream os = null;

            // 切换到下载目录
            for (String filePath: pathList) {
                log.info("download file path is : {}", filePath);
                int lastIndex = filePath.lastIndexOf("/");
                if (lastIndex < 0) {
                    continue;
                }
                try {
                    String pathName = filePath.substring(0, lastIndex);
                    String fileName = filePath.substring(lastIndex + 1);
                    ftp.changeWorkingDirectory(pathName);
                    FTPFile[] ftpFiles = ftp.listFiles(pathName);
                    if (ftpFiles == null || ftpFiles.length == 0) {
                        continue;
                    }
                    for (FTPFile ftpFile: ftpFiles) {
                        String name = ftpFile.getName();
                        if (fileName.equals(name)) {
                            ftpFileList.add(ftpFile);
                            if (ftpFile.isFile()) {
                                os = new ByteArrayOutputStream();
                                String downFileName = new String(ftpFile.getName().getBytes(LOCAL_CHARSET), SERVER_CHARSET);
                                // 从FTP上下载downFileName该文件把该文件转化为字节数组的输出流
                                ftp.retrieveFile(downFileName, os);
                                byte[] bytes = os.toByteArray();
                                InputStream ins = new ByteArrayInputStream(bytes);

                                int len;
                                zipOut.putNextEntry(new ZipEntry(ftpFile.getName()));
                                // 读入需要下载的文件的内容，打包到zip文件
                                while ((len = ins.read(byteReader)) > 0) {
                                    zipOut.write(byteReader, 0, len);
                                }
                            }

                            break;
                        }
                    }
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception e) {

                        }
                    }
                }
            }
            zipOut.flush();
            // 关闭zip文件输出流
            if (null != zipOut) {
                IOUtils.closeQuietly(zipOut);
            }

            //切换目录
            if (ftp.changeWorkingDirectory(path)) {
                if (!ftp.changeWorkingDirectory(dateDir)) {
                    if (!ftp.makeDirectory(dateDir)) {
                        map.put("code", "0003");
                        map.put("message", "ftp maked directory failed.");
                        return map;
                    } else {
                        ftp.changeWorkingDirectory(dateDir);
                    }
                }
            } else {
                map.put("code", "0006");
                map.put("message", "change working directory failed.");
                return map;
            }
            // 上传文件到ftp
            inputStream = new FileInputStream(formalName);
            if (!ftp.storeFile(formalName, inputStream)) {
                map.put("code", "0004");
                map.put("message", "ftp upload failed.");
                return map;
            }
            map.put("code", "0000");
            map.put("message", "success");
            if (path.startsWith("/")) {
                map.put("path", path + "/" + dateDir+ "/" + formalName);
            } else {
                map.put("path", "/" + path + "/" + dateDir+ "/" +  formalName);
            }
        } catch (IOException e) {
            log.error("upload error",e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {

                }
            }
//            // 关闭zip文件输出流
//            if (null != zipOut) {
//                try {
//                    zipOut.closeEntry();
//                    zipOut.close();
//                } catch (IOException e) {
//                    log.error("close ZipOutputStream connection error :" + e);
//                }
//            }

            // 关闭输入流
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("close inputStream connection error :" + e);
                }
            }

            //退出ftp
            try {
                ftp.logout();
            }catch (IOException e) {
                log.error("upload error",e);
            }

            // 断开ftp连接
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("upload error",ioe);
                }
            }

            //删除临时文件
            if (zipFile != null) {
                try {
                    zipFile.delete();
                } catch (Exception e) {
                    log.error("delete file error :" + e);
                }
            }
        }
        return map;
    }

}
