package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 *  FileUtil
 * 文件工具类
 **/
public class FileUtil {

//    /**
//     * 复制整个文件夹内容
//     * @param oldPath String 原文件路径 如：c:/fqf
//     * @param newPath String 复制后路径 如：f:/fqf/ff
//     * @return boolean
//     */
//    public static void copyFolder(String oldPath, String newPath) {
//        try {
//            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
//            File a=new File(oldPath);
//            String[] file=a.list();
//            File temp=null;
//            for (int i = 0; i < file.length; i++) {
//                if(oldPath.endsWith(File.separator)){
//                    temp=new File(oldPath+file[i]);
//                }
//                else{
//                    temp=new File(oldPath+ File.separator+file[i]);
//                }
//                if(temp.isFile()){
//                    FileInputStream input = new FileInputStream(temp);
//                    FileOutputStream output = new FileOutputStream(newPath + "/" +
//                            (temp.getName()).toString());
//                    byte[] b = new byte[1024 * 5];
//                    int len;
//                    while ( (len = input.read(b)) != -1) {
//                        output.write(b, 0, len);
//                    }
//                    output.flush();
//                    output.close();
//                    input.close();
//                }
//                if(temp.isDirectory()){//如果是子文件夹
//                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
//                }
//            }
//        }
//        catch (Exception e) {
//            System.out.println("复制整个文件夹内容操作出错");
//            e.printStackTrace();
//        }
//    }
    public static void copyDir(File file, String newPath) throws IOException {
//        File file;
//        if (isClassPath) {
//            file = ResourceUtils.getFile("download/vulReport/");
//        } else {
//            file = new File(oldPath);        //文件名称列表
//        }
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(file.getAbsolutePath() + File.separator + filePath[i])).isDirectory()) {
                copyDir(new File(file.getAbsolutePath() + File.separator + filePath[i]), newPath  + File.separator + filePath[i]);
            }

            if (new File(file.getAbsolutePath()  + File.separator + filePath[i]).isFile()) {
                copyFile(file.getAbsolutePath() + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

        }
    }
    public static void copyFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(oldFile);
            out = new FileOutputStream(file);
            byte[] buffer=new byte[2097152];
            while((in.read(buffer)) != -1){
                out.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

    }

    /**
     *  InputStream 转 File
     */
    public static void inputStreamToFile (InputStream ins,File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("InputStream to file is error {}", e);
        } finally {
            if(null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != ins) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
