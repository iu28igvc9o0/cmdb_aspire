package com.aspire.mirror.alert.server.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lupeng
 */
public class FtpUtls {

    /**
     * Description: 向FTP服务器上传图片
     *
     * @param host     FTP服务器hostname
     * @param port     FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param fileName 文件名
     * @param input    输入流
     * @return 成功返回图片路径，否则返回null
     */
    public static String uploadImage(String host, int port, String username, String password, String fileName, InputStream input) {
        String result = null;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("/yyyy/MM/dd");
        String filePath = "/download" + df.format(date);

        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);// 连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            // 切换到上传目录
            if (!ftp.changeWorkingDirectory(filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || dir.isEmpty())
                        continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 上传文件
            String[] fileNames = fileName.split("\\.");
            String formalName = UUIDUtil.getUUID();
            if (fileNames.length >= 2) {
                formalName = formalName + "." + fileNames[fileNames.length - 1];
            }
            if (!ftp.storeFile(formalName, input)) {
                return result;
            }
            input.close();
            ftp.logout();
            result = filePath + "/" + formalName;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

	/*public static void main(String[] args) throws Exception {
		FileInputStream inputStream = new FileInputStream(new File("D:\\logo2.png"));
		System.out.println(FtpUtls.uploadImage("10.12.12.139", 21, "ftp", "aspire+888", "logo2.png", inputStream));
	}*/
}
