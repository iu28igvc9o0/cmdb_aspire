package com.aspire.mirror.alert.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 告警业务工具类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.util
 * 类名称:    AntZipUtil.java
 * 类描述:    Zip 解压工具类
 * 创建人:    LiangJun
 * 创建时间:  2019/7/11 11:55
 * 版本:      v1.0
 */
public class AntZipUtil {

    private static final Logger logger = LoggerFactory.getLogger(AntZipUtil.class);
    private static final String SYSTEM_FILE_SEPRATOR = System.getProperty("file.separator");
    /**
     * 调用org.apache.tools.zip实现解压缩，支持目录嵌套和中文名
     * 也可以使用java.util.zip不过如果是中文的话，解压缩的时候文件名字会是乱码。原因是解压缩软件的编码格式跟java.util.zip.ZipInputStream的编码字符集(固定是UTF-8)不同
     *
     * @param zipFileName
     *            要解压缩的文件
     * @param outputDirectory
     *            要解压到的目录
     * @throws Exception
     */
    public static boolean unZip(String zipFileName, String outputDirectory) {
        boolean flag = false;
        try {
            org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(zipFileName);
            java.util.Enumeration e = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            while (e.hasMoreElements()) {
                zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
                // System.out.println("unziping " + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                    logger.info("创建目录：" + outputDirectory + File.separator + name);
                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');
                    // System.out.println("测试文件1：" +fileName);
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0,
                                fileName.lastIndexOf("/")));
                        fileName = fileName.substring(
                                fileName.lastIndexOf("/") + 1, fileName
                                        .length());
                    }

                    File f = new File(outputDirectory + File.separator
                            + zipEntry.getName());

                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);

                    byte[] by = new byte[1024];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    out.close();
                    in.close();
                }
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    /**
     * 创建目录
     *
     * @param directory
     *            父目录
     * @param subDirectory
     *            子目录
     */
    private static void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (StringUtils.isEmpty(subDirectory) && !fl.exists())
                fl.mkdir();
            else if (StringUtils.isNotEmpty(subDirectory)) {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
