package com.aspire.mirror.ops.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.aspire.mirror.ops.api.util.EncryptUtil;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: GeneralInitializer
 * <p/>
 *
 * 类功能描述: 通用初始化组件
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月8日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Component
public class GeneralInitializer implements ApplicationRunner {
	@Value("${general.config.aes-key:}")
	private String aesKey;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		EncryptUtil.setDefaultAesKey(aesKey);
	}
}
