package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util;

import com.alibaba.fastjson.util.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileDownloadUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileDownloadUtil.class);
    private static final String DEFAULT_CHARTER = "UTF-8";

    private static void makeDownloadDir(File dir) {
        if (dir.getParentFile().exists()) {
            dir.mkdir();
        } else {
            makeDownloadDir(dir.getParentFile());
            dir.mkdir();
        }
    }

    /**
     * 创建数据源文件
     * @param filePath
     * @param content
     * @return
     */
    public static boolean createSourceFile(String filePath, String content) {
        boolean flag = true;
        File file = new File(filePath);
        makeDownloadDir(file.getParentFile());
        FileOutputStream out = null;
        PrintWriter pw = null;
        try {
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            out = new FileOutputStream(file);
            pw = new PrintWriter(out);
            pw.write(content.toCharArray());
            pw.flush();
        } catch (IOException e) {
            LOG.error("download File write error.", e);
            flag = false;
        } finally {
            if (pw != null) {
                pw.close();
            }
            IOUtils.close(out);
        }
        return flag;
    }

    /**
     * 压缩数据源文件
     * @param sourceFiles
     * @param tempZipFile
     * @return
     */
    public static boolean compressFile(List<String> sourceFiles, String tempZipFile, boolean isClear){
        boolean flag = true;
        File tmpZip = new File(tempZipFile);
        ZipOutputStream out = null;
        try {
            if (tmpZip.exists()){
                tmpZip.delete();
            }
            tmpZip.createNewFile();
            out = new ZipOutputStream(new FileOutputStream(tmpZip));
            for (String sourceFile: sourceFiles) {
                File source = new File(sourceFile);
                if (!source.exists()) {
                    continue;
                }
                if (zipFile(source, out)) {
                    LOG.info("#====> Successfully created zip file: {}", tempZipFile);
                    if (isClear) {
                        source.delete();
                        LOG.info("#====> Successfully delete source file: {}", sourceFile);
                    }
                }
            }
        } catch (IOException e) {
            flag = false;
            LOG.error("compress download file error.", e);
        } finally {
            IOUtils.close(out);
        }
        return flag;
    }

    /**
     * 压缩
     * @param file
     * @param outputStream
     */
    private static boolean zipFile(File file, ZipOutputStream outputStream){
        boolean flag = true;
        FileInputStream in = null;
        BufferedInputStream bInStream = null;
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    in = new FileInputStream(file);
                    bInStream = new BufferedInputStream(in, 1024);
                    ZipEntry entry = new ZipEntry(file.getName());
//                    outputStream.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
                    outputStream.putNextEntry(entry);
                    final int MAX_BYTE = 10 * 1024 * 1024; // 最大的流为10M
                    long streamTotal = 0; // 接受流的容量
                    int streamNum = 0; // 流需要分开的数量
                    int leaveByte = 0; // 文件剩下的字符数
                    byte[] inOutbyte; // byte数组接受文件的数据

                    streamTotal = bInStream.available(); // 通过available方法取得流的最大字符数
                    streamNum = (int) Math.floor(streamTotal / MAX_BYTE); // 取得流文件需要分开的数量
                    leaveByte = (int) streamTotal % MAX_BYTE; // 分开文件之后,剩余的数量
                    if (streamNum > 0) {
                        for (int j=0; j < streamNum; ++j) {
                            inOutbyte = new byte[MAX_BYTE];
                            // 读入流,保存在byte数组
                            bInStream.read(inOutbyte, 0, MAX_BYTE);
                            outputStream.write(inOutbyte, 0, MAX_BYTE); // 写出流
                        }
                    }
                    // 写出剩下的流数据
                    inOutbyte = new byte[leaveByte];
                    bInStream.read(inOutbyte, 0, leaveByte);
                    outputStream.write(inOutbyte);
                    outputStream.flush();
                    outputStream.closeEntry(); // Closes the current ZIP entry
                } else {
                    File[] files = file.listFiles();
                    for (File innerFile : files) {
                        zipFile(innerFile, outputStream);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            flag = false;
            LOG.error("File not found.", e);
        } catch (IOException e) {
            flag = false;
            LOG.error("error in compress file: " + file.getName(), e);
        }finally {
            IOUtils.close(bInStream);
            IOUtils.close(in);
            IOUtils.close(outputStream);
        }
        return flag;
    }

    /**
     * 导出压缩文件
     * @param file
     * @param response
     * @param isClear 是否删除压缩包
     */
    public static void dowloadFile(File file, HttpServletResponse response, boolean isClear){
        LOG.info("#===> About to downlaod zip file: " + file.getAbsolutePath());
        if (file.exists()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                response.reset();
                response.setContentType("application/zip");
                response.setHeader("content-type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" +
                        URLEncoder.encode(file.getName(), DEFAULT_CHARTER));
                response.setHeader("Content-Length", String.valueOf(file.length()));
                // luowenbo 2020-07-24 修改： 代码审计——存储型XSS缺陷
                response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max- age=seconds; HttpOnly");
                response.setCharacterEncoding(DEFAULT_CHARTER);
                in = new BufferedInputStream(new FileInputStream(file));
                out = new BufferedOutputStream(response.getOutputStream());
                byte[] buffer = new byte[in.available()];
                in.read(buffer);
                out.write(buffer);
                out.flush();
            } catch (FileNotFoundException e) {
                LOG.error("zip file: {} download error.", file.getName(), e);
            } catch (IOException e) {
                LOG.error("zip file: {} download error.", file.getName(), e);
            } finally {
                IOUtils.close(out);
                IOUtils.close(in);
                if (isClear) {
                    String filePath = file.getAbsolutePath();
                    file.delete();
                    LOG.info("#===> Successfully deleted file: {}", filePath);
                }
            }
        } else {
            LOG.error("#== zip file: {} is not exists.", file.getName());
        }
    }

    /**
     * 压缩并导出
     * @param sourceFiles
     * @param tempZipFile
     * @param response
     */
    public static void zipLogDownload(List<String> sourceFiles, String tempZipFile,
                                      HttpServletResponse response, boolean isClear) {
        if (compressFile(sourceFiles, tempZipFile, isClear)) {
            dowloadFile(new File(tempZipFile), response, isClear);
        }
    }

    /**
     * 下载 Entity
     * @param sourceFiles
     * @param tempZipFile
     * @return
     */
    public static ResponseEntity<FileSystemResource> zipEntityExport(List<String> sourceFiles, String tempZipFile, boolean isClear){
        if (compressFile(sourceFiles, tempZipFile, isClear)) {
            File zipFile = new File(tempZipFile);
            if (!zipFile.exists()) {
                LOG.error("#=== zip file: " + tempZipFile + " is not exists !");
                return null;
            }
            String fileName = zipFile.getName();
//            fileName = fileName.indexOf(".") == -1 ? fileName : fileName.substring(0, fileName.indexOf("."));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + fileName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipFile.length())
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(new FileSystemResource(zipFile));
        }
        return null;
    }

    /**
     * 直接压缩并且response输出
     * @param sourceFile
     * @param response
     */
    public static void zipLogDownload(String sourceFile, HttpServletResponse response, boolean isClear){
        File logFile = new File(sourceFile);
        String logFileName = logFile.getName();
        String zipSuffix = ".zip";
        String zipFileName = logFileName.indexOf(".") != -1 ?
                logFileName.substring(0, logFileName.lastIndexOf(".")) + zipSuffix :
                logFileName + zipSuffix;
        boolean zipFlg = true;
        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            //响应头的设置
            response.reset();
            response.setContentType("application/octet-stream");
//            response.setHeader("Content-type", "application-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipFileName, DEFAULT_CHARTER));
            response.setCharacterEncoding(DEFAULT_CHARTER);
            // luowenbo 2020-07-24 修改： 代码审计——存储型XSS缺陷
            response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max- age=seconds; HttpOnly");
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
//            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
        } catch (Exception e) {
            zipFlg = false;
            LOG.error("error in compress log file.", e);
        }
        if (!zipFlg) {
            return;
        }
        //循环将文件写入压缩流
        DataOutputStream os = null;
        try {
            zipos.putNextEntry(new ZipEntry(logFileName));
            os = new DataOutputStream(zipos);
            InputStream is = new FileInputStream(logFile);
            byte[] b = new byte[100];
            int length = 0;
            while((length = is.read(b))!= -1){
                os.write(b, 0, length);
            }
            is.close();
            zipos.closeEntry();
            os.flush();
        } catch (IOException e) {
            LOG.error("Error in download zip file.", e);
        } finally {
            IOUtils.close(os);
            IOUtils.close(zipos);
        }
        if (isClear) {
            logFile.delete();
        }
    }

    /**
     * 直接压缩字符串
     * @param content
     * @param logFileName
     * @param zipFileName
     * @param response
     */
    public static void zipLogDownload(String content, String logFileName, String zipFileName, HttpServletResponse response){
        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        OutputStream out = null;
        Writer writer = null;
        try {
            response.setContentType("application/zip");
            response.setCharacterEncoding(DEFAULT_CHARTER);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipFileName, DEFAULT_CHARTER));
            out = response.getOutputStream();
            bos = new BufferedOutputStream(out, 64 * 1024);
            zos = new ZipOutputStream(bos);
            zos.setLevel(6);
            zos.putNextEntry(new ZipEntry(logFileName));
            writer = new OutputStreamWriter(zos, DEFAULT_CHARTER);
            writer.write(content);
            writer.flush();
            zos.closeEntry();
        } catch (UnsupportedEncodingException e) {
            LOG.error("zip encoding error.", e);
        } catch (IOException e) {
            LOG.error("zip create error.", e);
        } finally {
            IOUtils.close(writer);
            IOUtils.close(zos);
            IOUtils.close(bos);
            IOUtils.close(out);
        }
    }

    /**
     * 纯文本下载
     * @param fileName
     * @param resJson
     * @param resp
     */
    public static void plainTextDownload(String fileName, String resJson, HttpServletResponse resp){
        // 写入文件流
        if (StringUtils.isEmpty(fileName)) {
            fileName = DateUtils.getDateStr(new Date(), DateUtils.DATA_PATTERN_WITHOUT_SYMBOL);
        }
        resp.setContentType("text/plain");
        resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        OutputStream os = null; // 输出流
        try {
            os = resp.getOutputStream();
            os.write(resJson.getBytes(DEFAULT_CHARTER));
            os.flush();
        } catch (IOException e) {
            LOG.error("export logs is error:{}", e);
        } finally {
            IOUtils.close(os);
        }
    }
}
