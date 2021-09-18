/**
 * @Title: FileUtil.java
 * @Package com.migu.tsg.microservice.atomicservice.ldap.util
 * @Description: 
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月16日 下午3:02:01
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.common.util;

import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @ClassName: FileUtil
 * @Description: 文件类
 * @author botao gao
 * @date 2018年1月16日 下午3:02:01
 *
 */

@Slf4j
public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

	public static String readFile(String path) {
		InputStream in = null;
		try {
			ClassPathResource classPathResource = new ClassPathResource(path);
			in = classPathResource.getInputStream();
			// size 为字串的长度 ，这里一次性读完
			int size = in.available();
			byte[] buffer = new byte[size];
			int bufferSize = in.read(buffer);
			log.info("Total read file {}, content size:{}", path, bufferSize);
			in.close();
			return new String(buffer, "UTF-8");
		} catch (IOException e) {
			LOGGER.info("error when read file to String, fileName=" + path);
			return null;
		} finally {
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}
	}
}
