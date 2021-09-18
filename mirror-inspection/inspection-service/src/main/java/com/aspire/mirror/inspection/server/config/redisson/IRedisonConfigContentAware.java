package com.aspire.mirror.inspection.server.config.redisson;

/**
* redisson配置内容感应接口    <br/>
* Project Name:index-adapt
* File Name:IRedisonConfigContentAware.java
* Package Name:com.aspire.mirror.inspection.server.config.redisson
* ClassName: IRedisonConfigContentAware <br/>
* date: 2018年12月10日 下午2:48:31 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface IRedisonConfigContentAware {
	/**
	* 返回Redisson配置内容的json表示. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public String formatConfig2JsonContent();
}
