package com.aspire.mirror.common.util;

import lombok.extern.slf4j.Slf4j;
//import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
//import java.util.zip.ZipEntry;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.util
 * 类名称:    ZipUtil.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/16 13:43
 * 版本:      v1.0
 */
@Slf4j
public class ZipUtil {
    public static void toZip(String srcDir, String outPathFile, boolean isDelSrcFile) throws Exception {
        long start = System.currentTimeMillis();
        FileOutputStream out = null;
        ZipOutputStream zos = null;
        try {
            out = new FileOutputStream(new File(outPathFile));
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            if (!sourceFile.exists()) {
                throw new Exception("待压缩文件不存在");
            }
            compress(sourceFile, zos, sourceFile.getName());
            if (isDelSrcFile) {
                delDir(srcDir);
            }
            log.info("源文件{}，压缩到{}完成，是否删除源文件{}，耗时{}ms", srcDir, outPathFile, isDelSrcFile, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("ZipUtil[toZip] is error, {}", e);
        } finally {
            if (zos != null) {
                zos.closeEntry();
                zos.close();
            }
            if (out != null) out.close();
        }
    }

    public static void zipFileAndEncrypt(String filePath, String zipFileName, String password) {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        List<File> filesToAdd = Arrays.asList(
                new File(filePath)
        );
        net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(zipFileName, password.toCharArray());
        try {
            zipFile.addFiles(filesToAdd, zipParameters);
        } catch (ZipException e) {
            log.error("ZipUtil[zipFileAndEncrypt] is error!", e);
        }
    }

    public static void zipFileAndEncrypt(List<File> filePathList, String zipFileName, String password) {
        ZipParameters zipParameters = new ZipParameters();
        net.lingala.zip4j.ZipFile zipFile = null;
        if (StringUtils.isNotEmpty(password)) {
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
            zipFile = new net.lingala.zip4j.ZipFile(zipFileName, password.toCharArray());
        } else {
            zipFile = new net.lingala.zip4j.ZipFile(zipFileName);
        }
//        List<File> filesToAdd = Lists.newArrayList();
//        for (String filePath : filePathList) {
//            filesToAdd.add(new File(filePath));
//        }
//        ZipFile zipFile = new ZipFile(zipFileName, password.toCharArray());
        try {
            zipFile.addFiles(filePathList, zipParameters);
        } catch (ZipException e) {
            log.error("ZipUtil[zipFileAndEncrypt] is error!", e);
        }
    }

    private static void delDir(String dirPath) {
        long start = System.currentTimeMillis();
        try {
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                return;
            }
            if (dirFile.isFile()) {
                dirFile.delete();
                return;
            }
            File[] files = dirFile.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                delDir(files[i].toString());
            }
            dirFile.delete();
            log.info("删除文件{}，耗时：{}", dirPath, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("删除文件{}异常，耗时：{}, 异常原因{}", dirPath, System.currentTimeMillis() - start, e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String dir) throws IOException {
        // 当前的是文件夹，则进行一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();

            //将文件夹添加到下一级打包目录
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(dir + "/"));

            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                compress(files[i], out, dir + files[i].getName());
            }
        } else { // 当前是文件
            // 输入流
            FileInputStream inputStream = new FileInputStream(file);
            // 标记要打包的条目
            out.putNextEntry(new org.apache.tools.zip.ZipEntry(dir));
            // 进行写操作
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            // 关闭输入流
            inputStream.close();
        }
    }

    public static String unZip(String path) {
        int count = -1;
        String savepath = "";
        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        savepath = path.substring(0, path.lastIndexOf(".")); //保存解压文件目录
        new File(savepath).mkdir(); //创建保存目录
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(path, "gbk"); //解决中文乱码问题
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                byte buf[] = new byte[2048];
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String filename = entry.getName();
                boolean ismkdir = false;
                if (filename.lastIndexOf("/") != -1) { //检查此文件是否带有文件夹
                    ismkdir = true;
                }
                filename = savepath + File.separator + filename;
                if (entry.isDirectory()) { //如果是文件夹先创建
                    file = new File(filename);
                    file.mkdirs();
                    continue;
                }
                file = new File(filename);
                if (!file.exists()) { //如果是目录先创建
                    if (ismkdir) {
                        new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                    }
                }
                file.createNewFile(); //创建文件
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, 2048);
                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
                bos.close();
                fos.close();
                is.close();
            }
            zipFile.close();
            return savepath;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
