package com.aspire.mirror.indexproxy.indexprocess;

import com.aspire.mirror.indexproxy.domain.StandardIndex;

/**
* 标准指标处理接口    <br/>
* Project Name:index-proxy
* File Name:IStandardIndexProcess.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: IStandardIndexProcess <br/>
* date: 2018年8月16日 下午1:41:51 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
interface IStandardIndexProcess {
	/**
	* 标准指标处理接口. <br/>
	*
	* 作者： pengguihua
	* @param index
	* @return
	*/  
	public boolean processStandardIndex(StandardIndex index);
}
