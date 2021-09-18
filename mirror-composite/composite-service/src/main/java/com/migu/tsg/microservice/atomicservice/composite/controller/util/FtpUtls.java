package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author menglinjie
 *
 */
public class FtpUtls {

	/**
	 * Description: 向FTP服务器上传图片
	 *
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param fileName
	 *            文件名
	 * @param filename
	 *@param input
	 *            输入流  @return 成功返回图片路径，否则返回null
	 */
	/**
	 * @param host   FTP服务器host
	 * @param port   FTP服务器端口
	 * @param username   FTP登录账号
	 * @param password   FTP登录密码
	 * @param nginxPort
	 * @param fileName   文件名
	 * @param resourceHandler
	 * @param input    输入流  @return 成功返回图片路径，否则返回null  @return
	 */
	public static String uploadFile(String host, int port, String username, String password,
									int nginxPort, String resourceLocations, String resourceHandler,
									String fileName, InputStream input) {
		String result = null;
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("/yyyy/MM/dd");
		String uuidFile = String.valueOf(UUIDUtil.getGeneratID());
		String filePath = resourceLocations + df.format(date)+"/"+uuidFile;
		String str = df.format(date).substring(1);
		String filePath1 = str+"/"+uuidFile;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// 连接FTP服务器
			ftp.connect(host, port);
			// 登录
			ftp.login(username, password);
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
					if (null == dir || "".equals(dir)) {
						continue;
					}
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
			//文件前缀名
			String preName = fileName.substring(0,fileName.lastIndexOf("."));
			if (fileNames.length >= 2) {
				preName = preName + "." + fileNames[fileNames.length - 1];
			}
			String preName1=new String(preName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
			if (!ftp.storeFile(preName1, input)) {
				return result;
			}
			input.close();
			ftp.logout();
//			result = filePath + "/" + preName;
			result = host + ":" +nginxPort + resourceHandler+filePath1+"/" + preName;
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

	/**
	 * MultipartFile 转 File
	 * @param file
	 * @throws Exception
	 */
	public static File multipartFileToFile(@RequestParam MultipartFile file ) throws Exception {

		File toFile = null;
		if (file.equals("") || file.getSize() <= 0) {
			file = null;
		} else {
			InputStream ins = null;
			ins = file.getInputStream();
			toFile = new File(file.getOriginalFilename());
			inputStreamToFile(ins, toFile);
			ins.close();
		}
		return toFile;
	}
	static void inputStreamToFile(InputStream ins, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
