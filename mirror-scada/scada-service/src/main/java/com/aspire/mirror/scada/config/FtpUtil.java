/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.aspire.mirror.scada.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ftp工具
 * 
 * @author wangxiaorong
 *
 */
@Slf4j
public class FtpUtil {
	private String userName;

	private String passWord;

	private String ip;

	private int port;
	// 文件存放的目录
	private String CURRENT_DIR;

	public static final String DIRSPLIT = "/";

	public String getCURRENT_DIR() {
		return CURRENT_DIR;
	}

	public void setCURRENT_DIR(String cURRENTDIR) {
		CURRENT_DIR = cURRENTDIR;
	}
	
	public FtpUtil(String ip, int port, String userName, String password) {
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.passWord = password;
	}
	
	// ftp客户端
	private FTPClient ftpClient = new FTPClient();
	/**
	 * 
	 * 功能：根据文件名称，下载文件流
	 * 
	 * @param
	 * @return
	 * @throws IOException
	 */
	public boolean downloadFile(String remote,String local) {
		boolean result = false;
		OutputStream os=null;
		try {
			// 建立连接
			connectToServer();
//			ftpClient.listFiles(pathname, FTPFileFilters.)
			ftpClient.enterLocalPassiveMode();
			// 设置传输二进制文件
			setFileType(FTP.BINARY_FILE_TYPE);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				throw new IOException("failed to connect to the FTP Server:" + ip);
			}
			if(ftpClient.changeWorkingDirectory(CURRENT_DIR)) {
				os = new FileOutputStream(local);
				result = ftpClient.retrieveFile(remote, os);
			}
		} catch (FTPConnectionClosedException e) {
			log.error("ftp连接被关闭！", e);
		} catch (FileNotFoundException e) {
			log.error("未找到文件", e);
		} catch (IOException e) {
			log.error("IO异常", e);
		} catch (Exception e) {
			log.error("其它异常", e);
		} finally {
			if(os!=null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public InputStream startInputStream(String remote) {
		try {
			// 建立连接
			connectToServer();
			ftpClient.enterLocalPassiveMode();
			// 设置传输二进制文件
			setFileType(FTP.BINARY_FILE_TYPE);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				throw new IOException("failed to connect to the FTP Server:" + ip);
			}
			ftpClient.changeWorkingDirectory(CURRENT_DIR);
			// ftp文件获取文件
			return ftpClient.retrieveFileStream(remote);
		} catch (FTPConnectionClosedException e) {
			log.error("ftp连接被关闭！", e);
		} catch (FileNotFoundException e) {
			log.error("未找到文件", e);
		} catch (IOException e) {
			log.error("IO异常", e);
		} catch (Exception e) {
			log.error("其它异常", e);
		} 
		return null;
	}
	
	public boolean completeInputStream(InputStream is) {
		if(is!=null) {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			return ftpClient.completePendingCommand();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 设置传输文件的类型[文本文件或者二进制文件]
	 * 
	 * @param fileType --BINARY_FILE_TYPE、ASCII_FILE_TYPE
	 */
	private void setFileType(int fileType) {
		try {
			ftpClient.setFileType(fileType);
		} catch (Exception e) {
			log.error("ftp设置传输文件的类型时失败！", e);
		}
	}

	/**
	 * 
	 * 功能：关闭连接
	 */
	public void closeConnect() {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (Exception e) {
			log.error("ftp连接关闭失败！", e);
		}
	}

	/**
	 * 连接到ftp服务器
	 */
	private void connectToServer() throws FTPConnectionClosedException, Exception {
		if (!ftpClient.isConnected()) {
			int reply;
			try {
				ftpClient = new FTPClient();
				ftpClient.connect(ip, port);
				ftpClient.login(userName, passWord);
				reply = ftpClient.getReplyCode();

				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					log.info("connectToServer FTP server refused connection.");
				}

			} catch (FTPConnectionClosedException ex) {
				log.error("服务器:IP：" + ip + "没有连接数！there are too many connected users,please try later", ex);
				throw ex;
			} catch (Exception e) {
				log.error("登录ftp服务器【" + ip + "】失败", e);
				throw e;
			}
		}
	}


	public List<String> getIncrementFiles(String pathname,String currentFileName,String prefix) throws Exception{
		try {
			// 建立连接
			connectToServer();
			ftpClient.enterLocalPassiveMode();
//			ftpClient.listFiles(pathname, FTPFileFilters.)
			FTPFile[] files = ftpClient.listFiles(pathname);
			if(files!=null) {
				List<String> results=new ArrayList<>();
				for(FTPFile ftpFile:files) {
					if(ftpFile==null||ftpFile.isDirectory()) {
						continue;
					}
					if(StringUtils.isNotEmpty(prefix)&&!ftpFile.getName().startsWith(prefix)) {
						continue;
					}
					if(currentFileName==null||ftpFile.getName().compareTo(currentFileName)>0) {
						results.add(ftpFile.getName());
					}
				}
				return results;
			}
		} catch (FTPConnectionClosedException e) {
			log.error("ftp连接被关闭！", e);
			throw e;
		} catch(Exception e) {
			log.error("ftp连接被关闭！", e);
			throw e;
		}
		return null;
	}
	
	public List<String> listFiles(String pathname) throws Exception{
		try {
			// 建立连接
			connectToServer();
			ftpClient.enterLocalPassiveMode();
//			ftpClient.listFiles(pathname, FTPFileFilters.)
			FTPFile[] files = ftpClient.listFiles(pathname);
			if(files!=null) {
				List<String> results=new ArrayList<>();
				for(FTPFile ftpFile:files) {
					if(ftpFile==null||ftpFile.isDirectory()) {
						continue;
					}
					results.add(ftpFile.getName());
				}
				return results;
			}
		} catch (FTPConnectionClosedException e) {
			log.error("ftp连接被关闭！", e);
			throw e;
		} catch(Exception e) {
			log.error("ftp连接被关闭！", e);
			throw e;
		}
		return null;
	}

}
